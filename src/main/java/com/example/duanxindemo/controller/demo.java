package com.example.duanxindemo.controller;

import com.example.duanxindemo.domain.ImagePro;
import com.example.duanxindemo.domain.PdfPro;
import com.example.duanxindemo.util.EditorPDF;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用 IntelliJ IDEA.
 * Description:
 * User: 康建成
 * Date: 2023-06-21
 * Time: 16:28
 */
public class demo {

    public static void main(String[] args) throws Exception {
        List<PdfPro> pdfList = new ArrayList<>();
        //患者姓名
        //text: --x:179.54--y:604.86--width:5.279999--height:11.071045
        pdfList.add(new PdfPro(100f, 800f, "张三"));
        //性别
        //text: --x:328.27--y:539.43--width:5.279999--height:11.071045
        pdfList.add(new PdfPro(328.27f, 718.62f, "男"));
        //患者电话
        pdfList.add(new PdfPro(250.25f, 702.42f, "13637222876"));
        //身份证号
        pdfList.add(new PdfPro(476.86f, 702.42f, "410122190910130294"));
        //户名
        pdfList.add(new PdfPro(476.74f, 654.18f, "6217477777888888214"));
        pdfList.add(new PdfPro(476.74f, 654.18f, "=================================================================================================================="));
        ArrayList<ImagePro> imgList = new ArrayList();
        // scalePercent 缩放比例
//        imgList.add(new ImagePro(130, 48, 15, "D:\\work\\11\\新建位图图像.bmp"));
        EditorPDF.writeToPdf("D:\\work\\11\\支付申请书.pdf", "D:\\work\\11\\测试PDF01.pdf", imgList, pdfList);
    }

}
