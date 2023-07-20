package cn.fhyjs.jntm.utility;

public class TimeSyncer {
    public native String getNativeString(); // 声明本地方法
    public TimeSyncer(){
        System.loadLibrary("native"); // 加载本地库文件
        System.out.println(getNativeString());
    }
}
