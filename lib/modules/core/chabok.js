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
    chabok.initializeApp(appName, options, (response) => {
      cbk(response);
    });
  };

  register = (userId, channels) => {
    chabok.register(userId, channels);
  };

  getAppId = async () => {
    return await chabok.getAppId();
  };

  getClientVersion = async () => {
    return await chabok.getClientVersion();
  };

  publish = async (channel, message) => {
    return await chabok.publish(channel, message);
  };

  addTag = async (tag) => {
    return await chabok.addTag(tag);
  };

  removeTag = async (tag) => {
    return await chabok.removeTag(tag);
  };

  subscribe = async (channel) => {
    return await chabok.subscribe(channel);
  };

  unsubscribe = async (channel) => {
    return await chabok.unsubscribe(channel);
  };
}
