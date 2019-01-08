package zxh.forjava;


import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

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
        // ====================连接到 mongodb 服务====================
        server =  new MongoClient(host,prot);

        //如果是集群服务
      /*  ServerAddress serverAddress1 = new ServerAddress("192.168.3.311",27017);
        ServerAddress serverAddress2 = new ServerAddress("192.168.3.326",27017);
        List<ServerAddress> addrs = new ArrayList<ServerAddress>();
        addrs.add(serverAddress1);
        addrs.add(serverAddress2);

        server = new MongoClient(addrs);*/

        //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
       /* MongoCredential credential = MongoCredential.createCredential("username","databaseName","password".toCharArray());
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        credentials.add(credential);

        server = new MongoClient(addrs,credentials);*/
    }

    public void documentOpertion(){
        //=========================获取数据库===================
        MongoDatabase database =  server.getDatabase("testdb");

        //========================创建集合=================
        //database.createCollection("testCollection");


        //=====================获取collection================
        MongoCollection<Document> collection = database.getCollection("testCollection");

        //=====================插入Document的数据================
        List<Document> addList = new ArrayList<>();
        for(int i=0;i<5;i++){
            Document document = new Document();
            document.append("title","mongodb_"+i)
                    .append("description", "database_"+i)
                    .append("likes", 100+i)
                    .append("by", "Fly_"+i);

            addList.add(document);
        }
        //插入Document
        collection.insertMany(addList);

        //=====================修改======================
        collection.updateMany(Filters.eq("title","mongodb_0"),new Document("$set",new Document("by","go_0").append("likes","200")));

        //=========================删除====================
        //删除符合条件的第一个文档
        collection.deleteOne(Filters.eq("title", "mongodb_1"));
        //删除所有符合条件的文档
        DeleteResult result = collection.deleteMany(Filters.eq("title","mongodb_1"));
        long deleteCount = result.getDeletedCount();
        System.out.println("删除的个数："+deleteCount);



        //==================查询=============
        //查询个数
        long row = collection.countDocuments();
        System.out.println("person 集合行数为："+row);

        //查询所有
        //FindIterable<Document>  allCocument = collection.find();
        FindIterable<Document>  allCocument = collection.find(Filters.gt("likes","102"));
        MongoCursor<Document> iterator =  allCocument.iterator();
        while (iterator.hasNext()){
            Document document =  iterator.next();

            System.out.println("_id="+document.get("_id")+" title="+document.get("title")+" description="+document.get("description")+" likes="+document.get("likes")+" by="+document.get("by"));
            //System.out.println(document);
        }
    }

}
