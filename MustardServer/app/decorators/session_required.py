from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse
from app.models import Sessions


def decorator(function):
    def wrapper(request):
        try:
            session = Sessions.objects.get(session_id=request.session.session, active=True)

        except ObjectDoesNotExist:
            return HttpResponse(status=401)

        return function(request, session)

    return wrapper
