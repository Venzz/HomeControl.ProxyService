package service.proxy;

import data.ClientSocket;
import data.ServiceSocket;

import java.util.HashSet;
import java.util.Set;

public class CameraProxy {
    private ServiceSocket service;
    private Set<ClientSocket> clients = new HashSet<ClientSocket>();

    public synchronized void set(ServiceSocket service) {
        if (this.service != null) {
            for (ClientSocket client : clients) {
                client.removePerformer(service);
            }
        }
        this.service = service;
        for (ClientSocket client : clients) {
            client.addPerformer(service);
        }
    }

    public synchronized void add(ClientSocket client) {
        clients.add(client);
        if (service != null) {
            client.addPerformer(service);
        }
    }

    public synchronized void remove(ClientSocket client) {
        clients.remove(client);
    }

    public synchronized void send(byte[] data) {
        for (ClientSocket client : clients) {
            client.send(data);
        }
    }
}
