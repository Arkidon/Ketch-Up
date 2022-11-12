import json
from json import JSONDecodeError
from typing import Union

from django.conf import settings
from django.http import HttpRequest


def user_data_validation(request: HttpRequest) -> Union[dict, bool]:
    """
    Validates a request body that contains user credentials

    :param: HttpRequest request : A request object
    :return: A dictionary that contains the credentials or False if the validation is not successful
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


def credential_check_validation(request: HttpRequest) -> Union[dict, bool]:
    """
    Validates a request body that contains user credentials

    :param: HttpRequest request : A request object
    :return: A dictionary that contains the credentials or False if the validation is not successful
    :rtype: (dict, bool)
    """
    try:
        body = json.loads(request.body.decode('utf-8'))

    except JSONDecodeError:
        # Print for debugging purposes
        print("Invalid JSON body")
        return False

    if 'id' not in body or 'session_token' not in body:
        return False

    return body
