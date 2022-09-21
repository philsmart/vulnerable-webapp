
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

/**
 * A controller that captures malicious requests. Intended to mimic endpoints a bad actor
 * might expose to capture user data.
 */
@Controller
public class AttackerController {

    private static final Logger log = LoggerFactory.getLogger(AttackerController.class);

    /**
     * Get the lottery winners malicious page.
     * 
     * @return the lottery-winners page
     */
    @GetMapping("/lottery-winners")
    public String getPage() {
        return "lottery-winners";
    }
    
    /**
     * Get the pet-rescue malicious page.
     * 
     * @return the pets page
     */
    @GetMapping("/pet-rescue")
    public String getPagePetRescue() {
        return "pets";
    }

    /**
     * An endpoint to send a cookie value to. Logs the value and returns a 302 redirect to the
     * items page.
     * 
     * @param request the http request
     * @param cookie the value of the sent cookie
     * 
     * @return a 302 redirect to the items page
     */
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

    /**
     * And endpoint to send key presses too (from a XSS attack for example). 
     * 
     * @param request the http request
     * @param keys the keystrokes
     * 
     * @return a HTTP 200 response.
     */
    @GetMapping(value = "/shadow")
    public ResponseEntity<Void> shadow(final HttpServletRequest request, @RequestParam("keys") final String keys) {
        log.debug("Found keypresses [{}]", keys);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
