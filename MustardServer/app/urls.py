from django.urls import path
from .views import login, sign_up, search_users, ping_server, check_credentials, request_friend_users

urlpatterns = [
    path('login', login.view),
    path('signup', sign_up.view),
    path('check-credential', check_credentials.view),
    path('request-friend-users', request_friend_users.view),  # Receives URL parameters
    path('search-users', search_users.view),  # Receives URL parameters
    path('', ping_server.view)
]
