package be.ucll.java.gip5.controller;

import be.ucll.java.gip5.dto.PersoonDTO;
import be.ucll.java.gip5.model.Rol;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@VaadinSessionScope
@Controller
public class PersoonController {
    private Logger logger = LoggerFactory.getLogger(PersoonController.class);

    private PersoonDTO loggedInUser;

    private static List<PersoonDTO> users;

    static {
        users = new ArrayList<>(1);
        users.add(new PersoonDTO("admin", "admin", Date.from(Instant.now()), "M", "Bergenhof 56",
                "0486947755", "0486947755", "admin@admin.com", "admin", Rol.SECRETARIS));
    }

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