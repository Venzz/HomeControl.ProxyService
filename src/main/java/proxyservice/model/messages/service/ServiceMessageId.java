package proxyservice.model.messages.service;

public enum ServiceMessageId {
    PushChannelSettings,
    PushNotification,
    PushChannelUri;

    public static ServiceMessageId create(byte value) {
        return ServiceMessageId.values()[value];
    }
}
