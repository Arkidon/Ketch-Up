import json
from json import JSONDecodeError
from django.conf import settings
from django.http import HttpRequest


def user_data_validation(request: HttpRequest) -> dict | bool:
    """
    Validates a request body that contains user credentials

    :param HttpRequest request : A request object
    :return: A dictionary that contains the credentials or False if the validation is not succesful
    :rtype: (dict, bool)
    """

    try:
        body = json.loads(request.body.decode('utf-8'))

    except JSONDecodeError:
        # Print for debugging purposes
        print("Invalid JSON body")
        return False

    # Request body validation
    if 'username' not in body:
        return False

    # Checks the model size limit
    if len(body['username']) > settings.USERNAME_MAX_LENGTH:
        return False

    if 'password' not in body:
        return False

    # Checks the model size limit
    if len(body['password']) > settings.PASSWORD_MAX_LENGTH:
        return False

    return body
