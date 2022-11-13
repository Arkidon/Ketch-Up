from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse
from django.views.decorators.http import require_http_methods

from app.models import Users, Sessions
from app.utils import validators


@require_http_methods('POST')
def view(request):
    credentials = validators.credential_check_validation(request)

    if credentials is False:
        return HttpResponse(status=400)

    user_id = credentials['id']
    session_token = credentials['session_token']

    try:
        user = Users.objects.get(user_id=user_id)
        Sessions.objects.get(user=user, session_token=session_token, active=True)
    # Returns a 401 error if the user does not exist
    except ObjectDoesNotExist:
        return HttpResponse(status=401)

    return HttpResponse(status=200)
