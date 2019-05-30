package com.engineer.imitate.interfaces.testinterfaces

import com.engineer.imitate.interfaces.testinterfaces.SimpleInterface
import com.engineer.imitate.interfaces.testinterfaces.SimpleJavaInterface
import com.engineer.imitate.interfaces.testinterfaces.SimpleJavaInterface1

/**
 * @author: zhuyongging
 * @since: 2019-05-30
 */
class Test {

    private fun a():String {
        return ""
    }

    private fun b() {
        val aa = AA()
        aa.setSimpleInterface(object : SimpleInterface {
            override fun ddd() {

            }

            override fun doe():String {
                return a()
            }
        })

        aa.setSimpleJavaInterface(SimpleJavaInterface { a() })

        aa.setSimpleJavaInterface1(object : SimpleJavaInterface1 {
            override fun dooe() {
                a()
            }

        })

        Thread(Runnable {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        })
    }

    private inner class AA {
        internal lateinit var mSimpleInterface: SimpleInterface

        fun setSimpleInterface(simpleInterface: SimpleInterface) {
            mSimpleInterface = simpleInterface
        }

        internal lateinit var mSimpleJavaInterface: SimpleJavaInterface

        fun setSimpleJavaInterface(simpleInterface: SimpleJavaInterface) {
            mSimpleJavaInterface = simpleInterface
        }

        internal lateinit var mSimpleJavaInterface1: SimpleJavaInterface1

        fun setSimpleJavaInterface1(simpleInterface: SimpleJavaInterface1) {
            mSimpleJavaInterface1 = simpleInterface
        }
    }
}
