from django.db import models
from django.conf import settings


class Sessions(models.Model):
    session_id = models.BigAutoField(primary_key=True)
    user = models.ForeignKey('Users', on_delete=models.DO_NOTHING)
    session_token = models.CharField(max_length=32, unique=True)
    active = models.BooleanField()


class Users(models.Model):
    user_id = models.BigAutoField(primary_key=True)
    username = models.CharField(unique=True, max_length=settings.USERNAME_MAX_LENGTH)
    password = models.CharField(max_length=settings.PASSWORD_MAX_LENGTH)


class UserSettings(models.Model):
    user_id = models.OneToOneField('Users', on_delete=models.DO_NOTHING, primary_key=True)
    user_pfp = models.CharField(max_length=250)
    user_alias = models.CharField(max_length=settings.USERNAME_MAX_LENGTH)
    user_status = models.CharField(max_length=60)


class UserRelations(models.Model):
    relationship_id = models.BigAutoField(primary_key=True)
    user_sender = models.ForeignKey('Users', on_delete=models.DO_NOTHING, related_name='user_sender')
    user_target = models.ForeignKey('Users', on_delete=models.DO_NOTHING, related_name='user_target')
    date = models.DateField()
    status = models.PositiveSmallIntegerField()
    # status: 0 -Relationship requested
    # status: 1 -Relationship accepted
    # status: 2 -Relationship deleted
    # status: 3 -Relationship blocked
    # status: 4 -Relationship rejected


class ChatMemberships(models.Model):
    membership_id = models.BigAutoField(primary_key=True)
    user_id = models.ForeignKey('Users', on_delete=models.DO_NOTHING)
    chat_id = models.ForeignKey('Chats', on_delete=models.DO_NOTHING)
    role = models.CharField(max_length=20)


class Chats(models.Model):
    chat_id = models.BigAutoField(primary_key=True)
    creation_date = models.DateField()


class ChatEntries(models.Model):
    entry_id = models.BigAutoField(primary_key=True)
    sender = models.ForeignKey('ChatMemberships', on_delete=models.DO_NOTHING)
    chat_id = models.ForeignKey('Chats', on_delete=models.DO_NOTHING)
    text = models.CharField(max_length=300)
    contains_attachment = models.BooleanField(default=False)
    response_to = models.ForeignKey('ChatEntries', on_delete=models.DO_NOTHING)
    date = models.DateField()


class EntriesAttachments:
    attachment_id = models.BigAutoField(primary_key=True)
    entry_id = models.ForeignKey('ChatEntries', on_delete=models.DO_NOTHING)
    location = models.CharField(max_length=250)
    type = models.CharField()

