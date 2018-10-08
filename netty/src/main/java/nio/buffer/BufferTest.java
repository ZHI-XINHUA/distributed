package nio.buffer;
import java.nio.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xh.zhi on 2018-10-8.
 */
public class BufferTest {
    public static void main(String[] args) {
        //capacityTest();
        //limitTest();

        //positionTest();

        //markTest();

        directTest();

        //clearTest();

        //listToTest();
    }

    private static void listToTest() {
       ByteBuffer buffer1 = ByteBuffer.wrap(new byte[]{'a','b','c'});
       ByteBuffer buffer2 = ByteBuffer.wrap(new byte[]{'x','y','z'});
       ByteBuffer buffer3 = ByteBuffer.wrap(new byte[]{'1','2','3'});

       List<ByteBuffer> list = new ArrayList<ByteBuffer>();
       list.add(buffer1);
       list.add(buffer2);
       list.add(buffer3);

       ByteBuffer[] byteBuffers = new ByteBuffer[list.size()];
       list.toArray(byteBuffers);

        System.out.println(byteBuffers.length);

        for (ByteBuffer byteBuffer:byteBuffers){

            while (byteBuffer.hasRemaining()){
                System.out.print( (char) byteBuffer.get());
            }
            System.out.println();

        }
    }

    private static void clearTest() {
        char[] chars = new char[]{'a','b','c','d','e'};
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        System.out.println("position:"+charBuffer.position() +" limit:"+charBuffer.limit() +" capacity:"+ charBuffer.capacity());

        charBuffer.position(2);
        charBuffer.limit(4);
        charBuffer.mark();
        System.out.println("position:"+charBuffer.position() +" limit:"+charBuffer.limit() +" capacity:"+ charBuffer.capacity());
        System.out.println("hasRemaining:"+charBuffer.hasRemaining() +" arrayOffset:"+charBuffer.arrayOffset());
        System.out.println(charBuffer.charAt(1));

        //claer后并没有清除数据，只是
        // position = 0;
        //limit = capacity;
        //mark = -1;
        charBuffer.clear();
        System.out.println("position:"+charBuffer.position() +" limit:"+charBuffer.limit() +" capacity:"+ charBuffer.capacity());
        System.out.println(charBuffer.charAt(1));



    }

    private static void directTest() {
        //创建非直接缓冲区
        ByteBuffer noditectBuffer = ByteBuffer.allocate(8);
        System.out.println(noditectBuffer.isDirect()+" "+noditectBuffer.hasArray()+"  "+noditectBuffer);

        //创建直接缓冲区
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(8);
        System.out.println(directBuffer.isDirect()+" "+directBuffer.hasArray()+"  "+directBuffer);

        byte[] bytes = new byte[]{1,2};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        System.out.println(byteBuffer.isDirect()+" "+byteBuffer.hasArray()+"  "+byteBuffer);

    }


    /**
     * capacity
     */

    private static void capacityTest(){
        byte[] bytes = new byte[]{1,2,3};
        short[] shorts = new short[4];
        char[] chars = new char[]{1,2,3};
        int[] ints = new int[]{1,2,3};
        float[] floats = new float[]{1,2,3};
        long[] longs = new long[]{1,2,3};
        double[] doubles = new double[]{1,2,3};

        ByteBuffer.allocate(8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        ShortBuffer shortBuffer = ShortBuffer.wrap(shorts);
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        IntBuffer intBuffer = IntBuffer.wrap(ints,0,1);
        FloatBuffer floatBuffer = FloatBuffer.wrap(floats);
        LongBuffer longBuffer = LongBuffer.wrap(longs);
        DoubleBuffer doubleBuffer = DoubleBuffer.wrap(doubles);

        System.out.println(byteBuffer.getClass().getName() +" > "+byteBuffer.capacity() +" isReadOnly:"+byteBuffer.isReadOnly());
        System.out.println(shortBuffer.getClass().getName()+" > "+shortBuffer.capacity());
        System.out.println(charBuffer.getClass().getName()+" > "+charBuffer.capacity());
        System.out.println(intBuffer.getClass().getName()+" > "+intBuffer.capacity());
        System.out.println(floatBuffer.getClass().getName()+" > "+floatBuffer.capacity());
        System.out.println(longBuffer.getClass().getName()+" > "+longBuffer.capacity());
        System.out.println(doubleBuffer.getClass().getName()+" > "+doubleBuffer.capacity());
    }


    /**
     * limit
     */
    private static void limitTest(){
        char[] chars = new char[]{'a','b','c','d','e'};
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        System.out.println("capacity:"+charBuffer.capacity()+" limit:"+charBuffer.limit());

        charBuffer.limit(3);
        System.out.println("capacity:"+charBuffer.capacity()+" limit:"+charBuffer.limit());
        charBuffer.put(0,'f');
        charBuffer.put(1,'g');
        charBuffer.put(2,'h');

        charBuffer.position(4);
        System.out.println("capacity:"+charBuffer.capacity()+" limit:"+charBuffer.limit());

        //index 3之后的会报错，limit 3  此位置之后是不能读不能写的索引
        charBuffer.put(3,'i');

    }


    /**
     * position
     */
    private static void positionTest(){
        char[] chars = new char[]{'a','b','c','d','e'};
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        System.out.println("capacity:"+charBuffer.capacity()+" limit:"+charBuffer.limit()+" position:"+charBuffer.position());
        //剩余空间  limit-position
        System.out.println("remaining:"+charBuffer.remaining());

        //设置position=2, 缓冲区位置
        charBuffer.position(5);
        charBuffer.put("j");
        System.out.println("capacity:"+charBuffer.capacity()+" limit:"+charBuffer.limit()+" position:"+charBuffer.position());

        char[] chars1 = charBuffer.array();
        for (char c : chars1){
            System.out.print(c+" ");
        }

        //剩余空间  limit-position
        System.out.println("remaining:"+charBuffer.remaining());


    }


    private static void markTest(){
        char[] chars = new char[]{'a','b','c','d','e','f'};
        CharBuffer charBuffer = CharBuffer.wrap(chars);

        charBuffer.position(1);
        // 索引为1的地方做标志
        charBuffer.mark();
        System.out.println(charBuffer.position());


        charBuffer.position(3);
        // 索引为3的地方做标志
        charBuffer.mark();
        System.out.println(charBuffer.position());

        charBuffer.position(5);
        System.out.println(charBuffer.position());

        //重置位置，位置回到上一个mark的位置
        charBuffer.reset();
        System.out.println(charBuffer.position());

        charBuffer.reset();
        charBuffer.reset();
        System.out.println(charBuffer.position());
    }
}
