package data;

public interface ClientSocket extends CommandReceiver {
    void send(byte[] data);
}
