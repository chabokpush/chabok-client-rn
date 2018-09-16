"use strict";

/**
 * This exposes the native AdpPushClient module as a JS module.
 */
import {NativeModules, platform} from "react-native";

const AdpNativeModule = NativeModules.AdpPushClient;

export const playServicesAvailability = AdpNativeModule.playServicesAvailability;

export class AdpPushClient {

  initializeApp = (appName, options, cbk) => {
    AdpNativeModule.initializeApp(appName, options, (response) => {
      cbk(response);
    });
  };

  register = (userId, channels) => {
    AdpNativeModule.register(userId, channels);
  };

  getAppId = async () => {
    return await AdpNativeModule.getAppId();
  };

  getClientVersion = async () => {
    return await AdpNativeModule.getClientVersion();
  };

  publish = async (channel, message) => {
    return await AdpNativeModule.publish(channel, message);
  };

  addTag = async (tag) => {
    return await AdpNativeModule.addTag(tag);
  };

  removeTag = async (tag) => {
    return await AdpNativeModule.removeTag(tag);
  };

  subscribe = async (channel) => {
    return await AdpNativeModule.subscribe(channel);
  };

  unsubscribe = async (channel) => {
    return await AdpNativeModule.unsubscribe(channel);
  };
}
