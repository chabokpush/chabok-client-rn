"use strict";

/**
 * This exposes the native AdpPushClient module as a JS module.
 */
import {NativeModules, platform} from "react-native";

const AdpNativeModule = NativeModules.AdpPushClient;

export const playServicesAvailability = AdpNativeModule.playServicesAvailability;

export class AdpPushClient {

  initializeApp = (appName, options, cbk) => {
    AdpNativeModule.initializeApp(appName, options, response => {
      cbk(response);
    });
  };


  init = async (appId, apiKey, username, password) => {
    if (!appId || !apiKey || !username || !password) {
      return Promise.reject(new Error('all parameters are required!'))
    }

    return await AdpNativeModule.init(appId, apiKey, username, password)
  }

  register = (userId, channels) => {
    AdpNativeModule.register(userId, channels);
  };



  publish = async (channel, message) => {
    return await AdpNativeModule.publish(channel, message);
  };

  addTag = async (tag) => {
    return await AdpNativeModule.addTag(tag);
  };

  addTags = async (...tag) => {
    return await AdpNativeModule.addTags(tag);
  };

  removeTag = async (tag) => {
    return await AdpNativeModule.removeTag(tag);
  };

  removeTags = async (...tag) => {
    return await AdpNativeModule.removeTags(tag);
  };

  getInstallationId = async () => {
    return await AdpNativeModule.getInstallationId()
  }

  getUserId = async () => {
    return await AdpNativeModule.getUserId()
  }

  resetBadge = () => {
    AdpNativeModule.resetBadge()
  }

  setDevelopment = devMode => {
    AdpNativeModule.setDevelopment(devMode)
  }


  track = (trackName, data) => {
    AdpNativeModule.track(trackName, data)
  }

  subscribe = async (channel) => {
    return await AdpNativeModule.subscribe(channel);
  };

  unsubscribe = async (channel) => {
    return await AdpNativeModule.unsubscribe(channel);
  };
}
