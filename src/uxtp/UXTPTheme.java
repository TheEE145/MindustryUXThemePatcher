package uxtp;

import arc.func.Prov;
import arc.struct.Seq;
import arc.graphics.g2d.TextureRegion;

import mindustry.graphics.MenuRenderer;
import net.tmmc.graphics.MenuBackgroundRenderers;

import static uxtp.UXTP.*;

public class UXTPTheme {
    public static Seq<UXTPTheme> themes = new Seq<>();

    public Prov<MenuRenderer> getter = () -> this.renderer;
    public MenuRenderer renderer;
    public TextureRegion region;
    public String name;
    public int id;

    public UXTPTheme(String name, Prov<MenuRenderer> getter) {
        this(name, (MenuRenderer) null);
        this.getter = getter;
    }

    public UXTPTheme(String name, MenuRenderer renderer) {
        this.renderer = renderer;
        this.id = themes.size;
        themes.add(this);

        this.name = name;
        this.region = atlas3.get(name);
    }

    public void set() {
        MenuBackgroundRenderers.setRenderer(this.getter.get());
        UXTPThemes.currentTheme = this;
        UXTPSettings.set(this.name);
    }
}