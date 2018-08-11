"use strict";

/**
 * This exposes the native AdpPushClient module as a JS module.
 */
import {NativeModules, platform} from "react-native";

export const module = NativeModules.AdpPushClient;

export const playServicesAvailability = module.playServicesAvailability;

export class AdpPushClient {

  constructor() {
    console.log("constructor called");
  }

  initializeApp = (appName, options, cbk) => {
    module.initializeApp(appName, options, (response) => {
      cbk(response);
    });
  };

  register = (userId, channels) => {
    module.register(userId, channels);
  };

  getAppId = async () => {
    return await module.getAppId();
  };

  getClientVersion = async () => {
    return await module.getClientVersion();
  };

  publish = async (channel, message) => {
    return await module.publish(channel, message);
  };

  addTag = async (tag) => {
    return await module.addTag(tag);
  };

  removeTag = async (tag) => {
    return await module.removeTag(tag);
  };

  subscribe = async (channel) => {
    return await module.subscribe(channel);
  };

  unsubscribe = async (channel) => {
    return await module.unsubscribe(channel);
  };
}
