from django.urls import path
from .views import *
from .views import login, sign_up, request_users

urlpatterns = [
    path('login', login.view),
    path('signup', sign_up.view),
    path('request-users', request_users.view),
]
