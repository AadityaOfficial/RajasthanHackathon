#!/usr/bin/python
# -*- coding: utf-8 -*-
#loading the required dependencies
from flask import Flask, request, jsonify, send_file
import base64
import requests
import pytesseract
import json
import os
import keras
import numpy as np
from pyfcm import FCMNotification
import cv2
import io
from google.cloud import vision
from PIL import Image
import urllib
import time
from collections import OrderedDict
from operator import itemgetter
#api key for google cloud vision api
os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="/Users/aadityasuri/Desktop/apikey.json"

app = Flask(__name__)
APP_ROOT = os.path.dirname(os.path.abspath(__file__))

#defining the api end-point for image handling on server
@app.route('/')


@app.route('/asl',methods=['GET','POST'])
def aslfunct():
    if request.method=='GET':
        # urllib.urlretrieve("http://192.168.43.249:8000/getimage", "asl.png")
        # im=Image.open('asl.png')
        # result=im.rotate(180)
        # result.save('asl.png')
        cap = cv2.VideoCapture(0)
        while True:
            time.sleep(2)
            _, frame = cap.read()
            cv2.imwrite('asl.png', frame)
            break
        cap.release()
        base_url="https://api-us.faceplusplus.com/humanbodypp/beta/gesture"
        api_key="?api_key=lUPiZTzUOW7EQiv2r9j2DQ2-LljC5t2g"
        api_secret="&api_secret=ZcJ4CJs7M-Be7SMCBp061PMIKV0gF8c-"
        final_url=base_url+api_key+api_secret
        with open("asl.png","rb") as image_file:
            value=base64.b64encode(image_file.read())
        payload = {'image_base64': value}
        response = requests.post(final_url, data=payload)
        jsondata=json.loads(response.text)
        data = jsondata['hands'][0]['gesture']
        print data
        d = OrderedDict(sorted(data.items(), key=itemgetter(1) , reverse=True) )
        ans = d.keys()[0]
        print(ans)
        return jsonify({'answer':ans}) 
    
    elif request.method == 'POST':
        # Getting data in the form of JSON with specifies keys
        data=request.get_json(force=True)
        newFile=data['imageFile']
        correct_ans = data['correctAns']
        # the imageFile key in the JSON has the value which is base64 encoded form of image with required option 
        # 0 in the start indicated its a digit image, send the value to i
        # 1 in the start indicates its a character image, send the value to i
        # 2 in the start indicates its object in the image, send the value to i
       
        # Decoding the base64 string ie the image
        imgdata = base64.b64decode(newFile)
        
        # Saving the retrieved image in root directory
        filename = 'asl.png'
        with open(filename, 'wb') as f:
            f.write(imgdata)

        img = cv2.imread('asl.png')
        img = cv2.pyrUp(img)
        img = cv2.pyrUp(img)
        print(img.shape)
        cv2.imwrite('asl.png', img)

        base_url="https://api-us.faceplusplus.com/humanbodypp/beta/gesture"
        api_key="?api_key=lUPiZTzUOW7EQiv2r9j2DQ2-LljC5t2g"
        api_secret="&api_secret=ZcJ4CJs7M-Be7SMCBp061PMIKV0gF8c-"
        final_url=base_url+api_key+api_secret
        with open("asl.png","rb") as image_file:
            value=base64.b64encode(image_file.read())
        payload = {'image_base64': value}
        response = requests.post(final_url, data=payload)
        print response.text
        jsondata=json.loads(response.text)
        data = jsondata['hands'][0]['gesture']
        print data
        d = OrderedDict(sorted(data.items(), key=itemgetter(1) , reverse=True) )
        ans = d.keys()[0]
        print(ans)

        if ans == correct_ans:
            testNotif(77,'true')
        else:
            testNotif(77,'false')    
        
        return "{'status':'Success'}"


@app.route('/image', methods=['GET', 'POST'])
def imageFunction():
    # GET request to check if the end-point is working
    if request.method == 'GET':
        return "getting data"
    #POST request to recieve image from android app to server
    elif request.method == 'POST':
        # Getting data in the form of JSON with specifies keys
        data=request.get_json(force=True)
        newFile=data['imageFile']
        # the imageFile key in the JSON has the value which is base64 encoded form of image with required option 
        # 0 in the start indicated its a digit image, send the value to i
        # 1 in the start indicates its a character image, send the value to i
        # 2 in the start indicates its object in the image, send the value to i
        i = newFile[0]
        newFile = newFile[1:]
        # Decoding the base64 string ie the image
        imgdata = base64.b64decode(newFile)
        
        # Saving the retrieved image in root directory
        filename = 'some_image.png'
        with open(filename, 'wb') as f:
            f.write(imgdata)
        # Retrieving image from root directory
        img = cv2.imread('some_image.png', 0)
        print(img.shape)
        # if i==0 we call the detection of digit function
        if i == '0':
           val = detect_images(img)
        #if i==1 we call detection of character function
        elif i == '1':
            val = detect_char(img)
        #if i==2 or anything else it will call detection of object function
        else:
            correct_ans = data['correctAns']
            val = detect_object(img, correct_ans)
                    
        testNotif(i, val)
        return "{'status':'Success'}"

# This function is used to detect the digit from an image
def detect_images(img):
    #Applying bitwise not operation on image
    img = cv2.bitwise_not(img)
     

    #Scale down the image to the required size   
    for j in range(5):
        img = cv2.pyrDown(img)
      
    img = cv2.resize(img, (28,28))
    
    #Loading the trained model
    model = keras.models.load_model('/Users/aadityasuri/Documents/Vihaan/Vidya-App/models/MNIST_model.h5')
    
    #Predicting the result for the inout image
    pred = model.predict_proba(img.reshape(1,28,28,1))
    val=np.argmax(pred)
    print(val)
    return val

# This function is used to detect the character from an image
def detect_char(img):
    #Applying bitwise not operation on image
    img = cv2.bitwise_not(img)
        
    #Scale down the image to the required size  
    for j in range(5):
        img = cv2.pyrDown(img)

    #Setting up the parameters for tesseract
    kernel = np.ones((1, 1), np.uint8)
    img = cv2.dilate(img, kernel, iterations=1)
    img = cv2.erode(img, kernel, iterations=1)
    tessdata_dir_config = '--tessdata-dir "/usr/local/Cellar/tesseract/3.05.01/share/tessdata/" --psm 10  --oem 2 '
    
    #Applying tesseract on the input image to detect characters 
    arr = Image.fromarray(img)
    result = pytesseract.image_to_string(arr, config = tessdata_dir_config)
    
    print(result)
    return result

# This function detect an object in image
def detect_object(img, correct_ans):
    #Making an object of Google Vision Client
    vision_client = vision.Client()
    file_name = 'some_image.png'

    #Loading the image
    with io.open(file_name, 'rb') as image_file:
        content = image_file.read()
    image = vision_client.image(content=content, )

    #Detecting the labels in image
    labels = image.detect_labels()
    for label in labels:
        print(label.description, label.score)

    for label in labels:
        #if any label description is equal to the correct ans, return true else return false
        if str(label.description) == correct_ans:
            print("true : ",label.description)
            return "true"
    
    return "false"

def testNotif(i, val):
    #Sends notification to the app with calculated result
    push_service = FCMNotification(api_key="AAAAupVl040:APA91bHF4i3_t-8vnZgES2pNDHHtx7EDGGQ_t6lRWMc5QA3ehSVkJgGJIHXVgtr1ZbkBGpmXAaWRsfwCkT-pWX0PzXDppYrMiXCAQ1dRVNq6Cv0Uiw_j8HfkDYhGQIEmxOElfJ0DdtDm")
    registration_id = "c-IXwk9n6R0:APA91bHwu-_XdN-W9vmNvMJXQkdunQKce-Sxw9Fsp537k8hrh8id8r8tKd7nH5r7XoCuzxqE0NUesfDOXc2fW4UNXOA8l5mcQo00RurSw1EfYB23bScV9cEby-nFu4MLO2jvA0OTxTmr"

    data_message = {
        "ans":val, 
        "type":i
        }
    result = push_service.notify_single_device(registration_id=registration_id, data_message=data_message)
    print(result)
    return "success"

if __name__ == '__main__':
    app.debug = False
    app.run(host='0.0.0.0', port=8079)  

