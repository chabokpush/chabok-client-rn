# Chabok Push Client for react-native
React native wrapper for chabok library.
This client library supports react-native to use chabok push library.
A Wrapper around native library to use chabok functionalities in react-native environment.

## installation
For java-part, library refrence and library initialization that includes seting up: APP_ID, API_KEY, SDK_USERNAME,  SDK_PASSWORD and platform specific parts regarding library reference for ios, and installatin details refer to [docs](https://doc.chabokpush.com/react-native/setup.html).

to install npm package:

```yarn
npm install react-native-chabok --save
```
or
```bash
npm install react-native-chabok --save
```

## import

```javascript
import * as chabok from 'react-native-chabok';
```


## Basic Usage

```javascript
    const USER = "react_native_user_ID";
    var channels = ["ipl", "private/demo", "wall", "my_channel"];
    this.chabok = new chabok.AdpPushClient();

    // register to chabok service
    this.chabok.register(USER, channels);

    // subscribe to channel
    this.chabok.subscribe(channel).then(res => () => {
            console.log(res);
            alert('subscribe success');
        });
        
    // publish message
    chabok.publish(channel, msg)
        .then(res => console.log(res))
        .catch(error => console.log(error));
    
    // unsubscribe
    chabok.unSubscribe(channel)
        .then(res => () => {
                console.log(res);
            })
        .catch(error => console.log(error));
```
