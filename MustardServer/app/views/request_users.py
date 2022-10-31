import base64
import os.path

from django.http import HttpResponse, JsonResponse
from app.decorators import session_required
from app.models import Users
from django.conf import settings


def view(request):
    # Request method validation
    if request.method != 'GET':
        return HttpResponse(status=405)

    # Temporary code for testing purposes, the intended
    # behaviour is to filter by the users that has been added
    users_models_list = Users.objects.all()

    users_list = []

    image_path = os.path.join(settings.BASE_DIR, 'testlogo.png')

    # Reads the image from disk
    with open(image_path, 'rb') as image_reader:
        image_data = image_reader.read()

    # Placeholder image codified to base64, applied to every user for testing
    image_data = base64.b64encode(image_data).decode('utf-8')

    for user_model in users_models_list:
        users_list.append({"username": user_model.username,
                           "picture": image_data})

    return JsonResponse({'users': users_list})
