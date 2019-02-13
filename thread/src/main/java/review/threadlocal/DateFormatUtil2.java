package review.threadlocal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 方式二、
 */
public class DateFormatUtil2 {
    /**使用ThreadLocal包装SimpleDateFormat**/
    private static ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>();

    private static DateFormat getDateFormat(){
        DateFormat dfLocal = df.get();
        //如果定义变量的时候没有初始化，默认会是null
        if(dfLocal==null){
            //初始化变量
            dfLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return dfLocal;
    }

    public static Date parse(String srouce) throws ParseException {
        return getDateFormat().parse(srouce);
    }

    public static String format(Date date){
        return  getDateFormat().format(date);
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(10);
        for (int i=1;i<100;i++){
            es.execute(()->{
                System.out.println(Thread.currentThread().getName()+">"+ DateFormatUtil2.format(new Date()));
            });
        }
        es.shutdown();
    }
}
