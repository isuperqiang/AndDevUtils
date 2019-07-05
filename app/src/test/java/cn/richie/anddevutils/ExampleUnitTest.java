package cn.richie.anddevutils;

import com.richie.utils.common.DecimalUtils;
import com.richie.utils.common.FeatureUtils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void floatEqual() {
        float a = 0.3f * 3;
        float b = 1.0f - 0.1f;
        boolean b1 = DecimalUtils.floatEquals(a, b);
        boolean b2 = a == b;
        System.out.println("b1:" + b1 + ", b2:" + b2 + ", a: " + a + ", b: " + b);
    }

    @Test
    public void getUUID32() {
        String uuid32 = FeatureUtils.getUUID32();
        System.out.println(uuid32);
        System.out.println("length:" + uuid32.length());
    }

    @Test
    public void md5String() {
        String helloWorld = FeatureUtils.md5Encode("Hello World");
        System.out.println(helloWorld);
        System.out.println("length:" + helloWorld.length());
    }

    @Test
    public void md5File() {
        File file = new File("C:\\AndroidProjects\\SelfProject\\AndDevUtils\\README.md");
        try {
            String md5ByFile = FeatureUtils.getMd5ByFile(file);
            System.out.println(md5ByFile);
            System.out.println("length:" + md5ByFile.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}