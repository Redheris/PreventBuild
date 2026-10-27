package dev.redheris.preventbuild.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.ClickEvent;
import org.jetbrains.annotations.NotNull;

// God knows I didn’t want to do this.
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
