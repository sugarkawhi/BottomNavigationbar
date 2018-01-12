package me.sugarkawhi.mreader.bean;

/**
 * battery
 * Created by ZhaoZongyao on 2018/1/12.
 */

public class Battery {

    private float head;
    private float width;
    private float height;
    private float gap;

    public Battery(float head, float width, float height, float gap) {
        this.head = head;
        this.width = width;
        this.height = height;
        this.gap = gap;
    }

    public float getHead() {
        return head;
    }

    public void setHead(float head) {
        this.head = head;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getGap() {
        return gap;
    }

    public void setGap(float gap) {
        this.gap = gap;
    }
}
