package aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by xh.zhi on 2018-9-19.
 * 接受通知消息处理类，实现CompletionHandler处理类
 */
public class ServerCompletionHandler implements CompletionHandler<AsynchronousSocketChannel,AioServer>{
    /**
     * 成功处理方法
     * @param asc
     * @param attachment
     */
    @Override
    public void completed(AsynchronousSocketChannel asc, AioServer attachment) {
        //当有下一个客户端接入的时候 直接调用Server的accept方法，这样反复执行下去，保证多个客户端都可以阻塞
        attachment.assc.accept(attachment,this);

        // 消息处理
        read(asc);
    }

    /**
     * 失败处理方法
     * @param exc
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, AioServer attachment) {
        System.out.println(" handler failed");
        exc.printStackTrace();
    }

    /**
     * 读取消息
     * @param asc
     */
    private void read(final AsynchronousSocketChannel asc){
        //创建缓存区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        asc.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer resultSize, ByteBuffer attachment) {
                //进行读取之后,重置标识位
                attachment.flip();
                //获得读取的字节数
                System.out.println("Server -> " + "收到客户端的数据长度为:" + resultSize);
                //获取读取的数据
                String resultData = new String(attachment.array()).trim();
                System.out.println("Server -> " + "收到客户端的数据信息为:" + resultData);

                //服务端响应数据
                String reponseText = "我是服务端，已收到您的消息："+resultData;
                write(asc,reponseText);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
    }

    /**
     * 服务器响应数据给客户端
     * @param asc
     */
    private void write(final AsynchronousSocketChannel asc,String responseText){

        //1、建立写缓存区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //2、写入数据
        buffer.put(responseText.getBytes());
        //3、复位
        buffer.flip();
        //4、写入管道
        asc.write(buffer);
    }
}
