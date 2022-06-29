
package uk.ac.jisc.cybersec.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AttackerController {

    private static final Logger log = LoggerFactory.getLogger(AttackerController.class);

    @GetMapping("/lottery-winners")
    public String getPage() {
        return "lottery-winners";
    }

    @GetMapping("/pet-rescue")
    public String getPagePetRescue() {
        return "pets";
    }

}
