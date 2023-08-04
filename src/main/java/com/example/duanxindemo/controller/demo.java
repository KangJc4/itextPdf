package com.example.duanxindemo.controller;

import com.example.duanxindemo.domain.ImagePro;
import com.example.duanxindemo.domain.PdfPro;
import com.example.duanxindemo.util.EditorPDF;
import com.example.duanxindemo.util.PdfUtils;
import com.example.duanxindemo.util.neo.PDFUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 使用 IntelliJ IDEA.
 * Description:
 * User: 康建成
 * Date: 2023-06-21
 * Time: 16:28
 */
public class demo {

    public static void main(String[] args) {

        long l = System.currentTimeMillis();
        String pdfFilePath = "D:\\work\\11\\9.pdf";
        try {
            PDDocument document = PDDocument.load(new File(pdfFilePath));
            int pageCount = document.getNumberOfPages();

            if (pageCount > 0) {
                PDPage page = document.getPage(pageCount - 1);
                PDFont font = PDType0Font.load(document, new File("C:\\Windows\\Fonts\\STFANGSO.TTF"));
                int fontSize = 12;
                //不超过这个值
                float lineWidth = 417;
                float lineHeight = 87;

                List<String> lines = new ArrayList<>();
                List<List<TextPosition>> textPositionsList = new ArrayList<>();

                // 创建自定义的PDFTextStripper类
                PDFTextStripper stripper = new PDFTextStripper() {
                    @Override
                    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                        super.writeString(text, textPositions);
                        if (!textPositions.isEmpty()) {
                            lines.add(text);
                            textPositionsList.add(textPositions);
                        }
                    }
                };

                // 提取文本并填充textPositionsList
                stripper.getText(document);

                if (!textPositionsList.isEmpty()) {
                    List<TextPosition> textPositions = textPositionsList.get(textPositionsList.size() - 1);
                    TextPosition textPosition = textPositions.get(textPositions.size() - 1);
                    List<TextPosition> textPositions1 = textPositionsList.get(0);
                    TextPosition textPosition1 = textPositions1.get(0);
                    float x = 90;

                    float currentY = textPosition.getEndY();

                    float nextLineY = textPosition.getEndY() - fontSize;


                    PDRectangle imageBoundingBox = null;

                    float hei=0;
                    PDResources resources = page.getResources();
                    if (resources != null) {
                        for (COSName xObjectName : resources.getXObjectNames()) {
                            PDXObject xObject = resources.getXObject(xObjectName);
                            if (xObject instanceof PDImageXObject) {
                                PDImageXObject image = (PDImageXObject) xObject;
                                imageBoundingBox = getImageBoundingBox(image, page,hei);
                                System.out.println("Y: " + imageBoundingBox.getLowerLeftY());
                                hei= imageBoundingBox.getHeight();
                            }
                        }
                    }

                    if (imageBoundingBox!=null && (imageBoundingBox.getLowerLeftY() - fontSize) < nextLineY) {
                        nextLineY = (imageBoundingBox.getLowerLeftY() - fontSize);
                    }


                    //要插入的文本信息
                    String longText = "--------------------------------------------\n" + "评分： tbExperimentalReport.getScore()" + "\n评语：tbExperimentalReport.getComment()";
                    String[] textLines = PDFUtils.splitTextToFitLine(longText, font, fontSize, lineWidth);

                    // 创建新的一页并在新页中写入文本
                    PDPage newPage = new PDPage(page.getMediaBox());
                    document.addPage(newPage);

                    int a = 12;
                    for (String line : textLines) {
                        float capHeight = font.getFontDescriptor().getCapHeight();
                        if (nextLineY > lineHeight) {
                            // 在当前页的下一行续写
                            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                            contentStream.setFont(font, fontSize);
                            contentStream.beginText();
                            //参数为起始位置坐标
                            contentStream.newLineAtOffset(x, nextLineY);
                            contentStream.newLineAtOffset(0, -fontSize - (fontSize / 2) - a);
                            contentStream.showText(line);
                            //参数为每次的偏移量
                            contentStream.endText();
                            contentStream.close();
                            lineHeight = lineHeight + 50;
                            a = a + 20;
                        } else {
                            PDPageContentStream contentStream1 = new PDPageContentStream(document, newPage, PDPageContentStream.AppendMode.APPEND, true);
                            // 在当前页的下一行续写
                            contentStream1.setFont(font, fontSize);
                            contentStream1.beginText();
                            //参数为起始位置坐标
                            contentStream1.newLineAtOffset(x, newPage.getMediaBox().getHeight());

                            contentStream1.newLineAtOffset(0, -fontSize - (fontSize / 2) - a);
                            contentStream1.showText(line);
                            contentStream1.endText();
                            contentStream1.close();
                            lineHeight = lineHeight + 50;
                            a = a + 20;
                        }

                    }
                }
                document.save("D:\\work\\11\\file.pdf");
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long l1 = System.currentTimeMillis();
        System.out.println(l1-l);



    }

    private static PDRectangle getImageBoundingBox(PDImageXObject image, PDPage page, float height) throws IOException {
        // 获取图片的宽度和高度
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        // 获取图片在页面中的位置和大小
        Rectangle2D.Float imageRegion = new Rectangle2D.Float(0, 0, imageWidth, imageHeight);
        PDRectangle cropBox = page.getCropBox();
        PDRectangle mediaBox = page.getMediaBox();
        float pageWidth = cropBox.getWidth();
        float pageHeight = cropBox.getHeight();

        // 计算图片的位置和大小
        float imageX = imageRegion.x + cropBox.getLowerLeftX();
        float v = cropBox.getUpperRightY() - 100;
        float imageY = v - imageRegion.y - imageHeight/2 - height/2;
        float imageWidthOnPage = imageWidth * cropBox.getWidth() / mediaBox.getWidth();
        float imageHeightOnPage = imageHeight * cropBox.getHeight() / mediaBox.getHeight();

        // 返回图片在页面中的位置和大小信息
        return new PDRectangle(imageX, imageY, imageWidthOnPage, imageHeightOnPage);
    }

}
