package com.engineer.imitate.interfaces.testinterfaces;

import com.engineer.imitate.interfaces.testinterfaces.SimpleJavaInterface;

/**
 * @author: zhuyongging
 * @since: 2019-05-30
 */
public class Test1 {
    private void a(){

    }

    private void b(){
        AA aa = new AA();
        aa.setSimpleInterface(this::a);
    }

    private class AA {
        SimpleJavaInterface mSimpleInterface;

        public void setSimpleInterface(SimpleJavaInterface simpleInterface) {
            mSimpleInterface = simpleInterface;
        }
    }
}
