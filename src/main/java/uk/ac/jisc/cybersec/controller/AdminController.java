
package uk.ac.jisc.cybersec.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import uk.ac.jisc.cybersec.dto.UserAction;
import uk.ac.jisc.cybersec.model.User;
import uk.ac.jisc.cybersec.repo.UserRepository;

/** Controller for the admin functions. */
@Controller
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserRepository userRepo;

    /**
     * Get the user administration page. 
     * 
     * @param model the ui model
     * @param username the username
     * 
     * @return the user-admin page.
     */
    @GetMapping("/user-admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getUserAdminPage(final Model model, final String username) {

        if (username != null) {
            log.debug("Searching for username [{}]", username);
            final var users = userRepo.searchForUsers(username);
            model.addAttribute("users", users);
        } else {
            final List<User> users = userRepo.findAll();
            model.addAttribute("users", users);
            model.addAttribute(new UserAction());
        }
        return "user-admin";
    }
    
    
    /**
     * Update the user's displayName.
     * 
     * @param userAction the action that holds the information to update
     * 
     * @return the user-admin page via a redirect.
     */
    @PostMapping("/user-admin-display-name")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateDisplayName(@ModelAttribute("userAction") final UserAction userAction) {
        log.debug("Updating [{}] to have displayName [{}]", userAction.getId(), userAction.getUsername());
        Optional<User> user = userRepo.findById(userAction.getId());
        if (user.isPresent()) {
        	// Display name not validated here
        	user.get().setDisplayName(userAction.getDisplayName());
        	userRepo.save(user.get());
        }        
        return "redirect:/user-admin";
    }

    /**
     * Update the user.
     * 
     * @param userAction the action that holds the information to update
     * 
     * @return the user-admin page via a redirect.
     */
    @PostMapping("/user-admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateUsername(@ModelAttribute("userAction") final UserAction userAction) {
        log.debug("Updating [{}] to have username [{}]", userAction.getId(), userAction.getUsername());
        userRepo.updateUser(userAction.getUsername(), userAction.getId());
        return "redirect:/user-admin";
    }

}
