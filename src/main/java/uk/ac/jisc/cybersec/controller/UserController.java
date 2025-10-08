
package uk.ac.jisc.cybersec.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import uk.ac.jisc.cybersec.dto.UserAction;
import uk.ac.jisc.cybersec.model.User;
import uk.ac.jisc.cybersec.repo.UserRepository;
import uk.ac.jisc.cybersec.secure.CustomUserDetails;

/** Controller for the admin functions. */
@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/user-prefs/{id}")
    @PreAuthorize("isAuthenticated()")
    public String getUserAdminPage(final Model model, @PathVariable final String id) {
        log.debug("Getting user-prefs for user '{}'", id);
        if (id == null) {
            return "user-prefs";
        }
        final Long userIdLong = Long.parseLong(id);
        log.debug("Searching for username by ID [{}]", userIdLong);
        final var user = userRepo.findById(userIdLong);
        log.debug("Found '{}' user", user);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
        }
        return "user-prefs";
    }

    @PostMapping("/user-prefs")
    @PreAuthorize("isAuthenticated()")
    public String updateUsername(@ModelAttribute("userAction") final UserAction userAction, Authentication user) {
        log.debug("Updating [{}] to have username [{}]", userAction.getId(), userAction.getUsername());
        userRepo.updateUser(userAction.getUsername(), userAction.getId());
        
        if (user.getPrincipal() instanceof CustomUserDetails) {
            var userDetails = (CustomUserDetails) user.getPrincipal();
            log.debug("Logged in user has id '{}'", userDetails.getUser().getId());
            return "redirect:/user-prefs/"+userDetails.getUser().getId();
        }
        
        return "redirect:/";
    }
    
    @PostMapping("/user-prefs-displayname")
    @PreAuthorize("isAuthenticated()")
    public String updateDisplayName(@ModelAttribute("userAction") final UserAction userAction, Authentication authUser) {
    	
    	 log.debug("Updating [{}] to have displayName [{}]", userAction.getId(), userAction.getDisplayName());
         Optional<User> user = userRepo.findById(userAction.getId());
         if (user.isPresent()) {
         	user.get().setDisplayName(userAction.getDisplayName());
         	userRepo.save(user.get());
         }        
        
        if (authUser.getPrincipal() instanceof CustomUserDetails) {
            var userDetails = (CustomUserDetails) authUser.getPrincipal();
            userDetails.getUser().setDisplayName(userAction.getDisplayName());
            log.debug("Logged in user has id '{}'", userDetails.getUser().getId());
            return "redirect:/user-prefs/"+userDetails.getUser().getId();
        }
        
        return "redirect:/";
    }

}
