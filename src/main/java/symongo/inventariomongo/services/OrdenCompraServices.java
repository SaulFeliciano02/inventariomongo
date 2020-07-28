package symongo.inventariomongo.services;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.stereotype.Service;
import symongo.inventariomongo.connection.MongoConnect;
import symongo.inventariomongo.entities.OrdenCompra;
import symongo.inventariomongo.entities.Suplidor;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrdenCompraServices {
    private MongoCollection<Document> ordenesCompra = MongoConnect.database.getCollection("ordenesCompra");

    public ArrayList<OrdenCompra> getOrdenes(){
        MongoCursor<Document> ordenesCursor = ordenesCompra.find().iterator();
        Document result = new Document();
        ArrayList<OrdenCompra> ordenesList = new ArrayList<>();
        try{
            while(ordenesCursor.hasNext()){
                result = ordenesCursor.next();
                int codigoOrdenCompra = result.getInteger("codigoOrdenCompra");
                int codigoSuplidor = result.getInteger("codigoSuplidor");
                String fechaOrden = result.getString("fechaOrden");
                int montoTotal = result.getInteger("montoTotal");
                int codigoArticulo = result.getInteger("codigoArticulo");
                int precioSuplidor = result.getInteger("precioSuplidor");
                int precioCompra = result.getInteger("precioCompra");
                ordenesList.add(new OrdenCompra(codigoOrdenCompra, codigoSuplidor, fechaOrden, montoTotal, codigoArticulo, precioSuplidor, precioCompra));
            }
        } finally {
            ordenesCursor.close();
        }
        return ordenesList;
    }

    public ArrayList<OrdenCompra> buscarOrdenes(int articulo, int suplidor, String fechaOrden){
        ArrayList<OrdenCompra> ordenes = new ArrayList<>();
        if(articulo != 0 || suplidor != 0 || !fechaOrden.equalsIgnoreCase("")){
            List<Document> parametrosAggregate = new ArrayList<>();

            //MATCHES
            Document matchParameters = new Document();
            if(articulo != 0){
                matchParameters.append("codigoArticulo", articulo);
            }
            if(suplidor != 0){
                matchParameters.append("codigoSuplidor", suplidor);
            }
            if(!fechaOrden.equalsIgnoreCase("")){
                matchParameters.append("fechaOrden", fechaOrden);
            }

            parametrosAggregate.add(new Document("$match", matchParameters));

            AggregateIterable<Document> resultado = ordenesCompra.aggregate(parametrosAggregate);

            for(Document document : resultado){
                try{
                    OrdenCompra orden = new OrdenCompra();
                    orden.setCodigoOrdenCompra((int) Double.parseDouble(document.get("codigoOrdenCompra").toString()));
                    orden.setCodigoSuplidor((int) Double.parseDouble(document.get("codigoSuplidor").toString()));
                    orden.setCodigoArticulo((int) Double.parseDouble(document.get("codigoArticulo").toString()));
                    orden.setFechaOrden(document.get("fechaOrden").toString());
                    orden.setMontoTotal((int) Double.parseDouble(document.get("montoTotal").toString()));
                    orden.setPrecioSuplidor((int) Double.parseDouble(document.get("precioSuplidor").toString()));
                    orden.setPrecioCompra((int) Double.parseDouble(document.get("precioCompra").toString()));
                    ordenes.add(orden);
                }catch (NullPointerException e){

                }
            }
        }

        return ordenes;
    }

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
