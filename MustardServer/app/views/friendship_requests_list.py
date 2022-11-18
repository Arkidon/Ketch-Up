import base64
import os

from django.http import JsonResponse
from django.views.decorators.http import require_http_methods

from app.decorators import session_required
from app.models import UserRelations
from django.conf import settings


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
    users_list = []
    # Gets all friendships requests of the user and sends it to client
    friendship_requests_list = UserRelations.objects.filter(user_target=user, status=0)
    for friendship_requests in friendship_requests_list:
        users_list.append({"id": friendship_requests.user_sender.user_id,
                           "username": friendship_requests.user_sender.username,
                           "picture": image_data})
    return JsonResponse({'users': users_list})
