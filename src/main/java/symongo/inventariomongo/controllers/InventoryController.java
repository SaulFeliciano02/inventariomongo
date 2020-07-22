package symongo.inventariomongo.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import symongo.inventariomongo.connection.MongoConnect;
import symongo.inventariomongo.entities.InfoAlmacen;
import symongo.inventariomongo.entities.OrdenCompra;
import symongo.inventariomongo.services.ArticulosServices;
import symongo.inventariomongo.services.MovimientosServices;
import symongo.inventariomongo.services.OrdenCompraServices;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired public ArticulosServices articulosServices;
    @Autowired public MovimientosServices movimientosServices;
    @Autowired public OrdenCompraServices ordenCompraServices;

    @RequestMapping("")
    public String inventory(){
        System.out.println(MongoConnect.database);
        return "/inventory";
    }

    @RequestMapping("/guardarArticulo")
    public String saveArticulo(@RequestParam("codigoArticulo") String codigoArticulo,
                               @RequestParam("descripcion") String descripcion,
                               @RequestParam("unidadCompra") String unidadCompra,
                               @RequestParam("balanceActual1") int balanceActual1,
                               @RequestParam("balanceActual2") int balanceActual2,
                               @RequestParam("balanceActual3") int balanceActual3){
        InfoAlmacen info1 = new InfoAlmacen("1", balanceActual1);
        InfoAlmacen info2 = new InfoAlmacen("2", balanceActual2);
        InfoAlmacen info3 = new InfoAlmacen("3", balanceActual3);
        ArrayList<InfoAlmacen> listaInfos = new ArrayList<>();
        listaInfos.add(info1); listaInfos.add(info2); listaInfos.add(info3);
        articulosServices.guardarArticulo(codigoArticulo, descripcion, unidadCompra, listaInfos);
        return "";
    }

    @RequestMapping("/testUsoDiario")
    public String usoDiario(){
        movimientosServices.ventaDiaria(3);
        return "/index";
    }

    @RequestMapping("/generarOrden")
    public String generarOrden(List<Integer> articulos, List<Integer> cantidad, List<String> fechas){
        for(int i = 0; i < articulos.size(); i++){
            if(articulosServices.validateArticulo(articulos.get(i)) != 0){
                ordenCompraServices.guardarOrden(articulosServices.obtenerFechaYCantidad(articulos.get(i), fechas.get(i), movimientosServices.ventaDiaria(articulos.get(i)), cantidad.get(i)));
            }
        }
        return "/index";
    }
}
