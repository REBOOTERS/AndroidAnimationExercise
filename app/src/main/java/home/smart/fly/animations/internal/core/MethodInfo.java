package home.smart.fly.animations.internal.core;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MethodInfo {

    private static final String OUTPUT_FORMAT = "The method's name is %s ,the cost is %dms and the result is ";

    private String mClassName;              // 类名
    private String mMethodName;             // 方法名
    private String mMethodDesc;             // 方法描述符
    private Object mResult;                 // 方法执行结果
    private long mCost;                     // 方法执行耗时
    private List<Object> mArgumentList;     // 方法参数列表

    MethodInfo() {
        mArgumentList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format(Locale.CHINA, OUTPUT_FORMAT, getMethodName(), mCost) + mResult;
    }

    /**
     * @param className 设置类名
     */
    public void setClassName(String className) {
        mClassName = className;
    }

    /**
     * @return 返回类名
     */
    public String getClassName() {
        mClassName = mClassName.replace("/", ".");
        return mClassName;
    }

    /**
     * @param methodName 设置方法名
     */
    public void setMethodName(String methodName) {
        mMethodName = methodName;
    }

    /**
     * @return 返回方法名
     */
    public String getMethodName() {
        StringBuilder msg = new StringBuilder();
//        Type[] argumentTypes = Type.getArgumentTypes(mMethodDesc);
//        msg.append('(');
//        for (int i = 0; i < argumentTypes.length; i++) {
//            msg.append(argumentTypes[i].getClassName());
//            if (i != argumentTypes.length - 1) {
//                msg.append(", ");
//            }
//        }
//        msg.append(')');
        mMethodName = mMethodName + msg.toString();
        return mMethodName;
    }

    /**
     * @param cost 设置方法执行耗时
     */
    public void setCost(long cost) {
        this.mCost = cost;
    }

    /**
     * @return 返回方法执行耗时
     */
    public long getCost() {
        return mCost;
    }

    /**
     * @param result 设置方法执行结果
     */
    public void setResult(Object result) {
        this.mResult = result;
    }

    /**
     * @return 返回方法执行结果
     */
    public Object getResult() {
        return mResult;
    }

    /**
     * @param methodDesc 设置方法描述符
     */
    public void setMethodDesc(String methodDesc) {
        this.mMethodDesc = methodDesc;
    }

    /**
     * 添加方法参数
     *
     * @param argument 方法参数
     */
    public void addArgument(Object argument) {
        mArgumentList.add(argument);
    }

    /**
     * @return 得到方法参数列表
     */
    public List<Object> getArgumentList() {
        return mArgumentList;
    }
}
