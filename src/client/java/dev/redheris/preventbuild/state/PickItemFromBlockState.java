package dev.redheris.preventbuild.state;

import net.minecraft.util.Util;

public class PickItemFromBlockState {
    public static PickItemFromBlockState INSTANCE = new PickItemFromBlockState();

    private boolean waitingSlotChange = false;
    private long waitingStartTimeMs;

    private PickItemFromBlockState() {}

    public static PickItemFromBlockState getInstance() {
        return INSTANCE;
    }

    public boolean isWaitingSlotChange() {
        return waitingSlotChange;
    }

    public void setWaitingSlotChange(boolean waitingSlotChange) {
        // TODO: Maybe opt-in delay logging? To be able to estimate whether this thing is too long
        this.waitingSlotChange = waitingSlotChange;
        if (waitingSlotChange) {
            waitingStartTimeMs = Util.getMillis();
        }
    }

    public long getWaitingStartTimeMs() {
        return waitingStartTimeMs;
    }
}
