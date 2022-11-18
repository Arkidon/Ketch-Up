from django.urls import path
from .views import login, sign_up, search_users, ping_server, check_credentials, request_friend_users,\
    friendship_requests_list, friendship_requests_count, update_friendship_status

urlpatterns = [
    path('login', login.view),
    path('signup', sign_up.view),
    path('check-credential', check_credentials.view),
    path('request-friend-users', request_friend_users.view),  # Receives URL parameters
    path('search-users', search_users.view),  # Receives URL parameters
    path('friendship-requests-count', friendship_requests_count),
    path('friendship-requests-list', friendship_requests_list),
    path('update-friendship-status', update_friendship_status),
    path('', ping_server.view)
]
