# -*- coding: utf-8 -*-
"""
Created on Tue May 10 13:47:04 2022

@author: Dell
"""

#CREATE API
from flask import Flask
from flask import jsonify
from flask import request

app = Flask(__name__)


from Malwhere_model_predict_4 import Malwhere_predict4

#Create an instance of the class
app = Flask(__name__)

#https://malwhere.herokuapp.com/api/?url=
#@app.route('/<string:URL>/',  methods=['GET', 'POST'])
#URL = "http://127.0.0.1:5000/?url=https://malwhere.herokuapp.com/api/app.exe"

@app.route('/')
def Malwhere_api():
    URL = request.args.get('url')
    prediction= Malwhere_predict4(URL)
    print (prediction)
    return  jsonify({'prediction': prediction})
#WARNING return type must be string, dict, tuple, Response instance, or WSGI callable

if __name__ == '__main__':
    app.run(port=5000,debug=(True),threaded=(True))