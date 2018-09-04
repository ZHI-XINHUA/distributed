package order;

import java.util.concurrent.TimeUnit;

public class OrderlinessDemo {

    private volatile  static  OrderlinessDemo orderlinessDemo;

    private OrderlinessDemo(){
        super();
    }

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
        for (int i=0;i<1000;i++){
            new Thread(
                    () -> {
                        OrderlinessDemo orderlinessDemo = OrderlinessDemo.getInstance();
                        System.out.println(orderlinessDemo);
                    }
            ).start();
        }

        TimeUnit.SECONDS.sleep(3);
    }
}
