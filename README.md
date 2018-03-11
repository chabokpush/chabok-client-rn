# Chabok Push Client for react-native
React native wrapper for chabok library.
This client library supports react-native to use chabok push library.
A Wrapper around native library to use chabok functionalities in react-native environment.

### installation
To install platform specific parts regarding library reference for android and ios parts, and installatin details refer to [docs](https://doc.chabokpush.com/react-native/setup.html).

to install npm package:
```
npm install react-native-chabok --save
```

### import
```
// import AdpPushClient wrapper
import * as chabok from 'react-native-chabok';
```


### Sample Usage
```
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
```
