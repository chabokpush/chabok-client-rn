## Hitory

### v1.0.0 (17/9/2018)

- Add `unregister` method.
- Add `resetBadge` method issue #11.
- Add `addTags` and `removeTags` method.
- Add new initializer method `init` to library.
- Add `getUserId` and `getInstallationId` method.
- Add `track` method for tracking the user interactions.
- Add `setDevelopment` method to change the Chabok environments.
- Fix issues #15 and #6 thanks to @Mr-Hqq
- Fix crash iOS bridge when reloading the JS file.

#### Upgrade note:
- Change the signature of `unSubscribe` method.
- Change the signature of `publish` method it will get an object with `{'content','userId','channel','data'}`.
