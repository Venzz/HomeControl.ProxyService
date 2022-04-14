package proxyservice.primitives;

public class Id {
    private static int lastUsedId = 0;

    public synchronized static int getNext() {
        return ++lastUsedId;
    }
}
