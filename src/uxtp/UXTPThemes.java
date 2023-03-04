package uxtp;

import arc.files.Fi;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import mindustry.graphics.MenuRenderer;
import net.tmmc.graphics.ImageBackgroundRender;
import net.tmmc.graphics.MenuBackgroundRenderers;
import net.tmmc.graphics.Regs;

public class UXTPThemes {
    public static UXTPTheme currentTheme = null;
    public static MenuRenderer previous;

    public static void load() {
        for(int i = 1; i < 29; i++) {
            var theme = new UXTPTheme(i + "", () -> null);
            theme.getter = () -> new ImageBackgroundRender(theme.region);
        }

        new UXTPTheme("mindustry", MenuRenderer::new);

        UXTPSettings.caches().each(UXTPThemes::loadFromFile);
    }

    public static void loadAfter() {
        previous = MenuBackgroundRenderers.getRenderer();
        String themeId = UXTPSettings.get();

        if(!themeId.equals(UXTPSettings.noThemeToken)) {
            var theme = UXTPTheme.themes.find(uxtpTheme -> {
                return uxtpTheme != null && themeId.equals(uxtpTheme.name);
            });

            if(theme != null) {
                currentTheme = theme;
                theme.set();
            }
        }

        UXTPDialog.dialog.buildF();
    }

    public static void loadFromFile(String path) {
        TextureRegion reg;

        try {
            reg = Regs.to(new Texture(new Fi(path)));
        } catch(Throwable ignored) {
            reg = UXTP.atlas3.errorRegion();
        }

        TextureRegion finalReg = reg;
        var theme = new UXTPTheme(UXTPSettings.fileThemeToken + path, () -> {
            return new ImageBackgroundRender(finalReg);
        });

        theme.region = reg;
    }
}