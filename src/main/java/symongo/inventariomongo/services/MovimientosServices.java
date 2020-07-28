package symongo.inventariomongo.services;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import symongo.inventariomongo.connection.MongoConnect;
import symongo.inventariomongo.entities.Movimiento;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovimientosServices {
    private MongoCollection<Document> movimientos = MongoConnect.database.getCollection("movimientos");

    public void guardarMovimiento(int codigoArticulo, int cantidad, String tipoMovimiento){
        String today =  LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Document movimiento = new Document();
        movimiento.put("codigoMovimiento", (int) movimientos.countDocuments() + 1);
        movimiento.put("tipoMovimiento", tipoMovimiento);
        movimiento.put("codigoArticulo", codigoArticulo);
        movimiento.put("cantidad", cantidad);
        movimiento.put("fecha", today);

        movimientos.insertOne(movimiento);


    }

    public ArrayList<Movimiento> buscarMovimientos(String tipoMovimiento, int codigoArticulo, String fecha){
        ArrayList<Movimiento> listaMovimientos = new ArrayList<>();
        if(!tipoMovimiento.equalsIgnoreCase("") || codigoArticulo >= 0 || !fecha.equalsIgnoreCase("")){
            List<Document> parametrosAggregate = new ArrayList<>();
            //MATCHES
            Document matchParameters = new Document();
            if(!tipoMovimiento.equalsIgnoreCase("")){
                matchParameters.append("tipoMovimiento", tipoMovimiento);
            }
            if(codigoArticulo >= 0){
                matchParameters.append("codigoArticulo", codigoArticulo);
            }
            if(!fecha.equalsIgnoreCase("")){
                matchParameters.append("fecha", fecha);
            }

            parametrosAggregate.add(new Document("$match", matchParameters));

            AggregateIterable<Document> resultado = movimientos.aggregate(parametrosAggregate);

            for(Document document : resultado){
                int codigoMovimientoResult = document.getInteger("codigoMovimiento");
                String tipoMovimientoResult = document.getString("tipoMovimiento");
                int codigoArticuloResult = document.getInteger("codigoArticulo");
                int cantidadResult = document.getInteger("cantidad");
                String fechaResult = document.getString("fecha");
                listaMovimientos.add(new Movimiento(codigoMovimientoResult, tipoMovimientoResult, codigoArticuloResult, cantidadResult, fechaResult));
            }
        }
        return listaMovimientos;


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
