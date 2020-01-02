package home.smart.fly.animations.junit;

import home.smart.fly.animations.utils.Tools;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author: rookie
 * @since: 2018-12-20
 * <p>
 * https://blog.csdn.net/qq_17766199/column/info/18260
 */
public class ToolsUnitTest {

    private String version = "1.0.0";

    @BeforeClass
    public static void setUp() {
        System.out.println(ToolsUnitTest.class.getSimpleName() + "=====单元测试开始");
    }

    @AfterClass
    public static void end() {
        System.out.println(ToolsUnitTest.class.getSimpleName() + "=====单元测结束");
    }

    @Test
    public void ToolsTest() {
        assertNotEquals("1111", Tools.getCurrentTime());
        // 起码在 2020 年，这条测试是可以通过的
        assertEquals("2020年", Tools.getCurrentTime(System.currentTimeMillis()));
    }

}
