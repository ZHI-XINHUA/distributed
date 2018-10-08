package nio.buffer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

/**
 * Created by xh.zhi on 2018-10-8.
 */
public class ByteBufferTest {
    public static void main(String[] args) throws Exception{

        //allocateDirectTest1();

        //compareDirectTest();

        //compareNoDirectTest();

        warpTest();
    }

    private static void warpTest() {
        byte[] bytes = new byte[]{1,2,3,4,5,6,7,8};
        ByteBuffer buffer1 = ByteBuffer.wrap(bytes);
        ByteBuffer buffer2 = ByteBuffer.wrap(bytes,2,4);

        System.out.println(buffer1);
        System.out.println(buffer2);

        while (buffer2.hasRemaining()){
            System.out.print( buffer2.get()+" ");
        }



    }

    private static void compareDirectTest() throws InterruptedException {
        int num = 1300000000;
        long startTime = System.currentTimeMillis();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(num);
        for(int i=0;i<num;i++){
            byteBuffer.put((byte)1);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);

    }

    private static void compareNoDirectTest() throws InterruptedException {
        int num = 1300000000;
        long startTime = System.currentTimeMillis();
        ByteBuffer byteBuffer = ByteBuffer.allocate(num);
        for(int i=0;i<num;i++){
            byteBuffer.put((byte)1);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);

    }

    private static void allocateDirectTest() throws InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("===========A========");

        ByteBuffer buffer = ByteBuffer.allocateDirect(1500000000);

        System.out.println("============B=========");

        System.out.println("MAX_VALUE-->"+Integer.MAX_VALUE);
        byte[] bytes = new byte[]{1};
        for (int i=0;i<1500000000;i++){
            buffer.put(bytes);
        }
        System.out.println("=====put end ======");

        Thread.sleep(6000);

        //利用反射获取cleaner方法
        Method cleanerMethod = buffer.getClass().getMethod("cleaner");
        cleanerMethod.setAccessible(true);

        //调用方法，获取clean方法
        Object returnResult = cleanerMethod.invoke(buffer);
        Method cleanMethod = returnResult.getClass().getMethod("clean");
        cleanMethod.setAccessible(true);
        //回收“直接缓冲区”所占用的内存
        cleanMethod.invoke(returnResult);

        System.out.println("==============");
    }

    private static void allocateDirectTest1() throws InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("===========A========");

        ByteBuffer buffer = ByteBuffer.allocateDirect(1500000000);

        System.out.println("============B=========");

        System.out.println("MAX_VALUE-->"+Integer.MAX_VALUE);
        byte[] bytes = new byte[]{1};
        for (int i=0;i<1500000000;i++){
            buffer.put(bytes);
        }
        System.out.println("=====put end ======");

        //程序多次运行后，一直在耗费内存；进程结束后，也不会马上回收内存，而是会在某一个时机除非GC垃圾回收器进行内存的回收
    }


}
