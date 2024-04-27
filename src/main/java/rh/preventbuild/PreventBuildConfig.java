package rh.preventbuild;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "preventbuild")
public class PreventBuildConfig implements ConfigData {

    @ConfigEntry.Category("gameplay")
    public boolean enabled = false;

    @ConfigEntry.Category("gameplay")
    public boolean blockCarpets = true;

    @ConfigEntry.Category("gameplay")
    public boolean blockStripping = true;

    @ConfigEntry.Category("gameplay")
    public boolean doBlockBreakY = true;

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.Gui.Tooltip
    public String breakY = "";

    @ConfigEntry.Category("gameplay")
    public boolean doBlockPlaceY = true;

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.Gui.Tooltip
    public String placeY = "";

    @ConfigEntry.Category("gameplay")
    public boolean blockBreakBlocks = true;

    @ConfigEntry.Category("gameplay")
    @ConfigEntry.Gui.Tooltip
    public String breakBlocks = "";

}
