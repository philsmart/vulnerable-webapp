
package uk.ac.jisc.cybersec.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import uk.ac.jisc.cybersec.dto.PasswordResetDto;
import uk.ac.jisc.cybersec.model.User;
import uk.ac.jisc.cybersec.repo.UserRepository;

@Controller
public class PasswordController {

    private static final Logger log = LoggerFactory.getLogger(PasswordController.class);

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/password-reset")
    @PreAuthorize("isAuthenticated()")
    public String getPasswordResetPage(final Model model, final Principal principal) {

        final PasswordResetDto reset = new PasswordResetDto();
        reset.setUsername(getShaHash(principal.getName()));

        model.addAttribute("newPass", reset);
        return "password-reset";
    }

    private String getShaHash(final String username) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(username.getBytes());
            final byte[] hash = md.digest();
            return new String(Hex.encode(hash));
        } catch (final NoSuchAlgorithmException e) {
            log.error("Could not generate new password hash", e);

        }
        return null;
    }

    @PostMapping("/password-reset")
    @PreAuthorize("isAuthenticated()")
    public String resetPassword(final Model model, final @ModelAttribute("newPass") PasswordResetDto passwordReset,
            final BindingResult bindingResult) {
        log.debug("Resetting password for user [{}]", passwordReset.getUsername());

        User user = null;

        final List<User> allUsers = userRepo.findAll();

        for (final User userToTest : allUsers) {
            final var hash = getShaHash(userToTest.getUsername());
            if ((hash != null) && hash.equals(passwordReset.getUsername())) {
                user = userToTest;
            }
        }

        log.debug("Found user [{}] to reset password", user);
        if (user == null) {
            model.addAttribute("result", "done");
            return "password-reset";
        }
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordReset.getNewPassword().getBytes());
            final byte[] hash = md.digest();
            user.setPassword("{MD5}" + new String(Hex.encode(hash)));
            userRepo.save(user);
            model.addAttribute("result", "done");
        } catch (final NoSuchAlgorithmException e) {
            log.error("Could not generate new password hash", e);
            model.addAttribute("result", "password reset failed");
        }

        return "password-reset";
    }

}
