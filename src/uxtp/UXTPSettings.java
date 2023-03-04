package uxtp;

import arc.struct.Seq;
import org.jetbrains.annotations.NotNull;

import static arc.Core.*;
import static mindustry.Vars.*;

public class UXTPSettings {
    public static final String noThemeToken = "UXTPTheme.NOT_FOUND.-1";
    public static final String fileThemeToken = "UXTP.LOAD/";
    public static final String themesSettingId = "UXTP.THEME";
    public static final String filesCacheId = "UXTP.CACHE_FILES.";
    public static final String themesLoadTimer = "tlt";
    public static final String mobileRows = "mobilerows";
    public static final String pcRows = "pcrows";

    public static int getSlider() {
        return settings.getInt(themesLoadTimer);
    }

    public static int getRows() {
        return mobile ? settings.getInt(mobileRows) : settings.getInt(pcRows);
    }

    public static void load() {
        ui.settings.addCategory("UXThemePatcher", settingsTable -> {
            settingsTable.sliderPref(themesLoadTimer, 15, 1, 60, (f) -> {
                return f + " " + bundle.get(themesLoadTimer + ".val");
            });

            settingsTable.sliderPref(mobileRows, 3, 2, 15, (f) -> {
                return f + " " + bundle.get("uxtp.rows");
            });

            settingsTable.sliderPref(pcRows, 8, 2, 30, (f) -> {
                return f + " " + bundle.get("uxtp.rows");
            });
        });
    }

    public static int maxId() {
        int i = 0;
        while(settings.has(filesCacheId + i)) {
            i++;
        }

        return i;
    }

    public static @NotNull Seq<String> caches() {
        Seq<String> result = new Seq<>();
        for(int i = 0; i < maxId(); i++) {
            result.add(at(i));
        }

        return result;
    }

    public static String at(int id) {
        return settings.getString(filesCacheId + id);
    }

    public static void set(int id, String value) {
        settings.put(filesCacheId + id, value);
    }

    public static void set(String theme) {
        settings.put(themesSettingId, theme);
    }

    public static String get() {
        return settings.getString(themesSettingId, noThemeToken);
    }
}