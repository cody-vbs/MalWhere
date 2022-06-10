# MalWhere
>DESIGN AND DEVELOPMENT OF A MOBILE-BASED MALICIOUS URL DETECTION APPLICATION

###### Year over year, communication tools such as Social media are highly targeted by cybercriminals. Their schemes include the distribution of malicious URLs. A malicious URL is a URL that facilitates scams, frauds, and a cyberattack. By clicking on a malicious URL, you can automatically download a malware or virus that can take control of your devices or trick you into disclosing sensitive information on a fake website. End users who do not have basic knowledge of information security are easier to be exploited by cybercriminals. One of the solutions for this kind of problem is using blocklist lookup; though effective, blocklists have a significant false negative rate and lack the ability to detect newly generated malicious URLs. To address this problem, the proponents developed a mobile application, called MalWhere, where it can detect website links from an image data. MalWhere also utilizes and combines the two known URL classification approaches; blocklisting and machine learning. With the blocklist service, MalWhere has access to a large number of Malicious URLs around the world. With machine learning, we are able to predict the classification of a URL, whether it is benign or malicious. Our predictive model was trained using 39 features and a supervise machine learning classifier called XGBoost. We mainly used the classification ability of our predictive model to the URLs that are unknown and benign to the blocklist service. The integration of the predictive model to the developed application resulted to an 87% accuracy rate in detecting the classification of a URL—whether it is benign or malicious. 


**Mobile App (Android Version 6-10)**

![alt text](https://github.com/cody-vbs/MalWhere/blob/master/screenshots/app_img1.jpg)

**Web Monitoring System**

![alt text](https://github.com/cody-vbs/MalWhere/blob/master/screenshots/web_img1.png)


You can download the mobile app through [here](https://drive.google.com/uc?export=download&id=1wL80FKJFEsG3-vFOgmfzLsw1FGJn7MZ0).


