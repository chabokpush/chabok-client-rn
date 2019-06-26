"use strict";

/**
 * This exposes the native AdpPushClient module as a JS module.
 */
import {NativeModules, Platform} from "react-native";

const AdpNativeModule = NativeModules.AdpPushClient;

export const playServicesAvailability = AdpNativeModule.playServicesAvailability;

export class AdpPushClient {

    initializeApp = (options) => {
        if (!options){
            return Promise.reject(new Error('Options parameters is null, Please provide Chabok credential keys'));
        }

        return this.init(options.appId, options.apiKey, options.username, options.password, options.devMode);
    };

    init = async (appId, apiKey, username, password, devMode) => {
        if (!appId) {
            return Promise.reject(new Error('appId parameters is required!'))
        }
        if (!apiKey) {
            return Promise.reject(new Error('apiKey parameter is required!'))
        }
        if (!username) {
            return Promise.reject(new Error('username parameter is required!'))
        }
        if (!password) {
            return Promise.reject(new Error('password parameter is required!'))
        }
        if (typeof(devMode) !== "boolean") {
            return Promise.reject(new Error('devMode parameter is required!'))
        }

        return await AdpNativeModule.init(appId, apiKey, username, password, devMode)
    }

    notificationOpenedHandler = () => AdpNativeModule.setNotificationOpenedHandler();

    register = (userId, channels) => AdpNativeModule.register(userId, channels);

    registerAsGuest = () => AdpNativeModule.registerAsGuest();

    appWillOpenUrl = (url) => AdpNativeModule.appWillOpenUrl(url);

    setDefaultTracker = (defaultTracker) => AdpNativeModule.setDefaultTracker(defaultTracker);

    /**
     * @deprecated the function has been replaced with AdpNativeModule.setUserAttributes()
     */
    setUserInfo = (userInfo) => AdpNativeModule.setUserAttributes(userInfo);

    /**
     * @deprecated the function has been replaced with AdpNativeModule.getUserAttributes()
     */
    getUserInfo = () => AdpNativeModule.getUserAttributes();


    setUserAttributes = (userAttributes) => AdpNativeModule.setUserAttributes(userAttributes);
    getUserAttributes = () => AdpNativeModule.getUserAttributes();

    incrementUserAttribute = (attribute, value) => {
        value = value || 1;
        AdpNativeModule.incrementUserAttribute(attribute, value);
    }

    setDeeplinkCallbackListener = (shouldLaunchDeeplink, deeplinkCallbackListener) => {
        if (deeplinkCallbackListener){
            AdpNativeModule.setDeeplinkCallbackListener(shouldLaunchDeeplink).then((deeplink) => {
                deeplinkCallbackListener(deeplink)
            });
        } else {
            throw new Error('deeplinkCallbackListener is invalid, please provide a callback');
        }
    }

    trackPurchase = (eventName, chabokEvent) => {
        AdpNativeModule.trackPurchase(eventName, chabokEvent);
    }

    setDefaultNotificationChannel = (channelName) => {
        setDefaultNotificationChannel(channelName);
    }

    unregister = () => AdpNativeModule.unregister();

    /*
    For publish in public channel set userId to "*".
        * payload.channel: String
        * payload.content: String
        * payload.data: Object
        * payload.userId: String(optional)
     */
    publish = async (payload) => {
        if (!payload) {
            return Promise.reject(new Error('payload is required'))
        }
        if (!payload.channel || typeof payload.channel !== 'string') {
            return Promise.reject(new Error('channel must be a string value!'))
        }
        if (!payload.content || typeof payload.content !== 'string') {
            return Promise.reject(new Error('content must be a string value!'))
        }
        if (payload.userId && typeof payload.userId !== 'string') {
            return Promise.reject(new Error('userId must be a string value!'))
        }
        if (payload.data && typeof payload.data !== 'object') {
            return Promise.reject(new Error('data must be an object!'))
        }

        return await AdpNativeModule.publish(payload);
    };

    publishEvent = async (eventName, data) => {
        if (!data) {
            return Promise.reject(new Error("data must be a object value"))
        }
        if (!eventName || typeof eventName !== 'string') {
            return Promise.reject(new Error("eventName must be a string value"))
        }

        return await AdpNativeModule.publishEvent(eventName, data)
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

    // setDevelopment = devMode => {
    //     AdpNativeModule.setDevelopment(devMode)
    // }


    track = (trackName, data) => {
        AdpNativeModule.track(trackName, data)
    }

    subscribe = async (channel) => {
        return await AdpNativeModule.subscribe(channel);
    };

    subscribeEvent = async (eventName, installationId) => {
        if (!eventName || typeof eventName !== 'string') {
            return Promise.reject(new Error("eventName must be a string value"))
        }

        return await AdpNativeModule.subscribeEvent(eventName, installationId);
    };

    unSubscribe = async (channel) => {
        return await AdpNativeModule.unSubscribe(channel);
    };

    unSubscribeEvent = async (eventName, installationId) => {
        if (!eventName || typeof eventName !== 'string') {
            return Promise.reject(new Error("eventName must be a string value"))
        }

        return await AdpNativeModule.unSubscribeEvent(eventName, installationId);
    };
}
