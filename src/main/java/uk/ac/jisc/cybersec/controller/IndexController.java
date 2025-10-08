
package uk.ac.jisc.cybersec.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import uk.ac.jisc.cybersec.secure.CustomUserDetails;

/**
 * The home page controller.
 */
@Controller
class IndexController {
    
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    /**
     * Get the index or root or homepage. 
     * 
     * @param model the ui model
     * @param user the authenticated user
     * 
     * @return the home page.
     */
    @RequestMapping("/")
    public String index(Model model, Authentication user) {
        if (user.getPrincipal() instanceof CustomUserDetails) {
            var userDetails = (CustomUserDetails) user.getPrincipal();
            log.debug("Logged in user has id '{}'", userDetails.getUser().getId());
            model.addAttribute("userId",userDetails.getUser().getId());
        }
        return "home";
    }
    
    
    /**
     * Get the 3PC demonstration page.
     * 
     * @return the lottery-winners page
     */
    @GetMapping("/3pc")
    public String getPage(final HttpServletRequest req) {
    	req.getSession();
        return "3pc";
    }
    
    @GetMapping("/iframe-me")
    public String getIframePage(final HttpServletRequest req) {
    	if (req.getCookies() == null) {
    		log.info("No cookies");
    	} else {
    		Arrays.stream(req.getCookies()).forEach(c -> log.info("Cookie: {}", c));
    	}
    	req.getSession();
        return "iframe-me";
    }
}
