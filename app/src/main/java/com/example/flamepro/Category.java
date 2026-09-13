package com.example.flamepro;

public class Category {
    private String name;
    private int iconResource;
    private boolean isSelected;

    public Category(String name, int iconResource, boolean isSelected) {
        this.name = name;
        this.iconResource = iconResource;
        this.isSelected = isSelected;
    }

    public String getName() { return name; }
    public int getIconResource() { return iconResource; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}
