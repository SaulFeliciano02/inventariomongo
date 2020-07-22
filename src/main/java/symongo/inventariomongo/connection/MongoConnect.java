package symongo.inventariomongo.connection;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnect {
    public static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    public static MongoDatabase database = mongoClient.getDatabase("test-ordenes");
}
