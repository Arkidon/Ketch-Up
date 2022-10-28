from django.urls import path
from .views import login, sign_up

urlpatterns = [
    path('login', login.view),
    path('signup', sign_up.view)
]
