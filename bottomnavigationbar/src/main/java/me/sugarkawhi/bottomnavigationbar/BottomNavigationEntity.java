package me.sugarkawhi.bottomnavigationbar;

/**
 * 状态栏Item实体类
 * Created by sugarkawhi on 2017/11/1.
 */
public class BottomNavigationEntity {
    private String text;
    private int selectedIcon;
    private int unSelectIcon;

    //need text
    public BottomNavigationEntity(String text, int unSelectIcon, int selectedIcon) {
        this.text = text;
        this.unSelectIcon = unSelectIcon;
        this.selectedIcon = selectedIcon;
    }

    //do not need text
    public BottomNavigationEntity(int unSelectIcon, int selectedIcon) {
        this.unSelectIcon = unSelectIcon;
        this.selectedIcon = selectedIcon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(int selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public int getUnSelectIcon() {
        return unSelectIcon;
    }

    public void setUnSelectIcon(int unSelectIcon) {
        this.unSelectIcon = unSelectIcon;
    }
}
