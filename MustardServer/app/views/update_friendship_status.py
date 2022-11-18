from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse
from django.views.decorators.http import require_http_methods
from app.models import UserRelations, Users
from app.decorators import session_required
from app.utils import validators


@require_http_methods('POST')
@session_required.decorator
def view(request, session):
    update_request = validators.update_request_validator(request)
    # Checks if the credentials have the correct format, if not, returns a 400 error code
    if update_request is False:
        return HttpResponse(status=400)
    self_user = session.user
    friend_user_id = request.POST['user']
    status = request.POST['status']
    # Checks if status code is correct, if not, returns 400 error code
    if status != 1 and status != 4:
        return HttpResponse(status=400)
    # Search in database user target and users relations
    try:
        friend_user = Users.Objects.get(user_id=friend_user_id)
        user_friendship = UserRelations.Objects.get(user_target=friend_user, user_sender=self_user, status=0)

    except ObjectDoesNotExist:
        return HttpResponse(status=400)
    # Updates the relation with new status code
    user_friendship.status = status
    user_friendship.save()
    return HttpResponse(status=200)
