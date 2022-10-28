from django.contrib.auth.hashers import check_password
from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse, JsonResponse
from json.decoder import JSONDecodeError
from django.conf import settings
from app.models import Users
from app.decorators import session_required
from app.utils import generate_session_token, validators
import json


def view(request):
    credentials = validators.user_data_validation(request)

    # Checks if the credentials have the correct format, if not, returns a 400 error code
    if credentials is False:
        return HttpResponse(status=400)

    username = credentials['username']
    password = credentials['password']

    # Checks if the user exists
    try:
        user = Users.objects.get(username=username)

    except ObjectDoesNotExist:
        return HttpResponse(status=401)

    # Validates the password
    if not check_password(password, user.password):
        return HttpResponse(status=401)

    # Genereates the session_token
    session_token = generate_session_token.generate()

    return JsonResponse({'session_id': session_token})
