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

        //warpTest();

        //putAndGetTest();

        //putAndGetTest1();

        //putAndGetTest2();

        //putAndGetTest3();

        //putAndGetTest4();

        //putAndGetTest5();

        sliceTest();

    }

    private static void sliceTest() {
        byte[] bytes = {1,2,3,4,5,6,7,8};
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte)11);
       // buffer.position(5);
        ByteBuffer buffer1 = buffer.slice();
        System.out.println(buffer);
        buffer1.put((byte) 9);
        System.out.println(buffer1);

        byte[] bytes1 = buffer1.array();
        for(int i=0;i<bytes1.length;i++){
            System.out.print(bytes1[i]+" ");
        }
    }

    private static void putAndGetTest5() {
        ByteBuffer buffer1 = ByteBuffer.allocate(30);
        buffer1.putDouble(1);
        buffer1.putChar((char)2);
        buffer1.putFloat(3);
        buffer1.putInt(4);
        buffer1.putLong(5);

        buffer1.rewind();

        while (buffer1.hasRemaining()){
            System.out.print(buffer1.get() +" ");
        }
    }

    private static void putAndGetTest4() {
        ByteBuffer buffer1 = ByteBuffer.wrap(new byte[]{1,2,3,4,5});
        ByteBuffer buffer2 = ByteBuffer.wrap(new byte[]{98,99,100});
        //buffer1.position(2);
        buffer1.put(buffer2);

        buffer1.rewind();
        while (buffer1.hasRemaining()){
            System.out.print(buffer1.get() +" ");
        }
    }

    private static void putAndGetTest3() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put(2,(byte) 1);
        System.out.println(byteBuffer);
        System.out.println(byteBuffer.get(2));
        System.out.println(byteBuffer);
    }

    private static void putAndGetTest2() {
        byte[] bytes1 = new byte[]{1,2,3,4,5,6,7,8};
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(bytes1);
        System.out.println(buffer);

        buffer.rewind();

        while (buffer.hasRemaining()){
            System.out.print(buffer.get() +" ");
        }

        System.out.println();
        System.out.println(buffer);

        buffer.rewind();

        byte[] bytes2 = new byte[buffer.capacity()];
        buffer.get(bytes2);

        for (int i=0;i<bytes2.length;i++){
            System.out.print(bytes2[i]+" ");
        }
        System.out.println();
        System.out.println(buffer);
    }

    private static void putAndGetTest1() {
        byte[] bytes1 = new byte[]{1,2,3,4,5,6,7,8};
        //byte[] bytes2 = new byte[]{98,99,100};

        ByteBuffer buffer1 = ByteBuffer.allocate(10);
        buffer1.put(bytes1,1,7);
        System.out.println(buffer1);

        byte[] bytesResult = buffer1.array();
        for (int i=0;i<bytesResult.length;i++){
            System.out.print(bytesResult[i]+" ");
        }


        buffer1.rewind();

        byte[] bytes2 = new byte[buffer1.capacity()];
        buffer1.get(bytes2,1,3);
        for (int i=0;i<bytes2.length;i++){
            System.out.print(bytes2[i]+" ");
        }


    }


    private static void putAndGetTest() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        System.out.println(buffer);

        buffer.put((byte) 1);
        System.out.println(buffer);

        buffer.put((byte)2);
        System.out.println(buffer);

        buffer.put((byte)3);
        System.out.println(buffer);

        System.out.println("================");
        //重置
        buffer.rewind();
        System.out.println(buffer);


        System.out.println(buffer.get());
        System.out.println(buffer);
        System.out.println(buffer.get());
        System.out.println(buffer);


        byte[] bytes = buffer.array();
        for (byte b :bytes){
            System.out.print(b+" ");
        }




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
