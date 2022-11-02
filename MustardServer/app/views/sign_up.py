from django.contrib.auth.hashers import make_password
from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse
from django.views.decorators.http import require_http_methods

from app.models import Users
from app.utils import validators


@require_http_methods('POST')
def view(request):
    credentials = validators.user_data_validation(request)

    # Checks if the credentials have the correct format, if not, returns a 400 error code
    if credentials is False:
        return HttpResponse(status=400)

    username = credentials['username']
    password = credentials['password']

    try:
        Users.objects.get(username=username)
        return HttpResponse(status=409)

    except ObjectDoesNotExist:
        pass

    Users.objects.create(username=username, password=make_password(password))

    return HttpResponse(status=200)
