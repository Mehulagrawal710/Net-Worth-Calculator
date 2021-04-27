import pickle



# db = {
# 	"mehul" : {
# 		"username" : "mehul",
# 		"password" : "710"
# 	}
# }

# with open("userdb.pickle", 'wb') as handle:
# 	pickle.dump(db, handle, protocol=pickle.HIGHEST_PROTOCOL)


db = {
	"mehul" : {
		"username" : "mehul",
		"assets": [
			{
				"name": "Car",
				"amount": 1000000
			},
			{
				"name": "Bike",
				"amount": 80000
			}
		],
		"liabilities": [
			{
				"name": "Debt",
				"amount": 400000
			}
		]
	}
}

with open("financedb.pickle", 'wb') as handle:
	pickle.dump(db, handle, protocol=pickle.HIGHEST_PROTOCOL)