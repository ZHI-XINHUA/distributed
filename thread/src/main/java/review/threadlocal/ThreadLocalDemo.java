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

    /**使用ThreadLocal包装SimpleDateFormat**/
    private static final ThreadLocal<SimpleDateFormat>  sdfLocal = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static void main(String[] args) {

        // simpleDateFormatTest();

       simpleDateFormatThreadLocalTest();
    }

    /**
     * 使用Threadlocal来解决SimpleDateFormat的多线程安全问题
     */
    public static void simpleDateFormatThreadLocalTest(){
        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i=1;i<100;i++){
            es.execute(()->{
                try {
                    SimpleDateFormat sdf1 = sdfLocal.get();
                    Date date = sdf1.parse("2018-10-10 12:10:09");

                    //如果使用线程池，那就意味着当前线程未必会退出（比如固定大小的线程池，线程总会存在）。如果这样，将一些大的对象
                    //设置到ThreadLocal中（它实际保存在线程持有的threadLocals Map中），可能会使系统出现内存泄露。（设置对象到ThreadLocal中，但是不清理它，在使用后，这个对象不再有用，
                    // 但是它却无法被收回）
                    sdfLocal.remove();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
        es.shutdown();
    }

    /**
     * SimpleDateFormat!parse() 方法并发线程安全，所以多线程环境下会报错
     */
    public static void simpleDateFormatTest(){
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
