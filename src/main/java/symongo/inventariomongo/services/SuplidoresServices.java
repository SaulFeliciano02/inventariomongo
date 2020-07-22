package symongo.inventariomongo.services;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Service;
import symongo.inventariomongo.connection.MongoConnect;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuplidoresServices {
    private MongoCollection<Document> suplidores = MongoConnect.database.getCollection("suplidores");

    public void getOrdenSuplidor(int codigoArticulo, int tiempoEntrega){
        List<Document> parametrosAggregate = new ArrayList<>();

        //MATCH
        parametrosAggregate.add(new Document("$match", new Document("codigoArticulo", codigoArticulo)
                .append("tiempoEntrega", tiempoEntrega)));

        //SORT
        parametrosAggregate.add(new Document("$sort", new Document("precioCompra", 1)));

        //LIMIT
        parametrosAggregate.add(new Document("$limit", 1));

        AggregateIterable<Document> resultado = suplidores.aggregate(parametrosAggregate);

        for(Document document : resultado){
            System.out.println(document);
        }
    }
}
