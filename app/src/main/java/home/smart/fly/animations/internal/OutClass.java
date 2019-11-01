package home.smart.fly.animations.internal;

import home.smart.fly.animations.internal.mm.SimpleMMClass;

/**
 * @author rookie
 * @since 08-06-2019
 */
public class OutClass {
    private static final String OUT = "THIS IS OUT CLASS";

    private int value = 0;

    public int x = 0;

    protected int y = 0;

    int z = 0;

    private InnerClass innerClass = new InnerClass();

    private void test() {
        String temm ;
        temm = innerClass.a;
        temm = innerClass.b;
        temm = innerClass.c;
        temm = innerClass.test;
        innerClass.aa();
    }

    private SimpleClass simpleClass = new SimpleClass();
    private void test1() {
        String temp ;
        temp = simpleClass.a;
        temp = simpleClass.b;
        temp = simpleClass.c;

        simpleClass.bb();
        simpleClass.ccc();
        simpleClass.dd();
    }

    private SimpleMMClass mm = new SimpleMMClass();
    private void test2() {
        String temp;
        temp= mm.a;
        mm.bb();
    }

    private class InnerClass {
        private String test="e";
        public String a;
        protected String b;
        String c;

        private void aa() {
            String dd = OutClass.OUT;
            System.out.println("this is " + x);
            System.out.println("this is " + value);
            System.out.println("this is " + y);
            System.out.println("this is " + z);
        }
    }
}
