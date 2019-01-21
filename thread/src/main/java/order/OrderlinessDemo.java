package order;

import java.util.concurrent.TimeUnit;

/**
 * 有序性
 */
public class OrderlinessDemo {

    private /**volatile**/  static  OrderlinessDemo orderlinessDemo;

    private OrderlinessDemo(){
        super();
    }

    public void operation(){

    }

    /**
     * 有序性问题：可能发生指令重排
     * @return
     */
    public static OrderlinessDemo getInstance(){
        if (orderlinessDemo==null){
            synchronized (OrderlinessDemo.class){
                if (orderlinessDemo==null){
                    orderlinessDemo = new OrderlinessDemo();
                }
            }
        }
        return orderlinessDemo;
    }


    public static void main(String[] args) throws InterruptedException {
        OrderlinessDemo orderlinessDemo1 = OrderlinessDemo.getInstance();
        for (int i=0;i<100000;i++){
            new Thread(
                    () -> {
                        OrderlinessDemo orderlinessDemo = OrderlinessDemo.getInstance();
                        //如果发生指令重排，没有正在获取对象，调用对象方法会出错
                        orderlinessDemo.operation();

                    }
            ).start();
        }

        TimeUnit.SECONDS.sleep(3);
    }
}
