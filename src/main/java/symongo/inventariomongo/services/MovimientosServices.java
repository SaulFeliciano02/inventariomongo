package symongo.inventariomongo.services;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Service;
import symongo.inventariomongo.connection.MongoConnect;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovimientosServices {
    private MongoCollection<Document> movimientos = MongoConnect.database.getCollection("movimientos");

    public void guardarMovimiento(int codigoArticulo, int codigoAlmacen){

    }

    public int ventaDiaria(int codigoArticulo){
        List<Document> parametrosAggregate = new ArrayList<>();

        //MATCH PARA SOLO OBTENER LOS MOVIMIENTOS DE ENTRADA
        Document matchParameters = new Document("tipoMovimiento", "SALIDA").append("codigoArticulo", codigoArticulo);
        parametrosAggregate.add(new Document("$match", matchParameters));

        //GROUP ARTICULOS POR CODIGO Y FECHA
        Document firstGroupParameters = new Document("_id", new Document("codigoArticulo", "$codigoArticulo").append("fecha", "$fecha"));
        Document sumArticulosVendidos =  new Document("$sum", "$cantidad");
        parametrosAggregate.add(new Document("$group", firstGroupParameters.append("cantidadArticulosVendidos", sumArticulosVendidos)));

        //PROJECT DATOS DEL GROUP
        Document projectParameters = new Document("_id", 0).
                append("codigoArticulo", "$_id.codigoArticulo").
                append("fecha", "$_id.fecha").
                append("cantidadTotal", "$cantidadArticulosVendidos");
        parametrosAggregate.add(new Document("$project", projectParameters));

        //GROUP DE ARTICULOS PARA OBTENER PROMEDIO
        Document secondGroupParameters = new Document("_id", new Document("codigoArticulo", "$codigoArticulo"));
        Document averageArticulos = new Document("$avg", "$cantidadTotal");
        parametrosAggregate.add(new Document("$group", secondGroupParameters.append("promedioVenta", averageArticulos)));

        //TERCER PROJECT PARA REDONDEAR EL NUMERO
        Document groupParameters = new Document("codigoArticulo", "$_id.codigoArticulo").
                append("promedioVenta", new Document("$floor", "$promedioVenta"));
        parametrosAggregate.add(new Document("$project", groupParameters));

        AggregateIterable<Document> resultado = movimientos.aggregate(parametrosAggregate);

        int promedioVenta = 0;
        for(Document document : resultado){
            promedioVenta = (int) Double.parseDouble(document.get("promedioVenta").toString());
        }
        System.out.println(promedioVenta);

        return promedioVenta;
    }
}
