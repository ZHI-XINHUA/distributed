package zxh.forjava;


import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xh.zhi on 2018-12-29.
 */
public class MongoClientTest {
    /**mongodb host**/
    private final String host = "192.168.3.31";
    /**mongodb port**/
    private final int prot = 27017;

    static MongoClient server;

    public MongoClientTest(){
        // 连接到 mongodb 服务
        //server =  new MongoClient(host,prot);

        //如果是集群服务
        ServerAddress serverAddress1 = new ServerAddress("192.168.3.311",27017);
        ServerAddress serverAddress2 = new ServerAddress("192.168.3.326",27017);
        List<ServerAddress> addrs = new ArrayList<ServerAddress>();
        addrs.add(serverAddress1);
        addrs.add(serverAddress2);

        server = new MongoClient(addrs);

        //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
       /* MongoCredential credential = MongoCredential.createCredential("username","databaseName","password".toCharArray());
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        credentials.add(credential);

        server = new MongoClient(addrs,credentials);*/
    }



   public static void test(){
       MongoDatabase mongoDatabase = server.getDatabase("testdb");
       System.out.println(mongoDatabase.getName());
       MongoCollection<Document>  collection =  mongoDatabase.getCollection("person");
       System.out.println(collection.getDocumentClass());
       System.out.println("success");
   }


}
