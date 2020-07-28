package symongo.inventariomongo.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import symongo.inventariomongo.connection.MongoConnect;
import symongo.inventariomongo.entities.Articulo;
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
    public String inventory(Model model){
        model.addAttribute("listaArticulos", articulosServices.getArticulos());
        return "/inventory";
    }

    @RequestMapping("/guardarArticulo")
    public String saveArticulo(@RequestParam("codigoArticulo") int codigoArticulo,
                               @RequestParam("descripcion") String descripcion,
                               @RequestParam("unidadCompra") String unidadCompra,
                               @RequestParam("balanceActual") int total){
        articulosServices.guardarArticulo(codigoArticulo, descripcion, unidadCompra, total);
        return "redirect:/inventory";
    }

    @RequestMapping("/testUsoDiario")
    public String usoDiario(){
        movimientosServices.ventaDiaria(3);
        return "/index";
    }

    @RequestMapping("/mostrarOrdenes")
    public String mostrarOrden(){
        return "orders";
    }
}
