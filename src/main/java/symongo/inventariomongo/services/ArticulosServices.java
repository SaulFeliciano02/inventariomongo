package symongo.inventariomongo.services;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import symongo.inventariomongo.connection.MongoConnect;
import symongo.inventariomongo.entities.InfoAlmacen;
import symongo.inventariomongo.entities.OrdenCompra;

import javax.print.Doc;
import javax.websocket.RemoteEndpoint;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.DoubleConsumer;

import static com.mongodb.client.model.Filters.eq;

@Service
public class ArticulosServices {
    @Autowired SuplidoresServices suplidoresServices;

    private MongoCollection<Document> articulos = MongoConnect.database.getCollection("articulos");

    public void guardarArticulo(String codigoArticulo, String descripcion, String unidadCompra, int totalGeneral){
        Document articulo = new Document();
        articulo.put("codigoArticulo", codigoArticulo);
        articulo.put("descripcion", descripcion);
        articulo.put("unidadCompra", unidadCompra);
        articulo.put("totalGeneral", totalGeneral);


        articulos.insertOne(articulo);
    }

    public long validateArticulo(int codigoArticulo){
        Document query = new Document("codigoArticulo", new Document("$eq", codigoArticulo));
        long result = articulos.countDocuments(query);
        return result;
    }

    public OrdenCompra obtenerFechaYCantidad(int codigoArticulo, String fechaPedida, int usoDiario, int cantidadMinima){
        List<Document> parametrosAggregate = new ArrayList<>();
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString().replace("-", "/");

        //MATCH PARA FILTRAR POR ARTICULO
        Document firstMatchParameters = new Document("codigoArticulo", codigoArticulo);
        parametrosAggregate.add(new Document("$match", firstMatchParameters));

        //PROJECT PARA TRABAJAR CON LOS DATOS NECESARIOS
        Document projectParameters = new Document("codigoArticulo", "$codigoArticulo").append("balanceActual", "$totalGeneral");
        parametrosAggregate.add(new Document("$project", projectParameters));

        //SETEANDO VALOR DE DIAS DISPONIBLES
        BasicDBList subParameters = new BasicDBList();
        subParameters.add(new Document("$dateFromString", new Document("dateString", fechaPedida).append("format", "%Y/%m/%d")));
        subParameters.add(new Document("$dateFromString", new Document("dateString", today).append("format", "%Y/%m/%d")));

        BasicDBList MultParameters = new BasicDBList();
        MultParameters.add(new BasicDBObject("$subtract", subParameters));
        MultParameters.add(1000 * 60 * 60 * 24);

        /**MultParameters deberia ser divParameters, oops**/
        Document diasDisponibles = new Document("diasDisponibles", new BasicDBObject("$divide", MultParameters));
        parametrosAggregate.add(new Document("$set", diasDisponibles));

        //SETEANDO VALOR DEL GASTO DIARIO
        MultParameters = new BasicDBList();
        MultParameters.add("$diasDisponibles");
        MultParameters.add(usoDiario);

        Document gastoEnTiempo = new Document("gastoEnTiempo", new BasicDBObject("$multiply", MultParameters));
        parametrosAggregate.add(new Document("$set", gastoEnTiempo));

        //SETEANDO EL VALOR DE LA CANTIDAD RESTANTE PARA EL DIA DE ENTREGA
        subParameters = new BasicDBList();
        subParameters.add("$balanceActual");
        subParameters.add("$gastoEnTiempo");
        Document restantes = new Document("restantes", new BasicDBObject("$subtract", subParameters));
        parametrosAggregate.add(new Document("$set", restantes));

        //SEGUNDO PROJECT PARA CALCULAR LA FECHA DE ENTREGA
        Document switch_restantesGTE = new Document("case", getSwitchGTEParameters())
                .append("then", fechaPedida);

        BasicDBList divParameters = new BasicDBList();
        divParameters.add("$balanceActual");
        divParameters.add(usoDiario);

        MultParameters = new BasicDBList();
        MultParameters.add(new Document("$floor", new BasicDBObject("$divide", divParameters)));
        MultParameters.add(24); MultParameters.add(60); MultParameters.add(60000);

        BasicDBList dateAddParemeters = new BasicDBList();
        dateAddParemeters.add(new Document("$dateFromString",
                new Document("dateString", today).append("format", "%Y/%m/%d")));
        dateAddParemeters.add(new BasicDBObject("$multiply", MultParameters));
        Document switch_restantesLT_thenParameters = new Document("format", "%Y/%m/%d")
                        .append("date", new BasicDBObject("$add", dateAddParemeters));

        Document switch_restantesLT = new Document("case", getSwitchLTParameters())
                .append("then", new Document("$dateToString", switch_restantesLT_thenParameters));


        BasicDBList branchesParameters = new BasicDBList();
        branchesParameters.add(switch_restantesGTE);
        branchesParameters.add(switch_restantesLT);

        Document switchStatement = new Document("$switch", new BasicDBObject("branches", branchesParameters));

        Document secondProjectParameters = new Document("codigoArticulo", "$codigoArticulo")
                .append("balanceActual", "$balanceActual")
                .append("diasDisponibles", "$diasDisponibles")
                .append("gastoEnTiempo", "$gastoEnTiempo")
                .append("restantes", "$restantes")
                .append("fechaEntrega", switchStatement);
        parametrosAggregate.add(new Document("$project", secondProjectParameters));

        //EL NUEVO SET PARA DIAS DISPONIBLES
        BasicDBList notEqual = new BasicDBList();
        notEqual.add("$fechaEntrega");
        notEqual.add(fechaPedida);

        subParameters = new BasicDBList();
        subParameters.add(new Document("$dateFromString", new Document("dateString", "$fechaEntrega").append("format", "%Y/%m/%d")));
        subParameters.add(new Document("$dateFromString", new Document("dateString", today).append("format", "%Y/%m/%d")));

        divParameters = new BasicDBList();
        divParameters.add(new BasicDBObject("$subtract", subParameters));
        divParameters.add(1000 * 60 * 60 * 24);

        BasicDBList diasDisponiblesCond = new BasicDBList();
        diasDisponiblesCond.add(new BasicDBObject("$ne",notEqual));
        diasDisponiblesCond.add(new BasicDBObject("$divide", divParameters));
        diasDisponiblesCond.add("$diasDisponibles");

        parametrosAggregate.add(new Document("$set", new Document("diasDisponibles", new BasicDBObject("$cond", diasDisponiblesCond))));

        //EL NUEVO SET PARA EL GASTO EN TIEMPO
        notEqual = new BasicDBList();
        notEqual.add("$fechaEntrega");
        notEqual.add(fechaPedida);

        MultParameters = new BasicDBList();
        MultParameters.add("$diasDisponibles");
        MultParameters.add(usoDiario);


        BasicDBList gastoEnTiempoCond = new BasicDBList();
        gastoEnTiempoCond.add(notEqual);
        gastoEnTiempoCond.add(new BasicDBObject("$multiply", MultParameters));
        gastoEnTiempoCond.add("$gastoEnTiempo");

        parametrosAggregate.add(new Document("$set", new Document("gastoEnTiempo", new BasicDBObject("$cond", gastoEnTiempoCond))));

        //EL NUEVO SET PARA EL RESTANTE
        notEqual = new BasicDBList();
        notEqual.add("$fechaEntrega");
        notEqual.add(fechaPedida);

        subParameters = new BasicDBList();
        subParameters.add("$balanceActual");
        subParameters.add("$gastoEnTiempo");


        BasicDBList restantesCond = new BasicDBList();
        restantesCond.add(notEqual);
        restantesCond.add(new BasicDBObject("$subtract", subParameters));
        restantesCond.add("$restantes");

        parametrosAggregate.add(new Document("$set", new Document("restantes", new BasicDBObject("$cond", restantesCond))));

        //SET FINAL
        subParameters = new BasicDBList();
        subParameters.add(cantidadMinima);
        subParameters.add("$restantes");
        parametrosAggregate.add(new Document("$set", new Document("cantidadPedir", new BasicDBObject("$subtract", subParameters))));

        AggregateIterable<Document> resultado = articulos.aggregate(parametrosAggregate);

        String fechaEntrega = "";
        int montoTotal = 0;
        int tiempoEntrega = 0;
        for(Document document : resultado){
            fechaEntrega = document.get("fechaEntrega").toString();
            montoTotal = (int) Double.parseDouble(document.get("cantidadPedir").toString());
            tiempoEntrega = (int) Double.parseDouble(document.get("diasDisponibles").toString());
            System.out.println(document);
        }
        OrdenCompra ordenCompra = suplidoresServices.getOrdenSuplidor(codigoArticulo, fechaEntrega, montoTotal, tiempoEntrega);
        return ordenCompra;
    }

    public BasicDBObject getSwitchGTEParameters(){
        BasicDBList switch_restantesGTEParameters = new BasicDBList();
        switch_restantesGTEParameters.add("$restantes");
        switch_restantesGTEParameters.add(0);
        return new BasicDBObject("$gte", switch_restantesGTEParameters);
    }

    public BasicDBObject getSwitchLTParameters(){
        BasicDBList switch_restantesLTParameters = new BasicDBList();
        switch_restantesLTParameters.add("$restantes");
        switch_restantesLTParameters.add(0);
        return new BasicDBObject("$lt", switch_restantesLTParameters);
    }
}
