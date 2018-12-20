package home.smart.fly.animations.junit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import home.smart.fly.animations.utils.Tools;

import static org.junit.Assert.*;

/**
 * @author: rookie
 * @since: 2018-12-20
 *
 * https://blog.csdn.net/qq_17766199/column/info/18260
 */
public class ToolsUnitTest {

    private String version = "1.0.0";


    @BeforeClass
    public static void setUp() {
        System.out.println( ToolsUnitTest.class.getSimpleName()+ "=====单元测试开始");
    }

    @AfterClass
    public static void end() {
        System.out.println( ToolsUnitTest.class.getSimpleName()+ "=====单元测结束");
    }

    @Test
    public void getCurrentTimeTest() {
        assertNotEquals("1111",Tools.getCurrentTime());
    }
}
