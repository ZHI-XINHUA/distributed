package socketguidebook.verifyAccept;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by xh.zhi on 2018-10-19
 *  测试try 关闭资源.
 */
public class CloseTest implements Closeable {
    public void operation(){
        System.out.println("CloseTest>operation()");
    }
    @Override
    public void close() throws IOException {
        System.out.println("调用了CloseTest>close()");
    }
}

class CloseTest1 implements Closeable {
    public void operation(){
        System.out.println("CloseTest1>operation()");
    }
    @Override
    public void close() throws IOException {
        System.out.println("调用了CloseTest1>close()");
    }
}

class MainTest{
    public static void main(String[] args) {
        try {
            try(
                    CloseTest closeTest = new CloseTest();
                     CloseTest1 closeTest1 = new CloseTest1();
            ){
                closeTest.operation();
                closeTest1.operation();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
