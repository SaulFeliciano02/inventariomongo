package symongo.inventariomongo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import symongo.inventariomongo.entities.OrdenCompra;
import symongo.inventariomongo.services.OrdenCompraServices;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersControllers {
    @Autowired public OrdenCompraServices ordenCompraServices;

    @RequestMapping("")
    public String orders(Model model, @ModelAttribute("ordenes") ArrayList<OrdenCompra> orden){
        if(orden.size() > 0){
            model.addAttribute("listaOrdenes", orden);
        }
        else{
            model.addAttribute("listaOrdenes", ordenCompraServices.getOrdenes());
        }
        return "/orders";
    }

    @RequestMapping("/buscarOrdenes")
    public RedirectView buscarOrdenes(Model model, RedirectAttributes attributes,
                                      @RequestParam("articulo") int articulo,
                                      @RequestParam("suplidor") int suplidor,
                                      @RequestParam("fecha") String fechaOrden){
        fechaOrden = fechaOrden.replace("-", "/");
        ArrayList<OrdenCompra> ordenes = ordenCompraServices.buscarOrdenes(articulo, suplidor, fechaOrden);
        attributes.addFlashAttribute("ordenes", ordenes);
        return new RedirectView("/orders");
    }
}
