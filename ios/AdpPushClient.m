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

@end

@implementation AdpPushClient

RCT_EXPORT_MODULE()

#pragma mark - Initilaizer

RCT_EXPORT_METHOD(init:(NSString *) appId
                  apiKey:(NSString *) apiKey
                  username:(NSString *) username
                  password:(NSString *) password
                  promise:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  self.appId = appId;
  [PushClientManager.defaultManager addDelegate:self];
  [PushClientManager.defaultManager application:UIApplication.sharedApplication
                  didFinishLaunchingWithOptions:nil];
  
  BOOL state = [PushClientManager.defaultManager registerApplication:appId
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
}

RCT_EXPORT_METHOD(initializeApp:(NSString *) appName options:(NSDictionary *) options cbk:(RCTResponseSenderBlock) cbk) {

  if(options == nil || [options isEqual:[NSNull null]]){
    RCTLogInfo(@"Option parameter is null");
    cbk(@[@{@"result":@"Option parameter is null"}]);
  } else {
    
    BOOL devMode = [[options valueForKey:@"isDev"] boolValue];
    
    [PushClientManager setDevelopment:devMode];
    PushClientManager.defaultManager.enableLog = YES;
    [PushClientManager.defaultManager addDelegate:self];
    
    NSString *appId = [options valueForKey:@"appId"];
    NSString *apiKey = [options valueForKey:@"apiKey"];
    NSString *username = [options valueForKey:@"username"];
    NSString *password = [options valueForKey:@"password"];
    
    NSArray *appIds = [appId componentsSeparatedByString:@"/"];
    [self init:appIds.firstObject
             apiKey:apiKey
           username:username
           password:password
       promise:^(id result) {
         cbk(result);
       } rejecter:^(NSString *code, NSString *message, NSError *error) {
         cbk(@[@{@"error":message}]);
       }];
  }
}

#pragma mark - Register methods

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
                                                      channels:chnl];
    if (state) {
      RCTLogInfo(@"@@@@@@@@@@@@@@@@@ Registered to chabok with channels");
    } else {
      RCTLogInfo(@"@@@@@@@@@@@@@@@@@ Fail to registered to chabok");
    }
  } else {
    RCTLogInfo(@"@@@@@@@@@@@@@@@@@ Could not register userId to chabok with channels");
  }
}

RCT_EXPORT_METHOD(unRegister) {
  [PushClientManager.defaultManager unregisterUser];
}

RCT_EXPORT_METHOD(getInstallationId:(RCTResponseSenderBlock)callback) {
  NSString *installationId = [PushClientManager.defaultManager getInstallationId];
  callback(@[installationId]);
}

RCT_EXPORT_METHOD(getUserId:(RCTResponseSenderBlock)callback) {
  NSString *userId = [PushClientManager.defaultManager userId];
  callback(@[userId]);
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
                                     resolve(@[@{@"count":@(count)}]);
                                   } failure:^(NSError *error) {
                                     NSString *errorCode = [NSString stringWithFormat:@"%zd",error.code];
                                     reject(errorCode,error.domain,error);
                                   }];
}
RCT_EXPORT_METHOD(addTags:(NSArray *) tagsName resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  [PushClientManager.defaultManager addTags:tagsName
                                   success:^(NSInteger count) {
                                     resolve(@[@{@"count":@(count)}]);
                                   } failure:^(NSError *error) {
                                     NSString *errorCode = [NSString stringWithFormat:@"%zd",error.code];
                                     reject(errorCode,error.domain,error);
                                   }];
}
RCT_EXPORT_METHOD(removeTag:(NSString *) tagName resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  [PushClientManager.defaultManager removeTag:tagName
                                   success:^(NSInteger count) {
                                     resolve(@[@{@"count":@(count)}]);
                                   } failure:^(NSError *error) {
                                     NSString *errorCode = [NSString stringWithFormat:@"%zd",error.code];
                                     reject(errorCode,error.domain,error);
                                   }];
}
RCT_EXPORT_METHOD(removeTags:(NSArray *) tagsName resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  [PushClientManager.defaultManager removeTags:tagsName
                                      success:^(NSInteger count) {
                                        resolve(@[@{@"count":@(count)}]);
                                      } failure:^(NSError *error) {
                                        NSString *errorCode = [NSString stringWithFormat:@"%zd",error.code];
                                        reject(errorCode,error.domain,error);
                                      }];
}

#pragma mark - publish

RCT_EXPORT_METHOD(publish:(NSString *) channel text:(NSString *) text resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  BOOL publishState = [PushClientManager.defaultManager publish:channel withText:text];
  resolve(@[@{@"published":@(publishState)}]);
}

//RCT_EXPORT_METHOD(publish:(NSString *) userId channel:(NSString *) channel text:(NSString *) text resolver:(RCTPromiseResolveBlock)resolve
//                  rejecter:(RCTPromiseRejectBlock)reject) {
//  BOOL publishState = [PushClientManager.defaultManager publish:userId toChannel:channel withText:text];
//  resolve(@[@{@"published":@(publishState)}]);
//}
//
//RCT_EXPORT_METHOD(publish:(NSDictionary *) message resolver:(RCTPromiseResolveBlock)resolve
//                  rejecter:(RCTPromiseRejectBlock)reject) {
//  PushClientMessage *chabokMessage = [[PushClientMessage alloc] initWithJson:message
//                                                               channel:[message valueForKey:@"channel"]];
//  BOOL publishState = [PushClientManager.defaultManager publish:chabokMessage];
//  resolve(@[@{@"published":@(publishState)}]);
//}

#pragma mark - subscribe
RCT_EXPORT_METHOD(subscribe:(NSString *) channel) {
  [PushClientManager.defaultManager subscribe:channel];
}

#pragma mark - unsubscribe
RCT_EXPORT_METHOD(unsubscribe:(NSString *) channel) {
  [PushClientManager.defaultManager unsubscribe:channel];
}

#pragma mark - chabok delegate methods
- (NSArray<NSString *> *)supportedEvents{
  return @[@"connectionStatus",@"ChabokMessageReceived"];
}

-(void) pushClientManagerDidReceivedMessage:(PushClientMessage *)message{
  [self sendEventWithName:@"ChabokMessageReceived" body:[message toDict]];
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
  } else {
    connectionState = @"NOT_INITIALIZED";
  }

  [self sendEventWithName:@"connectionStatus" body:connectionState];
}

@end
