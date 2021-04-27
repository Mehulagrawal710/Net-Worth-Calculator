from flask import Flask, send_file, request,render_template, make_response, redirect
from werkzeug.utils import secure_filename
import os
import pickle
import dbconnect

app = Flask(__name__)
app.static_folder = 'static'
app.config['UPLOAD_FOLDER'] = "uploads"
app.config['MAX_CONTENT_LENGTH'] = 20 * 1024 * 1024  # 20 MB

userDB = dbconnect.UserDB()
financeDB = dbconnect.FinanceDB()

@app.route('/', methods=['GET', 'POST'])
def home():
	return render_template('index.html', baseurl = request.host_url, loginResponse = {}, signupResponse = {})

@app.route('/<username>', methods=['GET', 'POST'])
def getData(username):
	if request.method == 'GET':
		data = financeDB.get(username)
		return render_template('main.html', baseurl = request.host_url, username =  username, data = data)
	if request.method == 'POST':
		entryType = request.form.get("entryType")
		name = request.form.get("name")
		amount = int(request.form.get("amount"))
		entry = {
			entryType : {
				"name": name,
				"amount": amount
			}
		}
		financeDB.save(username, entry)
		return redirect(username)

@app.route('/signup', methods=['POST'])
def signup():
	username = request.form.get('username')
	password = request.form.get('password')
	if userDB.exists(username):
		return render_template('index.html', baseurl = request.host_url, loginResponse = {}, signupResponse = {"msg": "Username Already Exists", "color": "red"})
	user = {
		"username": username,
		"password": password
	}
	userDB.save(user)
	financeDB.initialize(username)
	return render_template('index.html', baseurl = request.host_url, loginResponse = {}, signupResponse = {"msg": "signup Successful, you can login now", "color": "green"})

@app.route('/login', methods=['POST'])
def login():
	username = request.form.get('username')
	password = request.form.get('password')
	if userDB.exists(username):
		user = userDB.get(username)
		if user['password']==password:
			return redirect(username)
		else:
			return render_template('index.html', baseurl = request.host_url, loginResponse = {"msg": "wrong password", "color": "red"}, signupResponse = {})
	return render_template('index.html', baseurl = request.host_url, loginResponse = {"msg": "username not found", "color": "red"}, signupResponse = {})

if __name__ == '__main__':
    app.run(debug=True, threaded=False)