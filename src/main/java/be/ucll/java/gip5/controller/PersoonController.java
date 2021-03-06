package be.ucll.java.gip5.controller;

import be.ucll.java.gip5.dto.PersoonDTO;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.List;

@VaadinSessionScope
@Controller
public class PersoonController {
    private Logger logger = LoggerFactory.getLogger(PersoonController.class);

    private PersoonDTO loggedInUser;

    private static List<PersoonDTO> users;

    public PersoonDTO authenticateUser(PersoonDTO unauthenticateduser) {
        for (PersoonDTO user : users) {
            if (user.getEmail().equalsIgnoreCase(unauthenticateduser.getEmail()) &&
                    user.getWachtwoord().equals(unauthenticateduser.getWachtwoord())) {
                logger.info("User succesfully authenticated as '" + (user.getEmail() != null ? user.getEmail() : "<unknown>") + "'");
                return user;
            }
        }
        return null;
    }

    public boolean isUserSignedIn() {
        return loggedInUser != null;
    }

    public PersoonDTO getUser() {
        return loggedInUser;
    }

    public void setUser(PersoonDTO user){
        loggedInUser = user;
    }

    public void reset() {
        loggedInUser = null;
    }
}