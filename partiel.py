from flask import Flask
app = Flask(__name__)
@app.route("/add/<int:a>/<int:b>")
def add(a, b):
    return str(int(a) + int(b))
@app.route("/mul/<int:a>/<int:b>")
def mult(a, b):
    return str(int(a) * int(b))
if __name__ == "__main__":
    app.run()
