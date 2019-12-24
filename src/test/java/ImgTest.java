package supplier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.alibaba.simpleimage.ImageRender;
import com.alibaba.simpleimage.SimpleImageException;
import com.alibaba.simpleimage.render.ReadRender;
import com.alibaba.simpleimage.render.ScaleParameter;
import com.alibaba.simpleimage.render.ScaleRender;
import com.alibaba.simpleimage.render.WriteRender;
import org.apache.commons.io.IOUtils;
import org.junit.Test;


/**
 * @author lianhong
 * @description
 * @date 2019/12/4 0004下午 12:13
 */
public class ImgTest {
    @Test
    public void test() {
        File in = new File("C:\\Users\\Administrator\\Desktop\\123.png");      //原图片
        File out = new File("C:\\Users\\Administrator\\Desktop\\ali_bak_300.jpg");       //目的图片
        File out1 = new File("d:/desktop/d/ali_bak_50.jpg");       //目的图片
        ScaleParameter scaleParam = new ScaleParameter(300, 300);  //将图像缩略到1024x1024以内，不足1024x1024则不做任何处理

        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        WriteRender wr = null;
        try {
            inStream = new FileInputStream(in);
            outStream = new FileOutputStream(out);
            ImageRender rr = new ReadRender(inStream);
            ImageRender sr = new ScaleRender(rr, scaleParam);
            wr = new WriteRender(sr, outStream);

            wr.render();                            //触发图像处理
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inStream);         //图片文件输入输出流必须记得关闭
            IOUtils.closeQuietly(outStream);
            if (wr != null) {
                try {
                    wr.dispose();                   //释放simpleImage的内部资源
                } catch (SimpleImageException ignore) {
                    // skip ...
                }
            }
        }
    }
}
