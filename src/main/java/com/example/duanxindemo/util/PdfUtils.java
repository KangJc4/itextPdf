package com.example.duanxindemo.util;

import java.io.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

/**
 * 使用 IntelliJ IDEA.
 * Description:
 * User: 康建成
 * Date: 2023-07-07
 * Time: 10:43
 */
public class PdfUtils {
    static String PDF = "D:\\work\\11\\1111.pdf";
    public static final String NAME = "STSong-Light";
    public static final String ENCODE = "UniGB-UCS2-H";
    public static final int SIZE = 12;



    public static void moveContentDown(String inputPath, String outputPath, int numLines) {
        PdfReader reader = null;
        PdfStamper stamper = null;
        FileOutputStream outputStream = null;

        try {
            reader = new PdfReader(inputPath);
            outputStream = new FileOutputStream(outputPath);
            stamper = new PdfStamper(reader, outputStream);

            int numPages = reader.getNumberOfPages();

            for (int i = 1; i <= numPages; i++) {
                PdfImportedPage page = stamper.getImportedPage(reader, i);
                Document doc = new Document(PageSize.A4);
                PdfContentByte canvas = stamper.getOverContent(i);
                canvas.saveState();

                int rotation = reader.getPageRotation(i);
                if (rotation == 90 || rotation == 270) {
                    doc.setPageSize(PageSize.A4.rotate());
                } else {
                    doc.setPageSize(PageSize.A4);
                }

                doc.newPage();
                doc.open();

                // 计算下移的距离
                float moveDistance = -numLines * doc.getPageSize().getHeight() / 50;

                canvas.addTemplate(page, 0, moveDistance);
                canvas.restoreState();

                // 添加原本的内容
                Paragraph originalContent = new Paragraph("原本的内容", new Font(Font.FontFamily.HELVETICA, 12));
                originalContent.setAlignment(Element.ALIGN_LEFT);
                doc.add(originalContent);

                doc.close();
            }

            stamper.close();
            reader.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addTextAndSave(String text, String outputPath) {
        PdfReader pdfReader = null;
        PdfStamper pdfStamper = null;
        FileOutputStream fileOutputStream = null;

        try {
            BaseFont baseFont = BaseFont.createFont(NAME, ENCODE, BaseFont.NOT_EMBEDDED);
            InputStream inputStream = new FileInputStream(new File(PDF));
            pdfReader = new PdfReader(inputStream);
            fileOutputStream = new FileOutputStream(outputPath);
            pdfStamper = new PdfStamper(pdfReader, fileOutputStream);
            PdfContentByte pdfContentByte = pdfStamper.getOverContent(1);
            pdfContentByte.beginText();
            pdfContentByte.setFontAndSize(baseFont, SIZE);
            BaseColor baseColor = new BaseColor(0, 0, 0);
            pdfContentByte.setColorFill(baseColor);
            float lineHeight = baseFont.getFontDescriptor(BaseFont.ASCENT, SIZE) - baseFont.getFontDescriptor(BaseFont.DESCENT, SIZE);
            pdfContentByte.setTextMatrix(50, pdfReader.getPageSize(1).getTop()-lineHeight * 11);
            pdfContentByte.showText(text);
            pdfContentByte.endText();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pdfStamper != null) {
                    pdfStamper.close();
                }
                if (pdfReader != null) {
                    pdfReader.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


//    public static void addTextAndSave(String text, int width, int height, String outputPath) {
//        PdfReader pdfReader = null;
//        PdfStamper pdfStamper = null;
//        FileOutputStream fileOutputStream = null;
//
//        try {
//            BaseFont baseFont = BaseFont.createFont(NAME, ENCODE, BaseFont.NOT_EMBEDDED);
//            InputStream inputStream = new FileInputStream(new File(PDF));
//            pdfReader = new PdfReader(inputStream);
//            fileOutputStream = new FileOutputStream(outputPath);
//            pdfStamper = new PdfStamper(pdfReader, fileOutputStream);
//            PdfContentByte pdfContentByte = pdfStamper.getOverContent(1);
//            pdfContentByte.beginText();
//            pdfContentByte.setFontAndSize(baseFont, SIZE);
//            BaseColor baseColor = new BaseColor(0, 0, 0);
//            pdfContentByte.setColorFill(baseColor);
//            pdfContentByte.setTextMatrix(width, height);
//            pdfContentByte.showText(text);
//            pdfContentByte.endText();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (pdfStamper != null) {
//                    pdfStamper.close();
//                }
//                if (pdfReader != null) {
//                    pdfReader.close();
//                }
//                if (fileOutputStream != null) {
//                    fileOutputStream.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

    /**
     * 生成添加文字数据流
     *
     * @param text   输入信息
     * @param width  宽度
     * @param height 高度
     * @return 数据流
     */
    public static byte[] addText(String text, int width, int height) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        PdfReader pdfReader = null;
        PdfStamper pdfStamper = null;
        PdfContentByte pdfContentByte = null;

        try {
            BaseFont baseFont = BaseFont.createFont(NAME, ENCODE, BaseFont.NOT_EMBEDDED);
            InputStream inputStream = new FileInputStream(new File(PDF)); // 读取默认pdf文件
            pdfReader = new PdfReader(inputStream); // 加载文件到pdf引擎
            byteArrayOutputStream = new ByteArrayOutputStream();
            pdfStamper = new PdfStamper(pdfReader, byteArrayOutputStream); // 加载模板
            pdfContentByte = pdfStamper.getOverContent(1); // 获取顶部
            pdfContentByte.beginText(); // 插入文字信息
            pdfContentByte.setFontAndSize(baseFont, SIZE);
            BaseColor baseColor = new BaseColor(0, 0, 0);
            pdfContentByte.setColorFill(baseColor);
            pdfContentByte.setTextMatrix(width, height); // 设置文字在页面中的坐标
            pdfContentByte.showText(text);
            pdfContentByte.endText();
        } catch (Exception e) {
        } finally {
            try {
                pdfStamper.close();
                pdfReader.close();
                byteArrayOutputStream.close();
            } catch (Exception _e) {
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
}
