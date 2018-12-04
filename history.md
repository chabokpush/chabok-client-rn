## History

### v1.2.0 (2/12/2018)

#### Changes:

- Fix in changing Chabok environment.

#### Upgrade note:

- For change Chabok environments use `devMode` parameter in `init` method.
	`init(APP_ID, API_KEY, USERNAME, PASSWORD, DEVMODE)`
- Remove `appName` parameter in `initializeApp` method.
	`initializeApp(options)`
- The `setDevelopment` method is not available anymore. Use `devMode` parameter in `init` or `devMode` key in `initializeApp` method instead

### v1.1.1 (14/11/2018)
- Add `onSubscribe` and `onUnsubscribe` listener for getting subscribe and unsubscribe status.

### v1.1.0 (12/11/2018)
- Update Chabok android SDK version to [v2.14.0](https://github.com/chabokpush/chabok-client-android/releases/tag/v2.14.0)
- Update Chabok iOS SDK version to [v1.18.0](https://github.com/chabokpush/chabok-client-ios/releases/tag/v1.18.0)
- Fix promise reject for calling `getUserId` and `getInstallationId`

### v1.0.3 (10/11/2018)
- Update chabok android SDK version to [v2.13.3](https://github.com/chabokpush/chabok-client-android/releases/tag/v2.13.3)

### v1.0.2 (6/11/2018)
- Update android bridge compileSdkVersion to 26

### v1.0.1 (3/11/2018)
- Add `publishEvent` method.
- Add `onEvent` listener to receive `eventMessage`.
- Add `subscribeEvent`, `unSubscribeEvent` methods.
- Add `channel` key to message object.

### v1.0.0 (17/9/2018)
- Add `unregister` method.
- Add `resetBadge` method issue #11.
- Add `addTags` and `removeTags` method.
- Add a new initializer `init` method.
- Add `getUserId` and `getInstallationId` method.
- Add `track` method for tracking the user interactions.
- Add `setDevelopment` method to change the Chabok environments.
- Fix issues [#15](https://github.com/chabokpush/chabok-client-rn/issues/15) and [#6](https://github.com/chabokpush/chabok-client-rn/issues/6) thanks to @Mr-Hqq
- Fix crash iOS bridge when reloading the JS file.

#### Upgrade note:
- Change the signature of `unsubscribe` to `unSubscribe`.
- Change the signature of `publish` method it will gets an object with `{'content','userId','channel','data'}`.
