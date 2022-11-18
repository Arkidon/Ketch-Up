import base64
import datetime
import os.path

from django.conf import settings
from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse, HttpResponse
from django.views.decorators.http import require_http_methods

from app.models import Users, UserRelations


@require_http_methods('GET')
def view(request, session): # noqa
    if 'query' not in request.GET or 'self-user' not in request.GET:
        return HttpResponse(status=400)

    username_query = request.GET["query"]
    username_sender = request.GET["self-user"]
    users_list = []

    image_path = os.path.join(settings.BASE_DIR, 'testlogo.png')

    # Reads the image from disk
    with open(image_path, 'rb') as image_reader:
        image_data = image_reader.read()

    # Placeholder image codified to base64, applied to every user for testing
    image_data = base64.b64encode(image_data).decode('utf-8')
    # Checks if both users exits
    try:
        user_query = Users.objects.get(username=username_query)
        user_sender = Users.objects.get(username=username_sender)
    except ObjectDoesNotExist:
        return HttpResponse(status=400)
    # Checks if relation isn't accepted

    try:
        user_relation_list = UserRelations.objects.get(user_sender=user_query,
                                                       user_target=user_sender,
                                                       status=1) | \
                             UserRelations.objects.get(user_sender=user_query,
                                                       user_target=user_sender,
                                                       status=3)
        print(user_relation_list)
        return HttpResponse(status=400)
    except ObjectDoesNotExist:
        pass
    # Checks if relation isn't blocked
    try:
        user_relation_list = UserRelations.objects.get(user_sender=user_sender,
                                                       user_target=user_query,
                                                       status=1) | \
                             UserRelations.objects.get(user_sender=user_sender,
                                                       user_target=user_query,
                                                       status=3)

        return HttpResponse(status=400)
    except ObjectDoesNotExist:
        pass
    # Creates the relation with status accepted
    user_relation_list = UserRelations(user_sender=user_sender,
                                       user_target=user_query,
                                       date=datetime.datetime.now(datetime.timezone.utc),
                                       status=1)
    user_relation_list.save()

    users_list.append({"username": user_relation_list.user_target.username,
                       "picture": image_data,
                       "id": user_relation_list.user_target.user_id})

    return JsonResponse({'users': users_list})
