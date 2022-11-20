from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse, JsonResponse
from django.views.decorators.http import require_http_methods
from app.models import  ChatMemberships
from app.decorators import session_required


@require_http_methods('GET')
@session_required.decorator
def view(request, session):

    user = session.user
    users_chats = ChatMemberships.objects.filter(user=user)
    chat_memberships_list = []
    for users_chat in users_chats:
        try:
            chat_memberships_list.append(ChatMemberships.objects.get(chat=users_chat.chat, user=user))
        except ObjectDoesNotExist:
            return HttpResponse(status=400)

    users_list = []

    for chat_membership in chat_memberships_list:
        # for all friends if user_target is the user itself appends user_sender if not appends user_target

        # appends user to jsonResponse
        users_list.append({
                           "chat_date": chat_membership.chat.creation_date,
                           "chat_id": chat_membership.chat.chat_id,
                           "membership_id": chat_membership.membership_id,
                           "role": chat_membership.role})

    return JsonResponse({'self_user_chats': users_list})
