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
        async_to_sync(self.channel_layer.group_add)(f"user.{self.user.user_id}", self.channel_name)

        # Gets all the chat memberships the user posses
        chat_memberships = ChatMemberships.objects.filter(user=user)

        # Adds the user to the groups in order to receive updates from the chats
        for membership in chat_memberships:
            async_to_sync(self.channel_layer.group_add)(f"chat.{membership.chat_id}", self.channel_name)

        print("User connected")

    def receive(self, text_data):  # noqa
        try:
            # Decodes the message into a json object
            json_message = json.loads(text_data)

        except JSONDecodeError:
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
            chat_membership = ChatMemberships(user=self.user, chat=chat)

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

        # Sends the message to the recipient user consumer
        async_to_sync(self.channel_layer.send)(f"chat.{chat_id}", {"type": "incoming.message",
                                                                   "user_id": self.user.user_id,
                                                                   "message": message})

        # Creates the json response
        json_response = {'entry_id': chat_entry.entry_id,
                         'user_sender': chat_entry.sender.user_id,
                         'text': chat_entry.text}

        # Returns a validation to the client that the message has been received and stored
        self.send(json.dumps(json_response))

    def handle_new_message(self, json_message):
        """
        Handles a new message received from an user, stores it in the database and sends
        it to the users that participate in the chat, if the user is not connected,
        generates a notification for that user.
        """

    def incoming_message(self, event):
        """
        Handles an incoming message from another WebSocket instance
        """
        json_message = {"user_id": event["user_id"], "message": event["message"]}
        self.send(json.dumps(json_message))

    def disconnect(self, close_code):
        # Close the websocket connection
        self.close(4000)
        print("User disconnected")
