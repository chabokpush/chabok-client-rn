//
//  AdpPushClient.m
//  ChabokRNWrapper
//
//  Created by Hussein Habibi Juybari on 8/6/18.
//  Copyright © 2018 Facebook. All rights reserved.
//

#import <React/RCTLog.h>
#import "AdpPushClient.h"
#import <AdpPushClient/AdpPushClient.h>

@interface AdpPushClient()<PushClientManagerDelegate>

@property (nonatomic, strong) NSString *appId;

@end

@implementation AdpPushClient

RCT_EXPORT_MODULE()

#pragma mark - Initilaizer

RCT_EXPORT_METHOD(init:(NSString *) appId
                  apiKey:(NSString *) apiKey
                  username:(NSString *) username
                  password:(NSString *) password
                  devMode:(BOOL) devMode
                  promise:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    
    [PushClientManager setDevelopment:devMode];
    NSArray *appIds = [appId componentsSeparatedByString:@"/"];
    self.appId = appIds.firstObject;
    
    BOOL state = [PushClientManager.defaultManager registerApplication:self.appId
                                                                apiKey:apiKey
                                                              userName:username
                                                              password:password];
    
    if (state) {
        RCTLogInfo(@"Initilized sucessfully");
        resolve(@{@"result":@"Initilized sucessfully"});
    } else {
        RCTLogInfo(@"Could not init chabok parameters");
        NSError *error = [[NSError alloc] initWithDomain:NSLocalizedDescriptionKey
                                                    code:400
                                                userInfo:@{
                                                           @"result":@"Could not init chabok parameters"
                                                           }];
        reject(@"400",@"Could not init chabok parameters",error);
    }
    [PushClientManager.defaultManager addDelegate:self];
    [PushClientManager.defaultManager application:UIApplication.sharedApplication
                    didFinishLaunchingWithOptions:nil];
}

RCT_EXPORT_METHOD(initializeApp:(NSDictionary *) options
                  promise:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    
    if(options == nil || [options isEqual:[NSNull null]]){
        RCTLogInfo(@"Option parameter is null");
        NSError *error = [[NSError alloc] initWithDomain:NSLocalizedDescriptionKey
                                                    code:400
                                                userInfo:@{
                                                           @"result":@"Could not init chabok parameters"
                                                           }];
        reject(@"400",@"Could not init chabok parameters",error);
    } else {
        NSString *appId = [options valueForKey:@"appId"];
        NSString *apiKey = [options valueForKey:@"apiKey"];
        NSString *username = [options valueForKey:@"username"];
        NSString *password = [options valueForKey:@"password"];
        BOOL devMode = [[options valueForKey:@"devMode"] boolValue];
        NSArray *appIds = [appId componentsSeparatedByString:@"/"];
        
        [self init:appIds.firstObject
            apiKey:apiKey
          username:username
          password:password
           devMode:devMode
           promise:resolve
          rejecter:reject];
        
    }
}

#pragma mark - Register methods

RCT_EXPORT_METHOD(registerAsGuest) {
    BOOL state = [PushClientManager.defaultManager registerAsGuest];
    if (state) {
        RCTLogInfo(@"Registered to chabok");
    } else {
        RCTLogInfo(@"Fail to registered to chabok");
    }
}

RCT_EXPORT_METHOD(register:(NSString *)userId) {
    if (userId && ![userId isEqual:[NSNull null]]){
        BOOL state = [PushClientManager.defaultManager registerUser:userId];
        if (state) {
            RCTLogInfo(@"Registered to chabok");
        } else {
            RCTLogInfo(@"Fail to registered to chabok");
        }
    } else {
        RCTLogInfo(@"Could not register userId to chabok");
    }
}

RCT_EXPORT_METHOD(register:(NSString *)userId channel:(NSString *) channel) {
    if (userId && ![userId isEqual:[NSNull null]]){
        
        BOOL state = [PushClientManager.defaultManager registerUser:userId
                                                           channels:@[channel]];
        if (state) {
            RCTLogInfo(@"Registered to chabok with channel");
        } else {
            RCTLogInfo(@"Fail to registered to chabok");
        }
    } else {
        RCTLogInfo(@"Could not register userId to chabok with channel");
    }
}

RCT_EXPORT_METHOD(register:(NSString *)userId channels:(NSArray *) channels) {
    if (userId && ![userId isEqual:[NSNull null]]){
        
        NSArray *chnl = @[];
        if (![channels isEqual:[NSNull null]] && channels) {
            chnl = channels;
        }
        BOOL state = [PushClientManager.defaultManager registerUser:userId
                                                           channels:chnl registrationHandler:^(BOOL isRegistered, NSString *userId, NSError *error) {
                                                               RCTLogInfo(@"isRegistered : %d userId : %@ error : %@",isRegistered, userId, error );
                                                               if (error) {
                                                                   [self sendEventWithName:@"onRegister" body:@{@"error":error,
                                                                                                                @"isRegister":@(NO)
                                                                                                                }];
                                                               } else {
                                                                   [self sendEventWithName:@"onRegister" body:@{@"isRegister":@(isRegistered)}];
                                                               }
                                                           }];
        if (state) {
            RCTLogInfo(@"Registered to chabok with channels");
        } else {
            RCTLogInfo(@"Fail to registered to chabok");
        }
    } else {
        RCTLogInfo(@"Could not register userId to chabok with channels");
    }
}

RCT_EXPORT_METHOD(unregister) {
    [PushClientManager.defaultManager unregisterUser];
}

RCT_EXPORT_METHOD(getInstallationId:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    NSString *installationId = [PushClientManager.defaultManager getInstallationId];
    if (!installationId) {
        NSError *error = [NSError.alloc initWithDomain:@"Not registered"
                                                  code:500
                                              userInfo:@{
                                                         @"message":@"The installationId is null, You didn't register yet!"
                                                         }];
        reject(@"500",@"The installationId is null, You didn't register yet!",error);
    } else {
        resolve(installationId);
    }
}

RCT_EXPORT_METHOD(getUserId:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    NSString *userId = [PushClientManager.defaultManager userId];
    if (!userId) {
        NSError *error = [NSError.alloc initWithDomain:@"Not registered"
                                                  code:500
                                              userInfo:@{
                                                         @"message":@"The userId is null, You didn't register yet!"
                                                         }];
        reject(@"500",@"The userId is null, You didn't register yet!",error);
    } else {
        resolve(userId);
    }
}

#pragma mark - dev

RCT_EXPORT_METHOD(setDevelopment:(BOOL) devMode) {
    [PushClientManager setDevelopment:devMode];
}

#pragma mark - tags

RCT_EXPORT_METHOD(addTag:(NSString *) tagName resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [PushClientManager.defaultManager addTag:tagName
                                     success:^(NSInteger count) {
                                         resolve(@{@"count":@(count)});
                                     } failure:^(NSError *error) {
                                         NSString *errorCode = [NSString stringWithFormat:@"%zd",error.code];
                                         reject(errorCode,error.domain,error);
                                     }];
}
RCT_EXPORT_METHOD(addTags:(NSArray *) tagsName resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [PushClientManager.defaultManager addTags:tagsName
                                      success:^(NSInteger count) {
                                          resolve(@{@"count":@(count)});
                                      } failure:^(NSError *error) {
                                          NSString *errorCode = [NSString stringWithFormat:@"%zd",error.code];
                                          reject(errorCode,error.domain,error);
                                      }];
}
RCT_EXPORT_METHOD(removeTag:(NSString *) tagName resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [PushClientManager.defaultManager removeTag:tagName
                                        success:^(NSInteger count) {
                                            resolve(@{@"count":@(count)});
                                        } failure:^(NSError *error) {
                                            NSString *errorCode = [NSString stringWithFormat:@"%zd",error.code];
                                            reject(errorCode,error.domain,error);
                                        }];
}
RCT_EXPORT_METHOD(removeTags:(NSArray *) tagsName resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [PushClientManager.defaultManager removeTags:tagsName
                                         success:^(NSInteger count) {
                                             resolve(@{@"count":@(count)});
                                         } failure:^(NSError *error) {
                                             NSString *errorCode = [NSString stringWithFormat:@"%zd",error.code];
                                             reject(errorCode,error.domain,error);
                                         }];
}

#pragma mark - publish

//RCT_EXPORT_METHOD(publish:(NSString *) channel text:(NSString *) text resolver:(RCTPromiseResolveBlock)resolve
//                  rejecter:(RCTPromiseRejectBlock)reject) {
//  BOOL publishState = [PushClientManager.defaultManager publish:channel withText:text];
//  resolve(@[@{@"published":@(publishState)}]);
//}
//
//RCT_EXPORT_METHOD(publish:(NSString *) userId channel:(NSString *) channel text:(NSString *) text resolver:(RCTPromiseResolveBlock)resolve
//                  rejecter:(RCTPromiseRejectBlock)reject) {
//  BOOL publishState = [PushClientManager.defaultManager publish:userId toChannel:channel withText:text];
//  resolve(@[@{@"published":@(publishState)}]);
//}

RCT_EXPORT_METHOD(publish:(NSDictionary *) message resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *data = [message valueForKey:@"data"];
    NSString *userId = [message valueForKey:@"userId"];
    NSString *content = [message valueForKey:@"content"];
    NSString *channel = [message valueForKey:@"channel"];
    
    PushClientMessage *chabokMessage;
    if (data) {
        chabokMessage = [[PushClientMessage alloc] initWithMessage:content withData:data toUserId:userId channel:channel];
    } else {
        chabokMessage = [[PushClientMessage alloc] initWithMessage:content toUserId:userId channel:channel];
    }
    
    BOOL publishState = [PushClientManager.defaultManager publish:chabokMessage];
    resolve(@{@"published":@(publishState)});
}

#pragma mark - publish event

RCT_EXPORT_METHOD(publishEvent:(NSString *) eventName data:(NSDictionary *) data resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [PushClientManager.defaultManager publishEvent:eventName data:data];
}

#pragma mark - subscribe
RCT_EXPORT_METHOD(subscribe:(NSString *) channel) {
    [PushClientManager.defaultManager subscribe:channel];
}

RCT_EXPORT_METHOD(subscribeEvent:(NSString *) eventName) {
    [PushClientManager.defaultManager subscribeEvent:eventName];
}

RCT_EXPORT_METHOD(subscribeEvent:(NSString *) eventName installationId:(NSString *) installationId) {
    if (!installationId){
        [PushClientManager.defaultManager subscribeEvent:eventName];
    } else {
        [PushClientManager.defaultManager subscribeEvent:eventName installationId:installationId];
    }
}

#pragma mark - unsubscribe
RCT_EXPORT_METHOD(unSubscribe:(NSString *) channel) {
    [PushClientManager.defaultManager unsubscribe:channel];
}

RCT_EXPORT_METHOD(unSubscribeEvent:(NSString *) eventName) {
    [PushClientManager.defaultManager unsubscribeEvent:eventName];
}

RCT_EXPORT_METHOD(unSubscribeEvent:(NSString *) eventName installationId:(NSString *) installationId) {
    if (!installationId){
        [PushClientManager.defaultManager unsubscribeEvent:eventName];
    } else {
        [PushClientManager.defaultManager unsubscribeEvent:eventName installationId:installationId];
    }
}

#pragma mark - badge
RCT_EXPORT_METHOD(resetBadge) {
    [PushClientManager resetBadge];
}

#pragma mark - track
RCT_EXPORT_METHOD(track:(NSString *) trackName data:(NSDictionary *) data) {
    [PushClientManager.defaultManager track:trackName data:data];
}

#pragma mark - default tracker
RCT_EXPORT_METHOD(setDefaultTracker:(NSString *) defaultTracker) {
    [PushClientManager.defaultManager setDefaultTracker:defaultTracker];;
}

#pragma mark - userInfo
RCT_EXPORT_METHOD(setUserInfo:(NSDictionary *) userInfo) {
    [PushClientManager.defaultManager setUserInfo:userInfo];;
}

RCT_EXPORT_METHOD(getUserInfo:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSDictionary *userInfo = PushClientManager.defaultManager.userInfo;
    resolve(userInfo);
}

#pragma mark - deeplink
RCT_EXPORT_METHOD(appWillOpenUrl:(NSURL *) url) {
    [PushClientManager.defaultManager appWillOpenUrl:url];
}

#pragma mark - chabok delegate methods
- (NSArray<NSString *> *)supportedEvents{
    return @[@"connectionStatus",@"onEvent",@"onMessage", @"ChabokMessageReceived", @"onSubscribe", @"onUnsubscribe", @"onRegister", @"notificationOpened"];
}

-(void) pushClientManagerDidReceivedMessage:(PushClientMessage *)message{
    if (self.bridge) {
        NSMutableDictionary *messageDict = [NSMutableDictionary.alloc initWithDictionary:[message toDict]];
        [messageDict setObject:message.channel forKey:@"channel"];
        
        [self sendEventWithName:@"onMessage" body:messageDict];
        [self sendEventWithName:@"ChabokMessageReceived" body:messageDict];
    }
}

-(void) pushClientManagerDidReceivedEventMessage:(EventMessage *)eventMessage{
    if (self.bridge) {
        NSDictionary *eventMessageDic =  @{
                                           @"id":eventMessage.id,
                                           @"installationId":eventMessage.deviceId,
                                           @"eventName":eventMessage.eventName
                                           };
        NSMutableDictionary *eventPayload = [NSMutableDictionary.alloc initWithDictionary:eventMessageDic];
        if (eventMessage.data) {
            [eventPayload setObject:eventMessage.data forKey:@"data"];
        }
        
        [self sendEventWithName:@"onEvent" body:[eventPayload copy]];
    }
}

-(void) pushClientManagerDidChangedServerConnectionState {
    NSString *connectionState = @"";
    if (PushClientManager.defaultManager.connectionState == PushClientServerConnectedState) {
        connectionState = @"CONNECTED";
    } else if (PushClientManager.defaultManager.connectionState == PushClientServerConnectingState ||
               PushClientManager.defaultManager.connectionState == PushClientServerConnectingStartState) {
        connectionState = @"CONNECTING";
    } else if (PushClientManager.defaultManager.connectionState == PushClientServerDisconnectedState ||
               PushClientManager.defaultManager.connectionState == PushClientServerDisconnectedErrorState) {
        connectionState = @"DISCONNECTED";
    } else  if (PushClientManager.defaultManager.connectionState == PushClientServerSocketTimeoutState) {
        connectionState = @"SocketTimeout";
    } else {
        connectionState = @"NOT_INITIALIZED";
    }
    
    [self sendEventWithName:@"connectionStatus" body:connectionState];
}

-(void) pushClientManagerDidSubscribed:(NSString *)channel{
    [self sendEventWithName:@"onSubscribe" body:@{@"name":channel}];
}
-(void) pushClientManagerDidFailInSubscribe:(NSError *)error{
    [self sendEventWithName:@"onSubscribe" body:@{@"error":error}];
}

-(void) pushClientManagerDidUnsubscribed:(NSString *)channel{
    [self sendEventWithName:@"onUnsubscribe" body:@{@"name":channel}];
}
-(void) pushClientManagerDidFailInUnsubscribe:(NSError *)error{
    [self sendEventWithName:@"onUnsubscribe" body:@{@"error":error}];
}

- (void)invalidate {
    self.appId = nil;
}

-(void) userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler{
    NSDictionary *payload = response.notification.request.content.userInfo;
    
    NSString *actionType;
    NSString *actionUrl;
    NSString *actionId = response.actionIdentifier;
    NSArray *actions = [payload valueForKey:@"actions"];
    NSString *clickUrl = [payload valueForKey:@"clickUrl"];
    
    if ([actionId containsString:UNNotificationDismissActionIdentifier]) {
        actionType = @"dismissed";
        actionId = nil;
    } else if ([actionId containsString:UNNotificationDefaultActionIdentifier]) {
        actionType = @"opened";
        actionId = nil;
    } else {
        actionType = @"action_taken";
        
        actionUrl = [self getActionUrlFrom:actionId actions:actions];
    }
    
    NSMutableDictionary *notificationData = [NSMutableDictionary new];
    [notificationData setObject:actionType forKey:@"actionType"];
    
    if (actionId) {
        [notificationData setObject:actionId forKey:@"actionId"];
    }
    
    if (clickUrl) {
        [notificationData setObject:clickUrl forKey:@"actionUrl"];
    } else if (actionUrl){
        [notificationData setObject:actionUrl forKey:@"actionUrl"];
    }
    
    [notificationData setObject:payload forKey:@"message"];
    
    [self sendEventWithName:@"notificationOpened" body:notificationData];
}

- (NSString *)getActionUrlFrom:(NSString *)actionId actions:(NSArray *)actions {
    NSString *actionUrl;
    for (NSDictionary *action in actions) {
        NSString *acId = [action valueForKey:@"id"];
        if ([acId containsString:actionId]) {
            actionUrl = [action valueForKey:@"url"];
        }
    }
    return actionUrl;
}

@end
