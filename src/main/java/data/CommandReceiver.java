package data;

import data.CommandPerformer;

public interface CommandReceiver {
    void addPerformer(CommandPerformer performer);
    void removePerformer(CommandPerformer performer);
}
