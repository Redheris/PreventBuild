package dev.redheris.preventbuild.state;

import dev.redheris.preventbuild.PreventBuild;
import dev.redheris.preventbuild.PreventBuildClient;
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
        this.waitingSlotChange = waitingSlotChange;
        if (waitingSlotChange) {
            waitingStartTimeMs = Util.getMillis();
        } else if (PreventBuildClient.commonConfig.logWaitingTime()) {
            double seconds = (Util.getMillis() - waitingStartTimeMs) / 1000.0;
            PreventBuild.LOGGER.info("Received SetHeldSlot packet after {} seconds", seconds);
        }
    }

    public long getWaitingStartTimeMs() {
        return waitingStartTimeMs;
    }
}
