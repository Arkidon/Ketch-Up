from channels.generic.websocket import WebsocketConsumer


class ChatConsumer(WebsocketConsumer):
    def connect(self):
        print('User connected')
        self.accept()
        self.send(text_data="RUA?")

    def disconnect(self, close_code):
        print("User disconnected")

    def receive(self, text_data): # noqa
        self.send(text_data=text_data + "Jajant")
