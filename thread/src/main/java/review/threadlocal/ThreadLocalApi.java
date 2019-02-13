package review.threadlocal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalApi {
    //ThreadLocal本地变量
   static ThreadLocal<Integer> countLocal = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

   //共享变量
   static Integer count = 0;


    public static int add(){
       return count++;
    }

    public static int addLocal(){
        Integer i = countLocal.get()+1;
        countLocal.set(i);
        return countLocal.get();
    }

    public static void main(String[] args) throws InterruptedException {

        ExecutorService es = Executors.newFixedThreadPool(2);
        System.out.println("共享变量++操作：");
        for (int i=1;i<5;i++){
            es.execute(()->{
                System.out.println(Thread.currentThread().getName()+">"+ ThreadLocalApi.add());
            });
        }

        Thread.sleep(1000);

        System.out.println("本地变量++操作：");
        for (int i=1;i<5;i++){
            es.execute(()->{
                System.out.println(Thread.currentThread().getName()+">"+ ThreadLocalApi.addLocal());
            });
        }
        es.shutdown();
    }
}
