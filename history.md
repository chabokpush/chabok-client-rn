## History

### v1.3.0 (11/05/2019)

#### Changes:

- Update Chabok iOS SDK ([v1.19.0](https://github.com/chabokpush/chabok-client-ios/releases/tag/v1.19.0))

- Update Chabok android SDK ([v2.16.0](https://github.com/chabokpush/chabok-client-android/releases/tag/v2.16.0))

- Add `setUserInfo` method to send user information.
- Add `setDefaultTracker` for tracking pre-install campaigns.
- Add `appWillOpenUrl` method for sending attribution information in deeplinks.
- Add `notificationOpened` event for receiving click on notifications (actions and dismiss).
- `registerAsGuest` method added for applications with guest users, and for tracking installs on app launch (just like Adjust).

#### Upgrade:

* Android: 
- Support Google play `INSTALL_REFERRER`. Add the following dependency to your gradle.

``` groovy
    implementation 'com.android.installreferrer:installreferrer:1.0'
```

- To get notification action, implement the following code in `MainApplication` class in `onCreate` method:

```diff

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);
        
        if (chabok == null) {
            chabok = AdpPushClient.init(
                    getApplicationContext(),
                    MainActivity.class,
                    "APP_ID/SENDER_ID",
                    "API_KEY",
                    "USERNAME",
                    "PASSWORD"
            );

+            chabok.addNotificationHandler(new NotificationHandler(){
+                @Override
+                public boolean notificationOpened(ChabokNotification message, ChabokNotificationAction notificationAction) {
+                    ChabokReactPackage.notificationOpened(message, notificationAction);
+                   return super.notificationOpened(message, notificationAction);
+                }
+            });
        }
    }

```

* iOS:

- To get advertising id, add `AdSupport.framework` to  **Linked Frameworks and Libraries** of your target project
- Add `notificationOpened:` to send notification action event to React-Native 
- Add `registerToUNUserNotificationCenter` method to get notification actions by implementing the following code:
(To display rich notification, make sure to read our [documentation]())
```diff

+ @interface AppDelegate ()<PushClientManagerDelegate>

+ @end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
  {
    
+    [PushClientManager.defaultManager addDelegate:self];
+    [AdpPushClient registerToUNUserNotificationCenter];
  
    ...
    
    return true;
  }

+ -(void) userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler{
+     [AdpPushClient notificationOpened:response.notification.request.content.userInfo actionId:response.actionIdentifier];
+ }
  
+ -(void) application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler{
+     [AdpPushClient notificationOpened:userInfo];
+ }
  
+ -(void) application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo{
+    [AdpPushClient notificationOpened:userInfo];
+ }
  
+ -(void)application:(UIApplication *)application handleActionWithIdentifier:(NSString *)identifier forRemoteNotification:(NSDictionary *)userInfo completionHandler:(void (^)())completionHandler{
+     [AdpPushClient notificationOpened:userInfo actionId:identifier];
+ }

``` 

### v1.2.1 (06/03/2019)

#### Changes:

- Update Chabok iOS SDK ([v1.18.1](https://github.com/chabokpush/chabok-client-ios/releases/tag/v1.18.1))
- Update Chabok android SDK ([v2.14.2](https://github.com/chabokpush/chabok-client-android/releases/tag/v2.14.2))

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
- Add `onRegister` listener to check user registration status

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
