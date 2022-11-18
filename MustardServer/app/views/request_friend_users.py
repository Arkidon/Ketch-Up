import base64
import os.path
from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse, JsonResponse
from django.views.decorators.http import require_http_methods
from django.conf import settings
from app.models import Users, UserRelations
from app.decorators import session_required
from app.utils import validators


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

    user_relation_list = UserRelations.objects.filter(user_target=user, status=1) | \
        UserRelations.objects.filter(user_sender=user, status=1)

    users_list = []

    for user_relation in user_relation_list:
        # for all friends if user_target is the user itself appends user_sender if not appends user_target
        if user_relation.user_target.username != user.username:
            friend = user_relation.user_target
        else:
            friend = user_relation.user_sender
        # appends user to jsonResponse
        users_list.append({"id": friend.user_id,
                          "username": friend.username,
                          "picture": image_data})

    return JsonResponse({'users': users_list})
