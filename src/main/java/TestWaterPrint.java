import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class TestWaterPrint {
    public static void main(String[] args) throws DocumentException, IOException {
        if (args.length!=3)
        {
            System.out.println("TestWaterPrint 源文件 目标文件 水印");
            return;
        }
        // 要输出的pdf文件
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(args[1])));
        // 将pdf文件先加水印然后输出
        setWatermark(bos, args[0], args[2], 16);
    }

    /**
     *
     * @param input
     *            原PDF位置
     * @param waterMarkName
     *            页脚添加水印
     * @param permission
     *            权限码
     * @throws DocumentException
     * @throws IOException
     */
    public static void setWatermark(BufferedOutputStream bos, String input, String waterMarkName, int permission)
            throws DocumentException, IOException {
        PdfReader reader = new PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, bos);
        int total = reader.getNumberOfPages() + 1;
        PdfContentByte content;/*"STSong-Light", "UniGB-UCS2-H"*/
        BaseFont base = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.EMBEDDED);
        PdfGState gs = new PdfGState();
        for (int i = 1; i < total; i++) {
            content = stamper.getOverContent(i);// 在内容上方加水印
            // content = stamper.getUnderContent(i);//在内容下方加水印
            gs.setFillOpacity(0.2f);
            // content.setGState(gs);
            content.beginText();
            content.setColorFill(Color.LIGHT_GRAY);
            content.setFontAndSize(base, 50);
            content.setTextMatrix(70, 200);
            content.showTextAligned(Element.ALIGN_CENTER, waterMarkName, 300, 350, 55);
            /*Image image = Image.getInstance("G:/2.jpeg");

              img.setAlignment(Image.LEFT | Image.TEXTWRAP);
              img.setBorder(Image.BOX); img.setBorderWidth(10);
              img.setBorderColor(BaseColor.WHITE); img.scaleToFit(100072);//大小
              img.setRotationDegrees(-30);//旋转

            image.setAbsolutePosition(200, 206); // set the first background
            // image of the absolute
            image.scaleToFit(200, 200);
            content.addImage(image);*/
            content.setColorFill(Color.BLACK);
            content.setFontAndSize(base, 8);
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            content.showTextAligned(Element.ALIGN_CENTER, "生成时间：" + format.format(cal.getTime()) + "", 300, 10, 0);
            content.endText();

        }
        stamper.close();
    }
}