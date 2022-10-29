from channels.generic.websocket import WebsocketConsumer


class ChatConsumer(WebsocketConsumer):
    def connect(self):
        print('User connected')
        self.accept()

    def disconnect(self, close_code):
        print("User disconnected")

    def receive(self, text_data): # noqa
        print(text_data + "Jajant")
        self.send(text_data=text_data + "Jajant")
