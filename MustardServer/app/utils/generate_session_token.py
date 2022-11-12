from django.core.exceptions import ObjectDoesNotExist
from app.models import Sessions, Users
from uuid import uuid4


def generate(user: Users) -> str:
    """
    Generates a session token

    :param: Users user The user who owns the session
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
            Sessions.objects.create(user=user, session_token=session_token, active=True)
            return session_token
