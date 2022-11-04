from django.urls import path
from .views import login, sign_up, request_users, ping_server

urlpatterns = [
    path('login', login.view),
    path('signup', sign_up.view),
    path('request-users', request_users.view),
    path('', ping_server.view)
]
