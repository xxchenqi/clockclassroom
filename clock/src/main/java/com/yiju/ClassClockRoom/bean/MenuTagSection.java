package com.yiju.ClassClockRoom.bean;


public class MenuTagSection {

    private String menuTag;
    private int menuSection;

    public String getMenuTag() {
        return menuTag;
    }

    public int getMenuSection() {
        return menuSection;
    }

    public MenuTagSection(String menuTag, int menuSection) {
        this.menuTag = menuTag;
        this.menuSection = menuSection;
    }

    @Override
    public boolean equals(Object o) {
        MenuTagSection mts = (MenuTagSection) o;
        return menuTag.equals(mts.menuTag) && (menuSection == mts.menuSection);
    }

    @Override
    public int hashCode() {
        String in = menuSection + menuTag;
        return in.hashCode();
    }
}
