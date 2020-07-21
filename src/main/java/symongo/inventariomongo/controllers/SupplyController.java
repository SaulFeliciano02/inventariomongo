package symongo.inventariomongo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SupplyController {
    @RequestMapping("/supply")
    public String supply() {return "/suppliers";}
}
