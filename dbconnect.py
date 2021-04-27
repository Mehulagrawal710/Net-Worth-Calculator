import  pickle

class UserDB():
	"""
	User Object ->
	username = {
		username : "",
		password: ""
	}
	"""
	def __init__(self):
		self.db_name = "database/userdb.pickle"

	def exists(self, username):
		with open(self.db_name, 'rb') as handle:
			db = pickle.load(handle)
		if username in db.keys():
			return True
		return False

	def save(self, user_object):
		print("Saving user object :", user_object, "to database: ", self.db_name)
		with open(self.db_name, 'rb') as handle:
			db = pickle.load(handle)
			username = user_object["username"]
			db[username] = user_object
		with open(self.db_name, 'wb') as handle:
			pickle.dump(db, handle, protocol=pickle.HIGHEST_PROTOCOL)
		print("Saved user object :", user_object, "to database:", self.db_name, "successfuly")

	def get(self, username):
		print("Fetching user object for username:", username)
		with open(self.db_name, 'rb') as handle:
			db = pickle.load(handle)
		return db[username]

class FinanceDB():
	"""
	Finance Object ->
	username = {
		username : "",
		assets: [
			{
				name: ""
				amount: ""
			}
		],
		liabilities: [
			{
				name: ""
				amount: ""
			}
		]
	}
	"""
	def __init__(self):
		self.db_name = "database/financedb.pickle"

	def initialize(self, username):
		with open(self.db_name, 'rb') as handle:
			db = pickle.load(handle)
			data = {
				"username" : username,
				"assets": [],
				"liabilities": []
			}
			db[username] = data
		with open(self.db_name, 'wb') as handle:
			pickle.dump(db, handle, protocol=pickle.HIGHEST_PROTOCOL)

	def save(self, username, entry):
		print("Saving entry object :", entry, "to database: ", self.db_name)
		with open(self.db_name, 'rb') as handle:
			db = pickle.load(handle)
			entryType = list(entry.keys())[0]
			db[username][entryType].append(entry[entryType])
		with open(self.db_name, 'wb') as handle:
			pickle.dump(db, handle, protocol=pickle.HIGHEST_PROTOCOL)
		print("Saved the entry :", entry, "to database:", self.db_name, "successfuly")

	def get(self, username):
		print("Fetching finance object for username:", username)
		with open(self.db_name, 'rb') as handle:
			db = pickle.load(handle)
		if username in db.keys():
			return db[username]
		return None