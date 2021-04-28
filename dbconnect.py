from tinydb import TinyDB, Query

database_dir = "database/"

class UserDB():
	"""
	{
		"username": "mehul",
		"password": "710",
		"email": "mehulagrawal710@gmail.com"
	}
	"""
	def __init__(self):
		self.userDB = TinyDB(database_dir+"userdb.json")

	def exists(self, username):
		result = self.userDB.search(Query().username==username)
		if result:
			return True
		return False

	def save(self, user_object):
		self.userDB.insert(user_object)
		# self.userDB.purge()
		print("===> Successfuly saved user: {}".format(user_object["username"]))

	def get(self, username):
		print("===> Fetching user object for username:".format(username))
		result = self.userDB.search(Query().username==username)
		return result[0]

class AccountDB():
	"""
	{
		"username": "mehul",
		"title": "house",
		"category": "asset",
		"amount": 1
	}
	"""
	def __init__(self):
		self.accountDB = TinyDB(database_dir+"accountdb.json")

	def save(self, entry_object):
		self.accountDB.insert(entry_object)
		# self.accountDB.purge()
		print("===> Successfuly saved Entry for user: {}".format(entry_object["username"]))

	def get(self, username):
		print("===> Fetching entry object for username:".format(username))
		result = self.accountDB.search(Query().username==username)
		return result