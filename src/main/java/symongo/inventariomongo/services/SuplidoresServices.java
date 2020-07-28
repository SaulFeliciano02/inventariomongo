package symongo.inventariomongo.services;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.stereotype.Service;
import symongo.inventariomongo.connection.MongoConnect;
import symongo.inventariomongo.entities.Articulo;
import symongo.inventariomongo.entities.OrdenCompra;
import symongo.inventariomongo.entities.Suplidor;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuplidoresServices {
    private MongoCollection<Document> suplidores = MongoConnect.database.getCollection("suplidores");
    private MongoCollection<Document> ordenesCompra = MongoConnect.database.getCollection("ordenesCompra");

    public void guardarSuplidor(int codigoArticulo, int tiempoEntrega, int precioCompra){
        Document suplidor = new Document();
        suplidor.put("codigoArticulo", codigoArticulo);
        suplidor.put("codigoSuplidor", (int) suplidores.countDocuments() + 1);
        suplidor.put("tiempoEntrega", tiempoEntrega);
        suplidor.put("precioCompra", precioCompra);

        suplidores.insertOne(suplidor);
    }

    public ArrayList<Suplidor> getSuplidores(){
        MongoCursor<Document> suplidoresCursor = suplidores.find().iterator();
        Document result = new Document();
        ArrayList<Suplidor> suplidoresLista = new ArrayList<>();
        try{
            while(suplidoresCursor.hasNext()){
                result = suplidoresCursor.next();
                int codigoArticulo = result.getInteger("codigoArticulo");
                int codigoSuplidor = result.getInteger("codigoSuplidor");
                int tiempoEntrega = result.getInteger("tiempoEntrega");
                int precioCompra = result.getInteger("precioCompra");
                suplidoresLista.add(new Suplidor(codigoArticulo, codigoSuplidor, tiempoEntrega, precioCompra));
            }
        } finally {
            suplidoresCursor.close();
        }
        return suplidoresLista;
    }

    public OrdenCompra getOrdenSuplidor(int codigoArticulo, String fechaEntrega, int montoTotal, int tiempoEntrega){
        List<Document> parametrosAggregate = new ArrayList<>();

        //MATCH
        parametrosAggregate.add(new Document("$match", new Document("codigoArticulo", codigoArticulo)
                .append("tiempoEntrega", new Document("$lte", tiempoEntrega))));

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

        OrdenCompra ordenCompra = new OrdenCompra();
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
