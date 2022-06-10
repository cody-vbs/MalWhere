# -*- coding: utf-8 -*-
"""
Created on Tue May 10 09:31:43 2022

@author: Leah J
"""

#training python file main train sa new train folder with 39 features
# =============================================================================
# 
# user_input2 = ['https://quillbot.com/',
#               'https://translate.google.com/',
#               'http://alto18.co.uk/wave/wave.asp?o=44345',
#               'https://faacebok.zapto.org/',
#               'https://www.unionbank-ph.com/contact-us/write-to-us',
#               'https://www.unionbankph.com/contact-us/write-to-us',
#               'http://pastehtml.com/view/bjn144v0e.html',
#               'http://pastehtml-wer.com/view'
#               ]
# =============================================================================
#4_2S
#ACTUAL: [0,0,1,1,1,0,1,1]
#RESULT: [0 1 1 1 0 0 1 1]
#Score 5/8

#4
#ACTUAL: [0,0,1,1,1,0,1,1]
#RESULT: [1 1 1 1 1 1 1 1]
#Score 5/8


# =============================================================================
# user_input = [
# #B
#                 'https://www.statology.org/length-of-values-does-not-match-length-of-index/',
#                 'https://www.youtube.com/watch?v=4jRBRDbJemM&ab_channel=StatQuestwithJoshStarmer',
#                 'https://xgboost.readthedocs.io/en/stable/tutorials/param_tuning.html',
#                 
# #M_4            
#                 'http://182.114.250.201:47461/Mozi.m',
#                 'http://www.hillview.com.cy/component/tpresto/frontpage.html',
#                 
#                 
# #B_4
#                 'https://eksisozluk.com/?q=y%c4%b1ld%c4%b1z+tilbe%27nin+ibo%27yu+sesiyle+ezmesi%2f%2351270399',
#                 'http://bestblackhatforum.com/Thread-BIG-CONGRATULATIONS-TO-JOHNNY-SHADOW-Aka-Mr-20k-Reps-we-all-thank-you?page=3',
#                 'http://squarespace.com/press/2015/1/28/squarespace-grows-up-and-so-does-its-super-bowl-ad',
#                 
# #M2_S               
#                 'https://spdtextile.com/sport/39l2.exe',
#                 'https://pastebin.com/raw/eGjTb6Qm',
#                 
#                 
# #B2_S
#                 'http://hubpages.com/hub/The-Fascinating-Truth-about-the-Worlds-Oldest-Pests-Cockroaches',
#                ' https://www.canva.com/templates/',
#                ' https://twitter.com/home?status=%E3%83%8C%E3%81%91%E3%82%8B%EF%BC%81%E3%80%90%E3%82%A2%E3%83%8B%E3%83%A1%E3%80%91+http%3A%2F%2Fero-video.net%2Ft%2F6fIwKeRTUu8iYzn8+Driven+by+Lust+%23ero+%23douga+%23agesage'
# 
# 
# 
#               ]
# =============================================================================

# =============================================================================
# 
# #user_input= "https://www.unionbankph.com/contact-us/write-to-us"
# #4_2_S
# #ACTUAL: [0,0,1,1,0,1,1,1,1]
# #RESULT: [0 1 1 1 0 0 1 0 1]
# #Score: 6/9
# m-6
# b-3
# TP = 3
# FP = 2
# TN = 2
# FN = 2
# 
# #4
# #ACTUAL: [0,0,1,1,0,1,1,1,1]
# #RESULT: [1 1 1 1 1 1 1 1 1]
# #Score: 6/9
# 
# TP = 6
# FP = 3
# TN = 0
# FN = 0
# 
# #4
# #ACTUAL: [0,0,1,1,0,1,1,1,1,1 1 1]
# #RESULT: [1 1 1 1 1 1 1 1 1 1 1 1]
# #Score: 9/12
# m-9
# b-3
# TP = 9
# FP = 1
# TN = 0
# FN = 0
# 
# #4_2_S 
# #ACTUAL: [0,0,1,1,0,1,1,1,1,1 1 1]
# #RESULT: [0 1 1 1 0 0 1 0 1 1 0 1
#        
# #Score: 7/9
# 
# TP = 6
# FP = 1
# TN = 2
# FN = 2
# 
# #4_2_S    PERO MAUNI ANG KUHAUN.......................
# #ACTUAL: [0,0,0,1,1,0,0,0,1,1, 0 0 0]
# #RESULT: [0 1 1 1 1 0 0 0 1 1 0 0 0]
#          [0 1 1 1 1 0 0 0 1 1 0 0 0]
# #Score: 11/13
# m-4
# b-9
# TP = 4
# FP = 2
# TN = 7
# FN = 0
# 
# #4                         NUMBER 2
# #ACTUAL: [0,0,0,1,1,0,0,0,1,1, 0 0 0]
# #RESULT: [1 1 1 1 1 0 0 0 1 1 0 1 0]
# #Score: 9/13
# 
# 
# TP = 4
# FP = 4
# TN = 5
# FN = 0
# 
# 
# #DATSET 1 with combined S  NUMBER 1
# #ACTUAL: [0,0,0,1,1,0,0,0,1,1, 0 0 0]
# #RESULT: [1 1 1 1 1 0 0 0 1 1 0 1 0]
# #Score: 9/13
# 
# 
# TP = 4
# FP = 4
# TN = 5
# FN = 0
# 
# 
# #DATSET 1 with iteration
# #ACTUAL: [0,0,0,1,1,0,0,0,1,1, 0 0 0]
# #RESULT: [1 0 1 1 1 1 1 0 0 0 0 0 0]
# #Score: 7/13
# 
# TP = 2
# FP = 4
# TN = 5
# FN = 2
# 
# #Last latest version 3.v.2
# #ACTUAL: [0,0,0,1,1,0,0,0,1,1, 0 0 0]
# #RESULT: [0 1 0 1 0 1 1 1 0 0 0 0 1]
# #Score: 5/13
# 
# 
# SUMMARY
# #4
# TP = 6 + 9 + 4 =19/19
# FP = 3 + 1 + 4 =8
# TN = 0 + 0 + 5 =5/18
# FN = 0 + 0 + 0 =0
# Accuracy 24/37
# 
# #4_2_S
# 
# TP = 3 + 6 + 4 =13/19
# FP = 2 + 1 + 2 =5
# TN = 2 + 2 + 7 =11/18
# FN = 2 + 2 + 0 =4
# Accuracy 24/37
# 
# 
# m-6 + 9 + 4 = 19
# b-3 + 3 + 9 =  18
# 
# m-9
# b-3
# 
# m-4
# b-9
# 
# 
# =============================================================================




import xgboost as xgb
#from xgboost import XGBClassifier

import io
import pandas as pd
from tld import get_tld
import re #regex
from urllib.parse import urlparse
import math #entropy
import whois #get hostname related info (domain,subdomain..etc)
import time #time.sleep
from datetime import datetime 


def Malwhere_predict4(user_input):
       
   #Load saved Model
   model4 = xgb.XGBClassifier(n_estimators= 100)
#Dataset used is dataset1 combined not iterate with special
   model4.load_model("malwhere_modelv.4_2_S-21010k.json")

  #get the URL
#   user_input_type = type(user_input)   
#   if user_input_type == str():
   user_input = io.StringIO(user_input)
   df = pd.DataFrame(user_input, columns=['url']) 
# =============================================================================
#    else:
#         
#         df = pd.DataFrame(user_input, columns=['url']) 
#         
# =============================================================================

   #Lexical feature
    #F1 url length
   df['url_length'] = df['url'].apply(lambda i: len(str(i)))
    #F2:use IP or not
   df['use_of_ip'] = df['url'].apply(lambda i: having_ip_address(i))
    #F3:length of path
   df['path_length'] = df['url'].apply(lambda i: path_length(i))   
    #F4:pathURLRatio
   df['path_to_urllength_ratio'] =  df['path_length']/df['url_length']
    #F5:count number of directory 
   df['count_dir'] = df['url'].apply(lambda i: no_of_dir(i))
    #F6: count the length of the first directory
   df['fd_length'] = df['url'].apply(lambda i: fd_length(i))    
    #F7:count attached URL in path
   df['count_embed_domian'] = df['url'].apply(lambda i: no_of_embed(i))
    #F8:have short URl or not
   df['short_url'] = df['url'].apply(lambda i: shortening_service(i))
    #NOTE: Percentage = (number you want to find the percentage for ÷ total) × 100
    #F9: count lowercase
   df['count-lowercase']= df['url'].apply(lambda i: count_lowercase(i)) #high corr
    #F10: get lower to url length ratio
   df['lower_to_urllength_ratio'] = df['count-lowercase']/df['url_length']
    #F11: count upper letters
   df['count_uppercase'] =  df['url'].apply(lambda i: count_uppercase(i)) #high corr
    #F12:get upper to url length ratio
   df['upper_to_urllength_ratio'] = df['count_uppercase']/df['url_length']
    #F13:count digits
   df['count-digits']= df['url'].apply(lambda i: digit_count(i))
    #F14: digit to url length ratio
   df['digit_to_urllength_ratio'] = df['count-digits']/df['url_length']
    #F15: count letters
   df['count-letters']= df['url'].apply(lambda i: letter_count(i))
    #F16: count letter to url length ratio
   df['letters_to_urllength_ratio'] = df['count-letters']/df['url_length']
    #F17: count special characters 
   df['count-specchar']= df['url'].apply(lambda i: spechar_count(i))
    #F18: count special characters to url ratio
   df['specchar_to_urllength_ratio'] = df['count-specchar']/df['url_length']
    #count 19,20,21,22,23,24,28,26,27,28
   df['count-www'] = df['url'].apply(lambda i: i.count('www'))
   df['count.'] = df['url'].apply(lambda i: i.count('.'))
   df['count@'] = df['url'].apply(lambda i: i.count('@'))
   df['count%'] = df['url'].apply(lambda i: i.count('%'))
   df['count?'] = df['url'].apply(lambda i: i.count('?'))
   df['count-'] = df['url'].apply(lambda i: i.count('-'))
   df['count='] = df['url'].apply(lambda i: i.count('='))
   df['count#'] = df['url'].apply(lambda i: i.count('#')) #count fragment
   df['count;'] = df['url'].apply(lambda i: i.count(';'))
   df['count_'] = df['url'].apply(lambda i: i.count('_'))
    #    df['count/'] = df['url'].apply(lambda i: i.count('/')) #count no dir
   df['count&'] = df['url'].apply(lambda i: i.count('&')) #count param
    #F29: count search parameters
   df['param_count'] = df['url'].apply(lambda i: param_count(i))
    #Focus on hostname
    #F30: http or https or else
   df['http_or_https'] = df['url'].apply(lambda i: http_or_https(i))
    #F31: the higher the number the more random
   df['entropy'] = df['url'].apply(lambda i: entropy(i))
    #not a feature get TLD
   df['tld'] = df['url'].apply(lambda i: get_tld(i,fail_silently=True))
    #F32: get TLD length
   df['tld_length'] = df['tld'].apply(lambda i: len(str(i)))
    #F32:count domain suffixes or tld
   df['tld_count'] = df['url'].apply(lambda i: tld_count (i))
    #F33: get hostname length
   df['host_length'] = df['url'].apply(lambda i: hostname_len (i))
    #F34: get number of hyphen in host
   df['host_count-'] = df['url'].apply(lambda i: host_hyphen_count (i))
    #F35 get nymber of underscore in hostname
   df['host_count_'] = df['url'].apply(lambda i: host_under_count (i))
    #F36 get number of sudomain
   df['sub_domain_count'] = df['url'].apply(lambda i: subdomains_count (i))
    #F37: suspicious words if naa or wala
   df['sus_url'] = df['url'].apply(lambda i: suspicious_words(i))
    
    
   df = df.drop("tld",1)
    
    #HOST-BASED features
   df['days_since_exp'] = df['url'].apply(lambda i: daysSinceExpiration(i))
   df['days_since_reg'] = df['url'].apply(lambda i: daysSinceRegistration(i))
    
    #no page based feature hadlok mi sa malware
    #no blacklist feature kay wala mi budget
    
   X = df[[
        'url_length',                    #F3     #Rec.97_no.25
        'use_of_ip',                            #Rec.97_no.1
        'path_length',                          #Rec.97_no.17
        'path_to_urllength_ratio',              #Rec.97_no.18
        'count_dir',                            #Rec.97_no.5,count/=#Rec.97_no.12
        'fd_length',                            #Rec.97_no.28
        'count_embed_domian',                   #Rec.97_no.6
        'short_url',                     #F3    #Rec.97_no.7
        'count-lowercase',                      #Rec.97_no.20
       'lower_to_urllength_ratio',      #2FI 
        'count_uppercase',                      #Rec.97_no.19
        'upper_to_urllength_ratio',            #Rec.97_no.21
        'count-digits',                        #Rec.97_no.30
        'digit_to_urllength_ratio',       #F3   #Rec.97_no.22
     'count-letters',                  #2FI  #Rec.97_no.31
       'letters_to_urllength_ratio',     #2FI  #Rec.97_no.21
        'count-specchar',                #F3
        'specchar_to_urllength_ratio',   #F3   #Rec.97_no.23
        'count-www',                            #Rec.97_no.3
        'count.',                               #Rec.97_no.2
        'count@',                        #2FI   #Rec.97_no.4
        'count%',                        #2FI   #Rec.97_no.8
        'count?',                         #    #Rec.97_no.9
        'count-',                               #Rec.97_no.10
        'count=',                        #F3    #Rec.97_no.11
        'count#',                        #2FI   #Rec.97_no.13
        'count;',                        #F3    #Rec.97_no.15
        'count_',                               #Rec.97_no.16

#      'param_count',                    #2FI    count&=#Rec.97_no.14
        'http_or_https',                       #Rec.97_no.24
       'entropy',                         #2FI 
        'tld_length',                             #Rec.97_no.29
        'tld_count',                    #2FI   
        'host_length',                  #       #Rec.97_no.26
      'host_count-',                      #  
       'host_count_',                   #2FI 
        'sub_domain_count',             #
        'sus_url' ,                              #Rec.97_no.27
        
        
        #host-based
        'days_since_reg',
        'days_since_exp'
        
       ]]
   
   y_pred = model4.predict(X)
   prediction = str(y_pred)
   print (prediction)
   return prediction
    
#end make features

#GET FEATURES FUNCTIONS START (32)


#FOCUS on full URL
#-1 "expection error occured"
#Feature #1

#Use of IP or not in domain
def having_ip_address(url):
   #source regex: https://ihateregex.io/expr/ipv6/
   match2 = re.search ('(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))',url)     
   match = re.search ('(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.)',url)
   if match:
        # print match.group()
        return 1
   elif match2:
        return 1
   else:
        return 0
#TRY:
#print (having_ip_address('http://129555555/Mozi.m'))

#path length
def path_length(url):
    urlpath= urlparse(url).path
    try:
        return len(urlpath)
    except:
        return -1

#pip install googlesearch-python #maka search ka sa google ani
#count the number of directories
def no_of_dir(url):
    urldir = urlparse(url).path
    try:
        return urldir.count('/')
    except:
        return -1
    

#pip install tld
#Importing dependencies
#Directory Length
def fd_length(url):
    urlpath= urlparse(url).path
    try:
        return len(urlpath.split('/')[1]) #1 kay ang 0: / (forward slash)
    except:
        return -1

#count  attached url in the path
def no_of_embed(url):
    urldir = urlparse(url).path
    try:
        return urldir.count('//')
    except:
        return -1

#use shortener or not pwede pud sa embedd url
def shortening_service(url):
    match = re.search('bit\.ly|goo\.gl|shorte\.st|go2l\.ink|x\.co|ow\.ly|t\.co|tinyurl|tr\.im|is\.gd|cli\.gs|'
                      'yfrog\.com|migre\.me|ff\.im|tiny\.cc|url4\.eu|twit\.ac|su\.pr|twurl\.nl|snipurl\.com|'
                      'short\.to|BudURL\.com|ping\.fm|post\.ly|Just\.as|bkite\.com|snipr\.com|fic\.kr|loopt\.us|'
                      'doiop\.com|short\.ie|kl\.am|wp\.me|rubyurl\.com|om\.ly|to\.ly|bit\.do|t\.co|lnkd\.in|'
                      'db\.tt|qr\.ae|adf\.ly|goo\.gl|bitly\.com|cur\.lv|tinyurl\.com|ow\.ly|bit\.ly|ity\.im|'
                      'q\.gs|is\.gd|po\.st|bc\.vc|twitthis\.com|u\.to|j\.mp|buzurl\.com|cutt\.us|u\.bb|yourls\.org|'
                      'x\.co|prettylinkpro\.com|scrnch\.me|filoops\.info|vzturl\.com|qr\.net|1url\.com|tweez\.me|v\.gd|'
                      'tr\.im|link\.zip\.net',
                      url)
    if match:
        return 1
    else:
        return 0


#Feature  count_lowercase
def count_lowercase(url):
    try:
        lowercase = 0
        for i in url:
            if i.islower():
                lowercase= lowercase + 1
        return lowercase
    except:
        return -1


def count_uppercase(url):
    upper=sum(c.isupper() for c in url)
    try:
        return upper
    except:
        return -1


def digit_count(url):
    digits = 0
    try:
        for i in url:
            if i.isnumeric():
                digits = digits + 1
        return digits
    except:
        return -1



def letter_count(url):
    letters = 0
    try:
        for i in url:
            if i.isalpha():
                letters = letters + 1
        return letters
    except:
        return -1

#count special char
def spechar_count(url):
    specchar=sum(not c.isalnum() for c in url)
    try:
        return specchar
    except:
        return -1
#pila ka parameter gigamit for searching
def param_count(url):
    params = url.split('&')
    try:   
        return len(params) 
    except:
        return -1
    
#FOCUS On HOSTNAME

#uses http or https protocol
def http_or_https(url):
    urlprotocol = urlparse(url).scheme
    if urlprotocol == 'http':
        return 2
    elif urlprotocol == 'https':
        return 0
    else:
        return 1



   
   

#probability of disorderness or randomness
def entropy(url):
    string = url.strip()
    try:
        prob = [float(string.count(c)) / len(string) for c in dict.fromkeys(list(string))]
        entropy = sum([(p * math.log(p) / math.log(2.0)) for p in prob])
        return entropy
    except:
        return -1




#pip install tld

#tld and suffix is the same:refers to .com or ,edu
#get_tld and tldextract na function:same ra
#tld_daw =(get_tld("https://uve.usep.edu.ph/login/index.php") )
#count suffixes like .com
def tld_count(url):
    ext = tldextract.extract(url).suffix
    try:
        ext = ext.split('.')
        return len(ext)
    except:
        return -1

#netloc murag hostname
def hostname_len(url):
    hostname = urlparse(url).hostname
    try:
        return len(hostname)
    except:
        return -1
    
def host_hyphen_count(url):
    count_host_hyphen = urlparse(url).netloc
    try:
        return count_host_hyphen.count('-')
    except:
        return -1

def host_under_count(url):
    count_host_under = urlparse(url).netloc
    try:
        return count_host_under.count('_')
    except:
        return -1

#pip install tldextract
import tldextract
#subdomain has or not
def subdomains_count(url):
    subdomains = tldextract.extract(url).subdomain
    try:
        subdomains = subdomains.split('.')
        return len(subdomains)
    except:
        return -1


#suspicious words
def suspicious_words(url):
    match = re.search('PayPal|login|signin|bank|financial|account|update|free|winner|lucky|service|bonus|ebayisapi|webscr|porn|Anniversary|Promo',
                      url)
    if match:
        return 1
    else:
        return 0


# =============================================================================
# SGEG error
# #HOST BASED FEATURES
# import requests
# import sys
# def url_isalive(url):
#     # Making a get request 
#     try:
#         response = requests.get(url,timeout=5)
#         status = response.status_code
#         if status == 200:
#             return 0
#         else:
#             return 1
#     except Exception as e:
#         return -1
# 
# 
# =============================================================================

#domain age


def daysSinceRegistration(url):
    try:
        w=whois.whois(url)
        cr_date= w.creation_date.date()
        to_date=datetime.today().date()
#        print(cr_date)
#        print(to_date)
        if cr_date != ' ':
            url_age =to_date - cr_date
            url_age=str(url_age).split(' days')[0]
            url_age= int(url_age)
#            print(type(url_age))
            return url_age
        else:
            return 1
    except:
        return -1
    
    time.sleep(2)

def daysSinceExpiration(url):
    try:
        w=whois.whois(url)
        ex_date= w.expiration_date.date()
#        print(ex_date)
        to_date=datetime.today().date()
#        print( to_date)
        if ex_date != 'None':
            url_ex_age = ex_date - to_date 
            url_ex_age = str(url_ex_age).split(' days')[0]
            url_ex_age= int(url_ex_age)
#            print(type(url_ex_age))
            return url_ex_age
        else:
            return 1
    except:
        return -1

    time.sleep(3)


#END of feature get function


#prediction = print(Malwhere_predict4(user_input2))
