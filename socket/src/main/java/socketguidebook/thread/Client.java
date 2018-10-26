package socketguidebook.thread;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by xh.zhi on 2018-10-26.
 */
public class Client {
    public static void main(String[] args) throws IOException {

        for (int i=0;i<5;i++){
            final int flag = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new Client().test(flag);
                }
            }).start();
        }
    }

    public void test(int i) {
        Socket socket = null;
        try {
            socket = new Socket("localhost",9091);
            OutputStream out = socket.getOutputStream();
            out.write(("hello，我是张三_"+i).getBytes());

            out.close();
            socket.close();
            System.out.println("完成"+i);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
