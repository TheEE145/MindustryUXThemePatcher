package uxtp;

import arc.files.Fi;

import arc.graphics.Color;
import arc.scene.ui.Dialog;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.ui.dialogs.BaseDialog;

import net.tmmc.graphics.FDraw;
import net.tmmc.graphics.MenuBackgroundRenderers;
import net.tmmc.graphics.Regs;
import net.tmmc.util.FFiles;
import net.tmmc.util.ThrowableUtils;
import net.tmmc.util.UIUtils;

import static arc.Core.*;
import static mindustry.Vars.*;

public class UXTPDialog extends BaseDialog {
    public static UXTPDialog dialog;
    public static String search = "";

    public static void send(String message) {
        UIUtils.invoke("@uxtp.fail", cont -> {
            cont.add("[red]" + message + "[]");
        });
    }

    public static void load() {
        dialog = new UXTPDialog();
        ui.menufrag.addButton(UXTP.jsonModUTPX.getDisplayName(), dialog::show);
    }

    public UXTPDialog() {
        super(UXTP.jsonModUTPX.getDisplayName());
        build(true);
    }

    public void buildF() {
        build(false);
    }

    public void build(boolean repeat) {
        this.buttons.clearChildren();
        this.cont.clearChildren();

        this.buttons.defaults().size(200, 50);
        this.buttons.button("@left", Icon.left, this::hide);
        this.buttons.button("@uxtp.setToPrev", () -> {
            MenuBackgroundRenderers.setRenderer(UXTPThemes.previous);
            UXTPSettings.set(UXTPSettings.noThemeToken);
            UXTPThemes.currentTheme = null;
            buildF();
        });

        this.cont.table(table -> {
            table.table(uploader -> {
                uploader.field(search, ignored -> {}).update(field -> {
                    search = field.getText().replace('\\', '/');
                }).valid(str -> {
                    return FFiles.validFile(new Fi(str));
                }).growX().expandX().pad(6);

                uploader.button(Icon.upload, () -> {
                    if(UXTPSettings.caches().contains(search)) {
                        send(bundle.get("uxtp.fail.1"));
                        return;
                    }

                    Fi fi = new Fi(search);
                    if(!FFiles.validFile(fi) || !fi.extension().equals("png")) {
                        send(bundle.get("uxtp.fail.2"));
                        return;
                    }

                    try {
                        UXTPSettings.set(UXTPSettings.maxId(), fi.absolutePath());
                        UIUtils.invoke("@uxtp.exit.title", (cont) -> {
                            cont.add("@uxtp.exit.text").row();
                            cont.button(Icon.exit, app::exit).size(48).pad(6);
                        });
                    } catch(Throwable throwable) {
                        ThrowableUtils.showDialog(throwable);
                    }
                }).size(48).pad(6);
            }).growX().top().row();

            table.image().height(4f).padTop(3).padBottom(3).growX().row();

            table.pane(main -> {
                for(UXTPTheme theme : UXTPTheme.themes) {
                    main.button(Regs.to(theme.region), () -> {
                        theme.set();
                        buildF();
                    }).size(100, 50).pad(6).color(
                            UXTPThemes.currentTheme != theme ? Color.white : Pal.accent
                    );

                    int id = theme.id + 1;
                    if(id % UXTPSettings.getRows() == 0) {
                        main.row();
                    }
                }
            }).grow();

            table.setBackground(Tex.button);
        }).center().size(FDraw.width(mobile ? 1 : 0.8f), FDraw.height(mobile ? 1 : 0.6f));

        if(repeat) {
            buildF();
        }
    }

    @Override
    public Dialog show() {
        buildF();
        return super.show();
    }
}