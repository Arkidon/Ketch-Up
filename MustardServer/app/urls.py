from django.urls import path
from .views import login, sign_up, search_users, ping_server

urlpatterns = [
    path('login', login.view),
    path('signup', sign_up.view),
    path('search-users', search_users.view),  # Receives URL parameters
    path('', ping_server.view)
]
