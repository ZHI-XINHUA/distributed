package review.threadlocal;

public class InheritableThreadLocalDemo {

    public static ThreadLocal<String> threadLocal = new InheritableThreadLocal<String>();

    public static void main(String[] args) {
        //主线程中设置值
        threadLocal.set("hello threadlocal");

        new Thread(()->{
            //子线程输出
            System.out.println("thread:"+threadLocal.get());
        }).start();

        //主线程输出
        System.out.println("main:"+threadLocal.get());
    }
}
