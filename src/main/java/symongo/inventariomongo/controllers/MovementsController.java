package symongo.inventariomongo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import symongo.inventariomongo.entities.Movimiento;
import symongo.inventariomongo.entities.OrdenCompra;
import symongo.inventariomongo.services.ArticulosServices;
import symongo.inventariomongo.services.MovimientosServices;

import java.util.ArrayList;

@Controller
@RequestMapping("/movements")
public class MovementsController {

    @Autowired public MovimientosServices movimientosServices;
    @Autowired public ArticulosServices articulosServices;

    @RequestMapping("")
    public String movements(Model model, @ModelAttribute("listaMovimientos") ArrayList<Movimiento> movimientos){
        model.addAttribute("listaMovimientos", movimientos);
        return "/movements";
    }

    @RequestMapping("/guardarMovimiento")
    public String guardarMovimiento(@RequestParam("tipoMovimiento") String tipoMovimiento,
                                    @RequestParam("codigoArticulo") int codigoArticulo,
                                    @RequestParam("cantidad") int cantidad){
        movimientosServices.guardarMovimiento(codigoArticulo, cantidad, tipoMovimiento);
        articulosServices.updateArticulo(codigoArticulo, tipoMovimiento, cantidad);
        return "redirect:/movements";
    }

    @RequestMapping("/buscarMovimiento")
    public RedirectView buscarMovimiento(RedirectAttributes attributes,
                                         @RequestParam("tipoMovimiento") String tipoMovimiento,
                                         @RequestParam("articulo") int codigoArticulo,
                                         @RequestParam("fecha") String fecha){
        ArrayList<Movimiento> movimientos = movimientosServices.buscarMovimientos(tipoMovimiento, codigoArticulo, fecha);
        attributes.addFlashAttribute("listaMovimientos", movimientos);
        return new RedirectView("/movements");
    }
}
