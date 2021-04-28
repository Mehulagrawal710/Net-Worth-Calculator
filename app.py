from flask import Flask, send_file, request,render_template, make_response, redirect
from werkzeug.utils import secure_filename
import os
import dbconnect

app = Flask(__name__)
app.static_folder = 'static'
app.config['UPLOAD_FOLDER'] = "uploads"
app.config['MAX_CONTENT_LENGTH'] = 20 * 1024 * 1024  # 20 MB

userDB = dbconnect.UserDB()
accountDB = dbconnect.AccountDB()

@app.route('/', methods=['GET', 'POST'])
def home():
	return render_template('index.html', baseurl = request.host_url, loginResponse = {"msg": "Welcome", "class": "primary"}, signupResponse = {"msg": "Welcome", "class": "primary"}, formToOpen = "login")

@app.route('/<username>', methods=['GET', 'POST'])
def getData(username):
	if request.method == 'GET':
		data = accountDB.get(username)
		return render_template('main.html', baseurl = request.host_url, username =  username, data = data)
	if request.method == 'POST':
		category = request.form.get("category")
		title = request.form.get("title")
		amount = int(request.form.get("amount"))
		entry = {
			"username": username,
			"category": category,
			"title": title,
			"amount": amount
		}
		accountDB.save(entry)
		return redirect(username)

@app.route('/signup', methods=['POST'])
def signup():
	username = request.form.get('username')
	password = request.form.get('password')
	email = request.form.get('email')
	if userDB.exists(username):
		return render_template('index.html', baseurl = request.host_url, loginResponse = {"msg": "Welcome", "class": "primary"}, signupResponse = {"msg": "There is already a user registered with this username. Kindly choose another one", "class": "warning"}, formToOpen = "signup")
	user = {
		"username": username,
		"password": password,
		"email": email
	}
	userDB.save(user)
	return render_template('index.html', baseurl = request.host_url, loginResponse = {"msg": "Welcome", "class": "primary"}, signupResponse = {"msg": "signup Successful, you can login now", "class": "success"}, formToOpen = "signup")

@app.route('/login', methods=['POST'])
def login():
	username = request.form.get('username')
	password = request.form.get('password')
	if userDB.exists(username):
		user = userDB.get(username)
		if user["password"]==password:
			return redirect(username)
		else:
			return render_template('index.html', baseurl = request.host_url, loginResponse = {"msg": "wrong password", "class": "danger"}, signupResponse = {"msg": "Welcome", "class": "primary"}, formToOpen = "login")
	return render_template('index.html', baseurl = request.host_url, loginResponse = {"msg": "username not found", "class": "danger"}, signupResponse = {"msg": "Welcome", "class": "primary"}, formToOpen = "login")

if __name__ == '__main__':
    app.run(debug=True, threaded=False)