'use strict';

/**
 * This exposes the native AdpPushClient module as a JS module.
 */
import {NativeModules} from 'react-native';

const chabok = NativeModules.AdpPushClient;

export const playServicesAvailability = chabok.playServicesAvailability;

export class AdpPushClient {

    constructor() {
        console.log('constructor called');
    }

    initializeApp = (appName, options, cbk) => {
        chabok.initializeApp(appName, options , (response) => {
            cbk(response);
        });
    };

    register = (userId) => {
        console.log(userId);
        chabok.register(userId);
    };

    register = (userId, channels) => {
        console.log(userId);
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

    unSubscribe = async (channel) => {
        return await chabok.unSubscribe(channel);
    };
}

//export AdpPushClient;