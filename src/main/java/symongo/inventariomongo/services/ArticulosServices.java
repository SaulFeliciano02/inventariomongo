package symongo.inventariomongo.services;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Service;
import symongo.inventariomongo.connection.MongoConnect;
import symongo.inventariomongo.entities.InfoAlmacen;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ArticulosServices {
    public void guardarArticulo(String codigoArticulo, String descripcion, String unidadCompra, ArrayList<InfoAlmacen> infoList){
        MongoCollection<Document> articulos = MongoConnect.database.getCollection("articulos");

        Document articulo = new Document();
        articulo.append("codigoArticulo", codigoArticulo);
        articulo.append("descripcion", descripcion);
        articulo.append("unidadCompra", unidadCompra);


        ArrayList<HashMap<String, Integer>> almacenInfo = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            HashMap<String, Integer> almacen = new HashMap<>();
            almacen.put(infoList.get(i).getCodigoAlmacen(), infoList.get(i).getBalanceActual());
            almacenInfo.add(almacen);
        }
        articulo.put("infoAlmacen", almacenInfo);

        articulos.insertOne(articulo);
    }
}
