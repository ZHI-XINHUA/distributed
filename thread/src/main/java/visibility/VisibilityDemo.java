package visibility;

import java.util.concurrent.TimeUnit;

/**
 * Created by xh.zhi on 2018-9-4.
 */
public class VisibilityDemo {
    private static boolean stopFlag = false;
    private static int count = 0;

    public static void main(String[] args) throws Exception {
        new Thread(
                () -> {
                  while(!stopFlag) {
                      count++;
                      System.out.println(count);
                  }
                }
        ).start();

        TimeUnit.MILLISECONDS.sleep(500);

        stopFlag=true;

        System.in.read();

    }
}
