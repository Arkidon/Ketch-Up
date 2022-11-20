import base64
import os.path
from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse, JsonResponse
from django.views.decorators.http import require_http_methods
from django.conf import settings
from app.models import Users, UserRelations, ChatMemberships
from app.decorators import session_required
from django.db.models import Q


@require_http_methods('GET')
@session_required.decorator
def view(request, session):

    image_path = os.path.join(settings.BASE_DIR, 'testlogo.png')

    # Reads the image from disk
    with open(image_path, 'rb') as image_reader:
        image_data = image_reader.read()

    # Placeholder image codified to base64, applied to every user for testing
    image_data = base64.b64encode(image_data).decode('utf-8')

    user = session.user
    users_chats = ChatMemberships.objects.filter(user=user)
    chat_memberships_list = []
    for users_chat in users_chats:
        try:
            chat_memberships_list.append(ChatMemberships.objects.get(chat=users_chat.chat, user=user))
            chat_memberships_list.append(ChatMemberships.objects.get(~Q(user=user), chat=users_chat.chat))
        except ObjectDoesNotExist:
            return HttpResponse(status=400)

    users_list = []

    for chat_membership in chat_memberships_list:
        # for all friends if user_target is the user itself appends user_sender if not appends user_target
        if chat_membership.user.user_id != user.user_id:
            friend = chat_membership.user
            # appends user to jsonResponse
            users_list.append({"id": friend.user_id,
                               "username": friend.username,
                               "picture": image_data,
                               "chat_date": chat_membership.chat.creation_date,
                               "chat_id": chat_membership.chat.chat_id,
                               "membership_id": chat_membership.membership_id,
                               "role": chat_membership.role})

    return JsonResponse({'users': users_list})
