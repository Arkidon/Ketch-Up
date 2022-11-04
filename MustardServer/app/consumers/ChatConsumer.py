from asgiref.sync import async_to_sync
from channels.generic.websocket import WebsocketConsumer


class ChatConsumer(WebsocketConsumer):
    def __init__(self, *args, **kwargs):
        super().__init__(args, kwargs)
        self.room_group_name = "test"

    def connect(self):
        print('User connected')
        self.accept()

    def disconnect(self, close_code):
        print("User disconnected")

    def receive(self, text_data): # noqa
        print(text_data + "Jajant")
        self.send(text_data=text_data + "Jajant")

        # Send message to room group
        async_to_sync(self.channel_layer.group_send)(
            self.room_group_name, {"type": "chat_message", "message": text_data}
        )
