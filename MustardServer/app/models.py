from django.db import models
from django.conf import settings


class Sessions(models.Model):
    session_id = models.BigAutoField(primary_key=True)
    user = models.ForeignKey('Users', on_delete=models.DO_NOTHING)
    session_token = models.CharField(max_length=32)
    active = models.BooleanField()


class Users(models.Model):
    user_id = models.BigAutoField(primary_key=True)
    username = models.CharField(unique=True, max_length=settings.USERNAME_MAX_LENGTH)
    password = models.CharField(max_length=settings.PASSWORD_MAX_LENGTH)


