package symongo.inventariomongo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import symongo.inventariomongo.services.SuplidoresServices;

@Controller
@RequestMapping("/supply")
public class SupplyController {
    @Autowired SuplidoresServices suplidoresServices;

    @RequestMapping("")
    public String supply(Model model) {
        model.addAttribute("listaSuplidores", suplidoresServices.getSuplidores());
        return "/suppliers";
    }

    @RequestMapping("/guardarSuplidor")
    public String guardarSuplidor(@RequestParam("codigoArticulo") int codigoArticulo,
                                  @RequestParam("tiempoEntrega") int tiempoEntrega,
                                  @RequestParam("precioCompra") int precioCompra){
        suplidoresServices.guardarSuplidor(codigoArticulo, tiempoEntrega, precioCompra);
        System.out.println("I saved it!");
        return "redirect:/supply";
    }
}
