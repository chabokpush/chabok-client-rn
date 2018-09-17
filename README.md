![Logo](https://github.com/chabokpush/chabok-assets/blob/master/sdk-logo/RN-Bridge.svg)

# Chabok Push Client for React Native (Bridge)

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
    compileSdkVersion 26
    buildToolsVersion "26.0.2"
    ...
}
```

```groovy
dependencies {
    ...
    compile "com.google.android.gms:play-services-gcm:10.2.6"
    compile 'me.leolin:ShortcutBadger:1.1.22@aar'
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
                       "YOUR_APP_ID/SENDER_ID",
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

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    if ([PushClientManager.defaultManager application:application didFinishLaunchingWithOptions:launchOptions]) {
        NSLog(@"Application was launch by clicking on Notification...");
    }
    
    ...
   }

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


### Initialize
For [initlializing](https://github.com/chabokpush/chabok-starter-rn/blob/6794345acc1498b55cda8759b6e26550b21f9c6f/App.js#L36-L42) the ChabokPush with paramteres follow the bellow code:

```js

import { NativeEventEmitter, NativeModules } from 'react-native';
import chabok from 'react-native-chabok';

const options = {
  "appId": "APP_ID/GOOGLE_SENDER_ID",
  "apiKey": "API_KEY",
  "username": "USERNAME",
  "password": "PASSWORD"
};

this.chabok = new chabok.AdpPushClient();
this.chabok.init(options.appId, options.apiKey, options.username, options.password)
    .then((state) => {
        console.log(state);
        })
    .catch((error) => {
        console.log(error);
        });
```

### Change chabok environment
With using `setDevelopment` [method](https://github.com/chabokpush/chabok-starter-rn/blob/6794345acc1498b55cda8759b6e26550b21f9c6f/App.js#L34) can change the ChabokPush environment to sandbox or production :

```js
this.chabok.setDevelopment(devMode);
```

### Register user
To [register](https://github.com/chabokpush/chabok-starter-rn/blob/6794345acc1498b55cda8759b6e26550b21f9c6f/App.js#L78-L85) user in the ChabokPush service use `register` method:
```js
this.chabok.register('USER_ID');
```

### Get message
To getting the ChabokPush [message](https://github.com/chabokpush/chabok-starter-rn/blob/6794345acc1498b55cda8759b6e26550b21f9c6f/App.js#L70-L76) `addListener` on `ChabokMessageReceived` event:

```js
const chabokEmitter = new NativeEventEmitter(NativeModules.AdpPushClient);

chabokEmitter.addListener( 'ChabokMessageReceived',
    (msg) => {
        const messageJson = this.getMessages() + JSON.stringify(msg);
        this.setState({messageReceived: messageJson});
    });
```

### Get connection status
To get [connection state](https://github.com/chabokpush/chabok-starter-rn/blob/6794345acc1498b55cda8759b6e26550b21f9c6f/App.js#L44-L68) `addListener` on `connectionStatus` event :

```js
const chabokEmitter = new NativeEventEmitter(NativeModules.AdpPushClient);

chabokEmitter.addListener(
    'connectionStatus',
        (status) => {
            let connectionColor = 'red';
            let connectionState = 'error';

            if (status === 'CONNECTED') {
                connectionColor = 'green';
                connectionState = 'Connected';
            } else if (status === 'CONNECTING') {
                connectionColor = 'yellow';
                connectionState = 'Connecting';
            } else if (status === 'DISCONNECTED') {
                connectionColor = 'red';
                connectionState = 'Disconnected';
            }

            this.setState({
                connectionColor,
                connectionState
                    });
    });
```

### Publish message:

For [publishing](https://github.com/chabokpush/chabok-starter-rn/blob/6794345acc1498b55cda8759b6e26550b21f9c6f/App.js#L120-L125) a message use `publish` method:

```js
const msg = {
    channel: "default",
    userId: "USER_ID",
    content:'Hello world',
    data: OBJECT
        };
this.chabok.publish(msg)
```
