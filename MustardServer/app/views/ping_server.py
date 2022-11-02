from django.http import HttpResponse
from django.views.decorators.http import require_http_methods


@require_http_methods('GET')
def view(request):
    return HttpResponse(status=200, content="KetchUp")
