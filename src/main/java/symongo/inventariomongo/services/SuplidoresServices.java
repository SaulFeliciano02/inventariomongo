package symongo.inventariomongo.services;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Service;
import symongo.inventariomongo.connection.MongoConnect;
import symongo.inventariomongo.entities.OrdenCompra;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuplidoresServices {
    private MongoCollection<Document> suplidores = MongoConnect.database.getCollection("suplidores");
    private MongoCollection<Document> ordenesCompra = MongoConnect.database.getCollection("ordenesCompra");

    public OrdenCompra getOrdenSuplidor(int codigoArticulo, String fechaEntrega, int montoTotal, int tiempoEntrega){
        List<Document> parametrosAggregate = new ArrayList<>();

        //MATCH
        parametrosAggregate.add(new Document("$match", new Document("codigoArticulo", codigoArticulo)
                .append("tiempoEntrega", tiempoEntrega)));

        //SORT
        parametrosAggregate.add(new Document("$sort", new Document("precioCompra", 1)));

        //LIMIT
        parametrosAggregate.add(new Document("$limit", 1));

        //PROJECT
        BasicDBList multParameters = new BasicDBList();
        multParameters.add("$precioCompra");
        multParameters.add(montoTotal);

        Document projectParameters = new Document("_id", 0).append("codigoSuplidor", "$codigoSuplidor")
                .append("tiempoEntrega", "$tiempoEntrega").append("precioCompra", "$precioCompra")
                .append("precioOrden", new BasicDBObject("$multiply", multParameters));

        parametrosAggregate.add(new Document("$project", projectParameters));

        AggregateIterable<Document> resultado = suplidores.aggregate(parametrosAggregate);

        OrdenCompra ordenCompra = null;
        for(Document document : resultado){
            ordenCompra.setCodigoOrdenCompra((int) ordenesCompra.countDocuments() + 1);
            ordenCompra.setCodigoSuplidor((int) Double.parseDouble(document.get("codigoSuplidor").toString()));
            ordenCompra.setCodigoArticulo(codigoArticulo);
            ordenCompra.setFechaOrden(fechaEntrega);
            ordenCompra.setMontoTotal(montoTotal);
            ordenCompra.setPrecioSuplidor((int) Double.parseDouble(document.get("precioCompra").toString()));
            ordenCompra.setPrecioCompra((int) Double.parseDouble(document.get("precioOrden").toString()));

            System.out.println(document);
        }
        return ordenCompra;
    }
}
