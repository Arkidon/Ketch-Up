from getpass import getpass
from django.contrib.auth.hashers import make_password
from django.core.exceptions import ObjectDoesNotExist
from django.core.management import BaseCommand
from django.conf import settings
from app.models import Users


class Command(BaseCommand):
    help = 'create a new user'

    def handle(self, *args, **options):
        try:
            # Asks the user for the username
            loop = True
            while loop:
                username = input('Insert your username: ')
                if username == '':
                    self.stderr.write('The username cannot be empty.Try again.')
                    continue
                try:
                    Users.objects.get(username=username)
                    self.stderr.write(f'The username ({username}) already exists. Try again.')
                except ObjectDoesNotExist:
                    loop = False
            # Asks the user for the password
            loop = True
            while loop:
                password = getpass('Insert your password: ')
                if password == '':
                    self.stderr.write('The password cannot be empty.Try again.')
                    continue
                if len(password) < settings.PASSWORD_MINIMUM_LENGTH:
                    self.stderr.write('The password must be at least 6 characters long. Try again')
                    continue
                validate_password = getpass('Confirm your password: ')
                if validate_password != password:
                    self.stderr.write("The passwords didn't match. Try again")
                    continue
                loop = False
            # Adds the user to the database
            user = Users(username=username, password=make_password(password))
            user.save()
            self.stdout.write('User created')
        except KeyboardInterrupt:
            self.stderr.write('Operation aborted')
