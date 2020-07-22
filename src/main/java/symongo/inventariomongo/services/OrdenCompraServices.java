package symongo.inventariomongo.services;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Service;
import symongo.inventariomongo.connection.MongoConnect;
import symongo.inventariomongo.entities.OrdenCompra;

@Service
public class OrdenCompraServices {
    private MongoCollection<Document> ordenesCompra = MongoConnect.database.getCollection("ordenesCompra");
    public void guardarOrden(OrdenCompra ordenCompra){
        Document ordenCompraDocument = new Document();
        ordenCompraDocument.put("codigoOrdenCompra", ordenCompra.getCodigoOrdenCompra());
        ordenCompraDocument.put("codigoSuplidor", ordenCompra.getCodigoSuplidor());
        ordenCompraDocument.put("fechaOrden", ordenCompra.getFechaOrden());
        ordenCompraDocument.put("montoTotal", ordenCompra.getMontoTotal());
        ordenCompraDocument.put("codigoArticulo", ordenCompra.getCodigoArticulo());
        ordenCompraDocument.put("precioSuplidor", ordenCompra.getPrecioSuplidor());
        ordenCompraDocument.put("precioCompra", ordenCompra.getPrecioCompra());

        ordenesCompra.insertOne(ordenCompraDocument);
    }

}
