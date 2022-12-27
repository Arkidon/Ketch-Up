import datetime
import json
from json import JSONDecodeError
from asgiref.sync import async_to_sync
from channels.generic.websocket import WebsocketConsumer
from django.core.exceptions import ObjectDoesNotExist
from app.models import Sessions, Users, ChatMemberships, ChatEntries, Chats


class ChatConsumer(WebsocketConsumer):
    def __init__(self, *args, **kwargs):
        super().__init__(args, kwargs)
        self.user = None

    def connect(self):
        # Accepts the incoming websocket connection
        self.accept()

        # Gets the headers in raw bytes
        raw_headers = dict(self.scope['headers'])

        # Decodes the headers bytes to UTF-8 strings
        headers = {}
        for key, value in raw_headers.items():
            headers[key.decode('utf-8')] = value.decode('utf-8')

        # Checks if the necessary headers are included.
        # If they are not, closes the connection with a 1002 code.
        if 'user' not in headers or 'session' not in headers:
            self.disconnect(1002)
            return

        # Retrieves user id and the session token from the headers
        user_id = headers["user"]
        session_token = headers["session"]

        try:
            # Checks if both the user and the session token exists
            user = Users.objects.get(user_id=user_id)
            session = Sessions.objects.get(user=user, session_token=session_token, active=True)

        except ObjectDoesNotExist:
            # If the user or the session does not exist, ends the connection.
            self.disconnect(4000)
            return

        # Stores the user model object
        self.user = user

        # Adds the user to a group for itself
        async_to_sync(self.channel_layer.group_add)(f"user_{self.user.user_id}", self.channel_name)

        # Gets all the chat memberships the user posses
        chat_memberships = ChatMemberships.objects.filter(user=user)

        # Adds the user to the groups in order to receive updates from the chats
        for membership in chat_memberships:
                async_to_sync(self.channel_layer.group_add)(f"chat_{membership.chat_id}", self.channel_name)

        print("User connected")

    def receive(self, text_data):  # noqa
        try:
            # Decodes the message into a json object
            json_message = json.loads(text_data)

        except JSONDecodeError:
            print("Failed JSON decode:", text_data)

            # If the message doesn't decode to JSON, closes the connection
            self.disconnect(1003)
            return

        # Gets the chat id and the message from the json message
        chat_id = json_message['chat_id']
        message = json_message['message']

        # Gets the chat model from the chat id
        try:
            chat = Chats.objects.get(chat_id=chat_id)

        except ObjectDoesNotExist:
            self.disconnect(1003)
            return

        # Gets the chat membership
        try:
            chat_membership = ChatMemberships.objects.get(user=self.user, chat=chat)

        except ObjectDoesNotExist:
            self.disconnect(1003)
            return

        # Stores the chat entry in the database
        chat_entry = ChatEntries(sender=chat_membership,
                                 chat=chat,
                                 text=message,
                                 contains_attachment=False,
                                 response_to=None,
                                 date=datetime.datetime.now(datetime.timezone.utc)
                                 )

        chat_entry.save()

        # JSON message for the consumers in the chat group that notifies that a new message has been sent
        group_message = json.dumps({"user_sender": chat_membership.membership_id,
                                    "type": "incoming_message",
                                    "entry_id": chat_entry.entry_id,
                                    "message": message,
                                    "chat_id": chat_entry.chat.chat_id,
                                    })

        # Sends the message to the recipient user consumer
        async_to_sync(self.channel_layer.group_send)(f"chat_{chat_id}", {"type": "incoming.message",
                                                                         "text": group_message}
                                                     )

        # Creates the json response
        json_response = {"user_sender": self.user.user_id,
                         "type": "message_confirmation",
                         "entry_id": chat_entry.entry_id,
                         "message": message,
                         "chat_id": chat_entry.chat.chat_id}

        # Returns a validation to the client that the message has been received and stored
        self.send(json.dumps(json_response))

        print("Message received:", message)

    def incoming_message(self, event):
        """
        Handles an incoming message from another WebSocket instance and
        filters it so it doesn´t get sent back to the user that originally sent it
        """

        # Creates a JSON object from the message
        json_message = json.loads(event["text"])

        # If the user id matches, aborts the function
        if json_message["user_sender"] == self.user.user_id:
            return

        self.send(event["text"])

        print("Incoming message called")

    def disconnect(self, close_code):
        # Close the websocket connection
        self.close(4000)
        print("User disconnected")
