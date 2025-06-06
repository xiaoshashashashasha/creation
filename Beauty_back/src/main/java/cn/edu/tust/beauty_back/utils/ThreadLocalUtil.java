package cn.edu.tust.beauty_back.utils;

public class ThreadLocalUtil {

    //提供ThreadLocal对象
    private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();

    //根据键获取值
    public static <T> T get() {
        return (T) THREAD_LOCAL.get();
    }

    //存储键值对
    public static void set(Object o) {
        THREAD_LOCAL.set(o);
    }

    //清除ThreadLocal防止内存泄漏
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
