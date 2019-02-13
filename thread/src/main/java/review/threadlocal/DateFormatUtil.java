package review.threadlocal;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 使用ThreadLocal解决DateFormat并发问题
 */
public class DateFormatUtil {

    /**使用ThreadLocal包装SimpleDateFormat**/
    private static ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

    };

    public static Date parse(String source) throws ParseException {
        return df.get().parse(source);
    }

    public static String format(Date date){
        return df.get().format(date);
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(10);
        for (int i=1;i<100;i++){
            es.execute(()->{
                System.out.println(Thread.currentThread().getName()+">"+ DateFormatUtil.format(new Date()));
            });
        }
        es.shutdown();
    }


}
