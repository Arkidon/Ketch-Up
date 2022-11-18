from django.contrib.auth.hashers import check_password
from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse, JsonResponse
from django.views.decorators.http import require_http_methods
from app.models import Users
from app.utils import generate_session_token, validators


@require_http_methods('POST')
def view(request):
    # Cleans out the user credentials from the request headers
    credentials = validators.user_data_validation(request)

    # Checks if the credentials have the correct format, if not, returns a 400 error code
    if credentials is False:
        return HttpResponse(status=400)

    username = credentials['username']
    password = credentials['password']

    # Checks if the user exists
    try:
        user = Users.objects.get(username=username)

    # Returns a 401 error if the user does not exist
    except ObjectDoesNotExist:
        return HttpResponse(status=401)

    # Validates the password
    if not check_password(password, user.password):
        return HttpResponse(status=401)

    # Generates the session_token
    session_token = generate_session_token.generate(user)

    return JsonResponse({'id': user.user_id, 'session_token': session_token})
