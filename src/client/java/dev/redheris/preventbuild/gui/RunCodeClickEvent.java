package dev.redheris.preventbuild.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.ClickEvent;
import org.jetbrains.annotations.NotNull;

// God knows I didn’t want to do this.

//? if >1.21.4 {
@Environment(EnvType.CLIENT)
public record RunCodeClickEvent(Runnable codeAction) implements ClickEvent {
    @Override
    public @NotNull Action action() {
        // just because it is not allowed from server
        return Action.OPEN_FILE;
    }

    public void run() {
        codeAction.run();
    }
}
//?} else {
/*@Environment(EnvType.CLIENT)
public class RunCodeClickEvent extends ClickEvent {
    private final Runnable codeAction;

    public RunCodeClickEvent(Runnable codeAction) {
        super(Action.OPEN_FILE, "run_code");
        this.codeAction = codeAction;
    }

    @Override
    public @NotNull Action getAction() {
        // just because it is not allowed from server
        return Action.OPEN_FILE;
    }

    public void run() {
        codeAction.run();
    }
}
*///?}