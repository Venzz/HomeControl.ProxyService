package proxyservice.model.cameraproxy;

import proxyservice.App;
import proxyservice.model.cameradata.CameraDataConsumer;
import proxyservice.model.cameradata.CameraDataConsumerEventListener;
import proxyservice.model.cameradata.CameraDataProvider;
import proxyservice.model.cameradata.CameraDataProviderEventListener;
import proxyservice.model.messages.Message;
import proxyservice.model.messages.service.PushChannelSettingsMessage;
import proxyservice.model.messages.service.PushChannelUriMessage;
import proxyservice.model.messages.service.PushNotificationContentMessage;
import proxyservice.model.messages.standard.StandardMessage;
import proxyservice.utilities.Utilities;
import proxyservice.utilities.http.content.FormUrlEncodedContent;
import proxyservice.utilities.http.HttpClient;
import proxyservice.utilities.http.content.XmlContent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraProxy implements CameraDataProviderEventListener, CameraDataConsumerEventListener {
    private CameraDataProvider provider;
    private Map<Integer, CameraDataConsumer> consumers = new HashMap<Integer, CameraDataConsumer>();
    private PushChannelSettingsMessage pushChannelSettings;
    private List<String> pushChannels = new ArrayList<String>();

    public synchronized void setProvider(CameraDataProvider provider) {
        this.provider = provider;
        this.provider.setEventListener(this);
    }

    public synchronized void addConsumer(CameraDataConsumer consumer) {
        consumers.put(consumer.getId(), consumer);
        consumer.setEventListener(this);
    }

    public synchronized void removeConsumer(CameraDataConsumer consumer) {
        consumers.remove(consumer.getId());
    }

    public synchronized void onConsumerDataReceived(int consumerId, byte[] data) {
        Message message = Message.tryCreate(data);
        if (message instanceof PushChannelUriMessage) {
            PushChannelUriMessage pushChannelUriMessage = (PushChannelUriMessage)message;
            pushChannels.remove(pushChannelUriMessage.getPreviousUri());
            pushChannels.add(pushChannelUriMessage.getUri());
            App.logger.log("PushChannelUriMessage", "Previous: " + pushChannelUriMessage.getPreviousUri() + ", New: " + pushChannelUriMessage.getUri());
        } else if (provider != null) {
            ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).putInt(4, consumerId);
            provider.send(data);
        }
    }

    public synchronized void onProviderDataReceived(byte[] data) {
        Message message = Message.tryCreate(data);
        if (message instanceof StandardMessage) {
            StandardMessage standardMessage = (StandardMessage)message;
            int consumerId = standardMessage.getConsumerId();
            if (consumerId == 0) {
                for (CameraDataConsumer consumer : consumers.values()) {
                    consumer.send(data);
                }
            } else if (consumers.containsKey(consumerId)) {
                consumers.get(consumerId).send(data);
            }
        } else if (message instanceof PushChannelSettingsMessage) {
            PushChannelSettingsMessage pushChannelSettingsMessage = (PushChannelSettingsMessage)message;
            pushChannelSettings = pushChannelSettingsMessage;
            App.logger.log("PushChannelSettingsMessage", "ClientId: " + pushChannelSettings.getClientId() + ", ClientSecret: " + pushChannelSettings.getClientSecret());
        } else if (message instanceof PushNotificationContentMessage) {
            PushNotificationContentMessage pushNotificationContentMessage = (PushNotificationContentMessage)message;
            String accessToken = tryGetAccessToken();
            if (accessToken != null) {
                sendNotification(accessToken, pushNotificationContentMessage.getContent());
            }
        } else {
            for (CameraDataConsumer consumer : consumers.values()) {
                consumer.send(data);
            }
        }
    }

    private String tryGetAccessToken() {
        if (pushChannelSettings == null) {
            return null;
        }

        FormUrlEncodedContent content = new FormUrlEncodedContent();
        content.add("grant_type", "client_credentials");
        content.add("client_id", pushChannelSettings.getClientId());
        content.add("client_secret", pushChannelSettings.getClientSecret());
        content.add("scope", "notify.windows.com");

        HttpClient httpClient = new HttpClient();
        String responseString = httpClient.post("https://login.live.com/accesstoken.srf", content);
        String accessToken = Utilities.substringBetween(responseString, "access_token\":\"", "\"");
        App.logger.log("Push Notification Authentication", responseString);
        return accessToken;
    }

    private void sendNotification(String accessToken, String content) {
        XmlContent toast = new XmlContent(content);
        for (String pushChannel : pushChannels) {
            try {
                HttpClient httpClient = new HttpClient();
                httpClient.addHeader("Authorization", "Bearer " + accessToken);
                httpClient.addHeader("X-WNS-Type", "wns/toast");
                httpClient.addHeader("X-WNS-Cache-Policy", "no-cache");
                httpClient.addHeader("X-WNS-TTL", "60"); //60 seconds
                httpClient.post(pushChannel, toast);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}