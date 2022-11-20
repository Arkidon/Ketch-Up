import base64
import os

from django.conf import settings
from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse, HttpResponse
from django.views.decorators.http import require_http_methods

from app.decorators import session_required
from app.models import Users


@require_http_methods('GET')
@session_required.decorator
def view(request, session):
    user = session.user
    image_path = os.path.join(settings.BASE_DIR, 'testlogo.png')

    # Reads the image from disk
    with open(image_path, 'rb') as image_reader:
        image_data = image_reader.read()
    try:
        selfUser = Users.objects.get(user_id=user.user_id)
    except ObjectDoesNotExist:
        return HttpResponse(status=400)
    # Placeholder image codified to base64, applied to every user for testing
    image_data = base64.b64encode(image_data).decode('utf-8')
    return JsonResponse({'username': selfUser.username, 'status': "placeholder", 'picture': image_data})
