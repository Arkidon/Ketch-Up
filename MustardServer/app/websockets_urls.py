from django.urls import path
from app.consumers import ChatConsumer

websocket_urlpatterns = [
    path("ws-test", ChatConsumer.ChatConsumer.as_asgi()),
]
