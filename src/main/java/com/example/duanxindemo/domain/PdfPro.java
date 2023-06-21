package com.example.duanxindemo.domain;

/**
 * 使用 IntelliJ IDEA.
 * Description:
 * User: 康建成
 * Date: 2023-06-21
 * Time: 16:22
 */
public class PdfPro {
    private String text;  // 文本
    private float x; //x 坐标
    private float y; //y 坐标

    public PdfPro() {
    }

    public PdfPro(float x, float y, String text) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
}

