from django.db import models


class Users(models.Model):
    username = models.CharField(unique=True, max_length=25)
    password = models.CharField(max_length=50)
    user_id = models.BigAutoField
