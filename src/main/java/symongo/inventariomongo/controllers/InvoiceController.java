package symongo.inventariomongo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import symongo.inventariomongo.services.ArticulosServices;
import symongo.inventariomongo.services.MovimientosServices;
import symongo.inventariomongo.services.OrdenCompraServices;

import java.util.List;

@Controller
public class InvoiceController {
    @Autowired
    public ArticulosServices articulosServices;
    @Autowired public MovimientosServices movimientosServices;
    @Autowired public OrdenCompraServices ordenCompraServices;

    @RequestMapping("/invoice")
    public String invoice() {return "/invoice";}

    @RequestMapping("/generarOrden")
    public String generarOrden(@RequestParam("componente") int articulo,
                               @RequestParam("cantidad") int cantidad,
                               @RequestParam("fecha") String fecha){
        fecha = fecha.replace("-", "/");
        if(articulosServices.validateArticulo(articulo) != 0){
            ordenCompraServices.guardarOrden(articulosServices.obtenerFechaYCantidad(articulo, fecha, movimientosServices.ventaDiaria(articulo), cantidad));
        }
        return "redirect:/invoice";
    }
}
