from flask import Flask
import os

app = Flask(__name__)

@app.route("/boot-node-py")
def bootnodepy():
    return "Indirect response from Python"

@app.route("/directpy")
def bootnodepydirect():
    return "Direct response from Python"

@app.route("/boot-py")
def bootpy():
    return "Response of Python from springBoot"


if __name__ == "__main__":
    port = int(os.environ.get("PORT", 5000))
    app.run(debug=True,host='0.0.0.0',port=port)