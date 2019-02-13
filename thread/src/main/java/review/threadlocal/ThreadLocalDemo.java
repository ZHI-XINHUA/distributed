package review.threadlocal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ThreadLocal
 */
public class ThreadLocalDemo {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i=1;i<1000;i++){
            es.execute(()->{
                try {
                    Date date = sdf.parse("2018-10-10 12:10:09");
                } catch (ParseException e) {
                    //Exception in thread "pool-1-thread-3" java.lang.NumberFormatException: multiple points
                    //Exception in thread "pool-1-thread-5" java.lang.NumberFormatException: For input string: ""
                    e.printStackTrace();
                }
            });
        }
        es.shutdown();

    }

}
