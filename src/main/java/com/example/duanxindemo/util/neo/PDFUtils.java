package com.example.duanxindemo.util.neo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用 IntelliJ IDEA.
 * Description:
 * User: 康建成
 * Date: 2023-07-28
 * Time: 13:23
 */
public class PDFUtils {

    /**
     *
     * @param filePath 原文件位置
     * @param text 要加入的文本
     * @param derivePath 处理后文件位置
     */
    public static void addPdfInfo(String filePath,String text, String derivePath){
        // 获取当前时间戳，以测量代码运行时间
        long l = System.currentTimeMillis();
        // 存储待处理的PDF文件路径
        String pdfFilePath = filePath;
        try {
            // 使用Apache PDFBox库加载PDF文档
            PDDocument document = PDDocument.load(new File(pdfFilePath));
            // 获取PDF文档的总页数
            int pageCount = document.getNumberOfPages();
            // 仅当文档至少有一页时继续
            if (pageCount > 0) {
                // 获取文档的最后一页
                PDPage page = document.getPage(pageCount - 1);
                // 从Windows字体文件夹加载自定义TrueType字体（STFANGSO.TTF）
                PDFont font = PDType0Font.load(document, new File("/data/ailab/STFANGSO.TTF"));
                // 设置要添加文本的字体大小
                int fontSize = 12;
                //不超过这个值
                float lineWidth = 417;
                float lineHeight = 87;

                // 用于存储文本行和对应的TextPosition对象的列表
                List<String> lines = new ArrayList<>();
                List<List<TextPosition>> textPositionsList = new ArrayList<>();

                // 创建自定义的PDFTextStripper以提取文本并存储TextPosition对象
                PDFTextStripper stripper = new PDFTextStripper() {
                    @Override
                    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                        super.writeString(text, textPositions);
                        // 将提取的文本及其TextPosition对象添加到列表中
                        if (!textPositions.isEmpty()) {
                            lines.add(text);
                            textPositionsList.add(textPositions);
                        }
                    }
                };

                // 从PDF中提取文本并填充textPositionsList
                stripper.getText(document);
                // 如果有任何提取的文本，则继续
                if (!textPositionsList.isEmpty()) {
                    // 获取最后一页的最后一行的最后一个字符的TextPosition
                    List<TextPosition> textPositions = textPositionsList.get(textPositionsList.size() - 1);
                    TextPosition textPosition = textPositions.get(textPositions.size() - 1);
                    // 获取第一页的第一行的第一个字符的TextPosition
                    List<TextPosition> textPositions1 = textPositionsList.get(0);
                    TextPosition textPosition1 = textPositions1.get(0);
                    // 设置要添加的新文本的起始X坐标
//                    float x = textPosition1.getX();
                    float x = 90;
                    // 设置当前文本的起始Y坐标
                    float currentY = textPosition.getEndY();
                    // 设置要添加的新文本的下一行的Y坐标
                    float nextLineY = textPosition.getEndY() - fontSize;
                    String[] textLines = new String[0];
                    if (text != null || text.isEmpty()) {
                        textLines = splitTextToFitLine(text, font, fontSize, lineWidth);
                    }


                    boolean b = true;
                    // 用于处理文本定位的变量 // 行之间的垂直间距
                    int a = 12;
                    // 行之间的垂直间距
                    for (String line : textLines) {
                        if (nextLineY > lineHeight) {
                            // 如果行高不超过预定值，则将该行添加到当前页
                            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                            contentStream.setFont(font, fontSize);
                            contentStream.beginText();
                            // 设置新行的起始位置
                            contentStream.newLineAtOffset(x, nextLineY);
                            // 将该行添加到当前页
                            contentStream.newLineAtOffset(0, -fontSize - (fontSize / 2) - a);
                            contentStream.showText(line);
                            //参数为每次的偏移量
                            contentStream.endText();
                            contentStream.close();
                            // 更新下一行文本的Y坐标和垂直间距
                            lineHeight = lineHeight + 50;
                            a = a + 20;
                        } else {
                            if (b) {
                                // 创建新页面并在其中写入文本，如果文本超过最大行高
                                PDPage newPage = new PDPage(page.getMediaBox());
                                document.addPage(newPage);
                                page = newPage;
                            }
                            // 如果行高超过预定值，则将该行添加到新页面
                            PDPageContentStream contentStream1 = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                            // 设置新页面上新行的起始位置
                            contentStream1.setFont(font, fontSize);
                            contentStream1.beginText();
                            // 将该行添加到新页面
                            contentStream1.newLineAtOffset(x, page.getMediaBox().getHeight());

                            contentStream1.newLineAtOffset(0, -fontSize - (fontSize / 2) - a);
                            contentStream1.showText(line);
                            contentStream1.endText();
                            contentStream1.close();
                            // 更新新页面的行高和垂直间距
                            lineHeight = lineHeight + 50;
                            a = a + 20;
                            b = false;
                        }

                    }
                }
                // 将修改后的文档保存到新文件中
                document.save(derivePath);
            }

            // 关闭PDF文档
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取当前时间戳以再次测量代码运行时间
        long l1 = System.currentTimeMillis();
        // 计算并打印过程所花费的时间
        System.out.println("耗费时间："+(l1-l));
    }



    /**
     * 该方法将字符串分段，对与太长的字符串剪切分割换行
     * @param text
     * @return
     * @throws IOException
     */
    public static String[] splitTextToFitLine(String text, PDFont font, int fontSize, float lineWidth) throws IOException {
        //将字符串全部分割开
        String[] words = text.split("\n");
        String[] split = words[2].split("");
        int length = split.length + 2;
        // 创建一个新的具有所需大小的数组
        String[] newArray = new String[length];
        // 将原始数组的元素复制到新数组中
        System.arraycopy(words, 0, newArray, 0, words.length);

        // 确保要替换的新元素个数不超过原始数组可替换的元素个数
        int startIndex = 2; // 第三个元素的索引位置
        int endIndex = startIndex + split.length - 1; // 最后一个要替换的元素的索引位置
        if (endIndex >= newArray.length) {
            endIndex = newArray.length - 1;
        }

        // 将新元素逐个替换原始数组中对应的元素
        for (int i = startIndex, j = 0; i <= endIndex && j < split.length; i++, j++) {
            newArray[i] = split[j];
        }

        //将每行的字符串写入到 currentLine 中，以空格分割
        StringBuilder currentLine = new StringBuilder();
        //存储每次遍历的内容，将达到换行标准的数据存到此处
        StringBuilder stringBuilder = new StringBuilder();

        //遍历循环全部的字符
        for (int i=0;i<newArray.length;i++){
            String word = newArray[i].replaceAll(" ","");
            //将字符写入到stringBuilder中
            stringBuilder = stringBuilder.append(word);
            //计算此时字符串的长度
            float a = font.getStringWidth(String.valueOf(stringBuilder)) / 1000 * fontSize;
            //判断如果字符串长度在限定长度内
            if (a > lineWidth || i==newArray.length-1 || i<2 ) {
                currentLine.append(String.valueOf(stringBuilder)).append(" ");
                stringBuilder.delete(0, stringBuilder.length());
            }
        }

        String[] s = currentLine.toString().split(" ");

        return s;
    }
}
