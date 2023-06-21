package com.example.duanxindemo.domain;

/**
 * 使用 IntelliJ IDEA.
 * Description:
 * User: 康建成
 * Date: 2023-06-21
 * Time: 16:25
 */
public class ImagePro {
    private float x; //x 坐标
    private float y; //y 坐标
    private float scalePercent;  //缩放百分比
    private String imgPath; //路径

    public ImagePro() {
    }

    public ImagePro(float x, float y, float scalePercent, String imgPath) {
        this.x = x;
        this.y = y;
        this.scalePercent = scalePercent;
        this.imgPath = imgPath;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getScalePercent() {
        return scalePercent;
    }

    public void setScalePercent(float scalePercent) {
        this.scalePercent = scalePercent;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}


