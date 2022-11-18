from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse
from app.models import Sessions, Users


def decorator(function):
    """
    Checks if the user has an active session, if there is no active session, returns a 401 error
    """

    def wrapper(request):
        # Checks if the necessary headers are present
        if 'user' not in request.headers or 'session' not in request.headers:
            return HttpResponse(status=400)

        # Checks if the session exists
        try:
            user = Users.objects.get(user_id=request.headers["user"])
            session = Sessions.objects.get(user=user, session_token=request.headers["session"], active=True)

        except ObjectDoesNotExist:
            return HttpResponse(status=401)

        return function(request, session)

    return wrapper
