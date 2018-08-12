
# Chabok Push Client for react-native

[![NpmVersion](https://img.shields.io/npm/v/react-native-chabok.svg)](https://www.npmjs.com/package/react-native-chabok)
[![npm](https://img.shields.io/npm/dt/react-native-chabok.svg)](https://www.npmjs.com/package/react-native-chabok)

React native wrapper for chabok library.
This client library supports react-native to use chabok push library.
A Wrapper around native library to use chabok functionalities in react-native environment.

## installation
For java-part, library refrence and library initialization that includes seting up: `APP_ID`, `API_KEY`, `SDK_USERNAME`,  `SDK_PASSWORD` and platform specific parts regarding library reference for ios, and installatin details refer to [docs](https://doc.chabokpush.com/react-native-bridge/introducing.html).

To install packages:

```bash
yarn add react-native-chabok
```
or

```bash
npm install react-native-chabok --save
```

For linking `react-native-chabok`

```bash
react-native link react-native-chabok
```

## Getting Started Android

1. Update compileSdkVersion, buildToolsVersion, support library version
For the Android SDK, edit the `build.gradle` file in your `android/app` directory 

```groovy
android {
    compileSdkVersion 25
    buildToolsVersion "25.0.0"
    ...
}
```

```groovy
dependencies {
    ...
    compile "com.google.android.gms:play-services-gcm:10.2.6"
    compile 'me.leolin:ShortcutBadger:1.1.18@aar'
    compile 'com.adpdigital.push:chabok-lib:+'
    ...
}
```
2. Please update your `AndroidManifest.xml` according to following sample :

```xml
<manifest
    xmlns:android="http://schemas.android.com/apk/res/android"
    package="YOUR_APPLICATION_PACKAGE_ID">

    <permission
        android:name="YOUR_APPLICATION_PACKAGE_ID.permission.C2D_MESSAGE"
        android:protectionLevel="signature"/>

    <uses-permission android:name="YOUR_APPLICATION_PACKAGE_ID.permission.C2D_MESSAGE" />

    <application>
        
        <receiver android:name="PushMessageReceiver"> <!-- optional -->
                    <intent-filter>
                        <category android:name="YOUR_APPLICATION_PACKAGE_ID"/>
                        <action android:name="com.adpdigital.push.client.MSGRECEIVE"/>
                    </intent-filter>
        </receiver>
        
        <receiver
                android:name="com.google.android.gms.gcm.GcmReceiver"
                android:enabled="true"
                android:exported="true"
                android:permission="com.google.android.c2dm.permission.SEND">
            <intent-filter>
                <action android:name="com.google.android.c2dm.intent.RECEIVE"/>
                <action android:name="com.google.android.c2dm.intent.REGISTRATION"/>
                <category android:name="YOUR_APPLICATION_PACKAGE_ID"/>
            </intent-filter>
        </receiver>

...

    </application>

```

3. Initialize Chabok SDK in your `MainApplication.java`:

```java

public class YourAppClass extends Application {

private AdpPushClient chabok = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if (chabok == null) {
                   chabok = AdpPushClient.init(
                       getApplicationContext(),
                       MainActivity.class,
                       "YOUR_APP_ID",
                       "YOUR_API_KEY",
                       "SDK_USERNAME",
                       "SDK_PASSWORD"
                       );
               }
    }

    @Override
    public void onTerminate() {
        if (chabok != null)
            chabok.dismiss();

        super.onTerminate();
    }
}

```

## Getting started - iOS

1. The native iOS SDKs need to be setup using Cocoapods. In your project's `ios` directory, create a Podfile.
```bash
$ cd ios
$ pod init
```
2. Edit the Podfile to include `ChabokPush` as a dependency for your project, and then install the pod with `pod instal`.
```bash
use_frameworks!
platform :ios, '9.0'

target 'YOUR_TARGET_NAME' do

  # Pods for AwesomeProject
  pod 'ChabokPush'

end
```

3. Open the iOS project with .xcworkspace file in Xcode and also, open the `node_modules/react-native-chabok/` directory. Move the `ios/AdpPushClient.h` and `ios/AdpPushClient.m` files to your project.

4. Import inside `AppDelegate`:
```objectivec
#import <AdpPushClient/AdpPushClient.h>

...

- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error{
  // Hook and handle failure of get Device token from Apple APNS Server
  [PushClientManager.defaultManager application:application
didFailToRegisterForRemoteNotificationsWithError:error];
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken{
  // Manager hook and handle receive Device Token From APNS Server
  [PushClientManager.defaultManager application:application didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];
}

- (void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings{
  // Manager hook and Handle iOS 8 remote Notificaiton Settings
  [PushClientManager.defaultManager application:application didRegisterUserNotificationSettings:notificationSettings];
}

```


## Basic Usage
In your `App.js`:

```javascript

import { NativeEventEmitter, NativeModules } from 'react-native';
import chabok from 'react-native-chabok';

const USER = "react_native_user_ID";
var channels = ["sport", "private/news"];
this.chabok = new chabok.AdpPushClient();

this.chabok.initializeApp('APP_Name', options , (response) => {
  console.log('app initialized', response)
});


const chabokEmitter = new NativeEventEmitter(NativeModules.AdpPushClient);

chabokEmitter.addListener(
  'connectionStatus',
  (status) => {
    console.log('connectionStatus', status)
  }
);

chabokEmitter.addListener(
  'ChabokMessageReceived',
  (message) => {
    console.log("\nChabok Message Received :", message);
  }
);

// register to chabok service
this.chabok.register(USER, channels);

// subscribe to channel
this.chabok.subscribe(channel).then(res => () => {
        console.log(res);
        alert('subscribe success');
    });
    
// publish message
this.chabok.publish(channel, msg)
    .then(res => console.log(res))
    .catch(error => console.log(error));

// unsubscribe
this.chabok.unsubscribe(channel)
    .then(res => () => {
            console.log(res);
        })
    .catch(error => console.log(error));
```
