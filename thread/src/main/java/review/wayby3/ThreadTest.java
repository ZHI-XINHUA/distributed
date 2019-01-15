package review.wayby3;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ThreadTest {
    public static void main(String[] args) {
        //方式一：继承Thread，重写run方法
        ThreadWay way = new ThreadWay();
        way.start();

        //方式二：实现Runnable接口
        RunableWay way2 = new RunableWay();
        new Thread(way2).start();

        //方式三：通过Callable和FutureTask创建线程
        //创建异步任务
        FutureTask<String> way3 = new FutureTask(new FutureTaskWay());
        //启动线程
        new Thread(way3).start();
        //等待任务执行完成，返回结果
        try {
           String result =  way3.get();
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
