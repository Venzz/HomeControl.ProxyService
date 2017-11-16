package service.proxy;

import data.ClientSocket;

import java.util.HashSet;
import java.util.Set;

public class CameraProxy {
    private Set<ClientSocket> clients = new HashSet<ClientSocket>();

    public synchronized void add(ClientSocket client) {
        clients.add(client);
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
