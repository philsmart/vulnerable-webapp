package uk.ac.jisc.cybersec.controller;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import uk.ac.jisc.cybersec.repo.ItemRepository;

/** Controller for displaying all items.*/
@Controller
public class ItemController {
    
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);
    
    @Autowired private ItemRepository itemRepo;

    @GetMapping("/items")
    public String items(Model model, @RequestParam(required = false, name="q") final String searchTerm) {
        log.info("Getting items, using search term {}",searchTerm);
        var items = itemRepo.findAll();       
        log.info("Found '{}' items", items.size());        
        if (searchTerm != null) {
            var itemsFiltered = 
                    items.stream().filter(it -> it.getDescription().contains(searchTerm)).collect(Collectors.toList());
            model.addAttribute("searchTerm", searchTerm);
            model.addAttribute("items", itemsFiltered);
        } else {
            model.addAttribute("items", items);
        }
        return "items";
    }   

}
