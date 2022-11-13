import base64
import os.path
from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse, JsonResponse
from django.views.decorators.http import require_http_methods

from django.conf import settings
from app.models import Users, UserRelations
from app.utils import validators


@require_http_methods('GET')
def view(request):
    image_path = os.path.join(settings.BASE_DIR, 'testlogo.png')

    # Reads the image from disk
    with open(image_path, 'rb') as image_reader:
        image_data = image_reader.read()

    # Placeholder image codified to base64, applied to every user for testing
    image_data = base64.b64encode(image_data).decode('utf-8')
    # Checks if both users exits

    if 'username' not in request.GET:
        return HttpResponse(status=400)
    username = request.GET['username']
    users_list = []
    try:
        Users.objects.get(username=username)
    # Returns a 401 error if the user does not exist
    except ObjectDoesNotExist:
        return HttpResponse(status=401)
    try:
        user = Users.objects.get(username=username)
    except ObjectDoesNotExist:
        # if user don't have friend returns an error
        return HttpResponse(status=400)
    # Gets all friends of a user

    user_relation_list = UserRelations.objects.filter(user_target=user, status=1) | \
        UserRelations.objects.filter(user_sender=user, status=1)

    if len(user_relation_list) == 0:
        # if user don't have friend returns an error
        return HttpResponse(status=400)
    for user_relation in user_relation_list:
        # for all friends if user_target is the user itself appends user_sender if not appends user_target
        if user_relation.user_target.username != username:
            friend = user_relation.user_target
        else:
            friend = user_relation.user_sender
        # appends user to jsonResponse
        users_list.append({"username": friend.username,
                           "picture": image_data,
                           "id": friend.user_id})

    return JsonResponse({'users': users_list})
