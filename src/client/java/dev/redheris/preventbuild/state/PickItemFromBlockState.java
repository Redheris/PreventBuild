package dev.redheris.preventbuild.state;

public class PickItemFromBlockState {
    public static PickItemFromBlockState INSTANCE = new PickItemFromBlockState();

    private PickItemFromBlockState() {}

    public static PickItemFromBlockState getInstance() {
        return INSTANCE;
    }

    private boolean waitingSlotChange = false;

    public boolean isWaitingSlotChange() {
        return waitingSlotChange;
    }

    public void setWaitingSlotChange(boolean waitingSlotChange) {
        // TODO: Maybe opt-in delay logging? To be able to estimate whether this thing is too long
        this.waitingSlotChange = waitingSlotChange;
    }
}
