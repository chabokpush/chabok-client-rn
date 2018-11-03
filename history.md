## History

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
