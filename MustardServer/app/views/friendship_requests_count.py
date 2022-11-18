from django.http import JsonResponse
from django.views.decorators.http import require_http_methods
from app.decorators import session_required
from app.models import UserRelations


@require_http_methods('GET')
@session_required.decorator
def view(request, session): # noqa
    user = session.user
    # gets all friendship request of the user
    friendship_request = UserRelations.objects.filter(user_target=user, status=0)
    # count of the friendship request
    friendship_requests_count = len(friendship_request)
    return JsonResponse({'count': friendship_requests_count})


