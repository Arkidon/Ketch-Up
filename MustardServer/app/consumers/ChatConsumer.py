from asgiref.sync import async_to_sync
from channels.generic.websocket import WebsocketConsumer
from django.core.exceptions import ObjectDoesNotExist

from app.models import Sessions


class ChatConsumer(WebsocketConsumer):
    user_count = 0

    def __init__(self, *args, **kwargs):
        super().__init__(args, kwargs)
        self.user_id = None

    def connect(self):
        # Checks if the necessary headers are included.
        # If they are not, closes the connection with a 1002 code.
        if 'User' not in self.scope or 'Session' not in self.scope:
            self.disconnect(1002)

        user_id = self.scope["User"]
        session_token = self.scope["Session"]

        try:
            Sessions.objects.get(user=self.scope["User"], session_token=self.scope["Session"], active=True)

        except ObjectDoesNotExist:
            self.disconnect()

        self.channel_name = ChatConsumer.user_count
        ChatConsumer.user_count += 1
        print("Self channel layer name = ", self.channel_name)
        self.accept()



    def disconnect(self, close_code):
        print("User disconnected")

    def receive(self, text_data):  # noqa
        print(text_data + "Jajant")
        self.send(text_data=text_data + "Jajant")

        # Send message to room group
        async_to_sync(self.channel_layer.group_send)(
            self.room_group_name, {"type": "chat_message", "message": text_data}
        )
