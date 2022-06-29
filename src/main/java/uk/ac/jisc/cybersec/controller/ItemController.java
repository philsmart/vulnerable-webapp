package uk.ac.jisc.cybersec.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import uk.ac.jisc.cybersec.repo.ItemRepository;

/** Controller for displaying all items.*/
@Controller
public class ItemController {
    
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);
    
    @Autowired private ItemRepository itemRepo;

    @GetMapping("/items")
    public String items(Model model) {
        log.info("Getting items");
        var items = itemRepo.findAll();
        log.info("Found '{}' items", items.size());
        model.addAttribute("items", items);
        return "items";
    }   

}
