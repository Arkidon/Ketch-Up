from django.core.exceptions import ObjectDoesNotExist
from app.models import Sessions
from uuid import uuid4


def generate() -> str:
    """
    Generates a session token

    :return: A string that contains the session_token
    :rtype: str
    """
    while True:
        # Generates a uuid4 and removes the hyphens
        session_token = str((uuid4())).replace('-', '')

        try:
            # Checks that the uuid has been used
            Sessions.objects.get(session_token=session_token)

        except ObjectDoesNotExist:
            return session_token
