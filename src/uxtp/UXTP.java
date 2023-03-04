package uxtp;

import mindustry.game.EventType.ClientLoadEvent;

import net.tmmc.json.JsonMod;
import net.tmmc.util.events.Events;
import net.tmmc.graphics.ModAtlas;
import net.tmmc.util.ModLogger;
import net.tmmc.ApplicationMod;

@net.tmmc.annotations.Mod
public class UXTP extends ApplicationMod {
    public static JsonMod jsonModUTPX;
    public static ModLogger logger3;
    public static ModAtlas atlas3;

    {
        init(() -> {
            jsonModUTPX = this.loadedMod;
            logger3 = this.logger;
            atlas3 = this.atlas;
        });
    }

    public UXTP() {
        Events.currentEvent = ClientLoadEvent.class;
        Events.register(UXTPThemes::load);
        Events.register(UXTPSettings::load);
        Events.register(UXTPDialog::load);

        int s = UXTPSettings.getSlider();
        Events.register(s, UXTPThemes::loadAfter);
    }
}
