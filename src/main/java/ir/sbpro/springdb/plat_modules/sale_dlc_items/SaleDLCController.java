package ir.sbpro.springdb.plat_modules.sale_dlc_items;

import ir.sbpro.springdb.plat_modules.sale_game_items.SaleGameItem;
import ir.sbpro.springdb.plat_modules.sale_game_items.SaleGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/saledlc")
public class SaleDLCController {
    SaleDLCService saleDLCService;

    @Autowired
    SaleDLCController(SaleDLCService saleDLCService){
        this.saleDLCService = saleDLCService;
    }

    @GetMapping(value = {"", "/"})
    public String getPlatGamesView(Model model,
                                   @ModelAttribute("gsearch") SaleDLCItem saleDLC,
                                   @PageableDefault(size = 20) Pageable pageable){

        String sort = null;
        Optional<Sort.Order> orderOptional = pageable.getSort().get().findFirst();
        if(orderOptional.isPresent()) sort = orderOptional.get().getProperty();

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        model.addAttribute("records",
                saleDLCService.findBySearchQuery(pageRequest, saleDLC, sort));
        return "sale_dlc/sale_dlc";
    }
}
