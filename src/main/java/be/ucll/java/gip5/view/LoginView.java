package be.ucll.java.gip5.view;

import be.ucll.java.gip5.controller.PersoonController;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.rest.LoginResource;
import be.ucll.java.gip5.util.BeanUtil;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Route("")
@RouteAlias("login")
@PageTitle("GIP5 - Login")
public class LoginView extends VerticalLayout {
    private Logger logger = LoggerFactory.getLogger(LoginView.class);

    @Autowired
    private PersoonController userCtrl;

    @Autowired
    private LoginResource loginResource;

    @Autowired
    private MessageSource messageSrc;
    private Locale loc;

    public LoginView() {
        messageSrc = BeanUtil.getBean(MessageSource.class);
        loginResource = BeanUtil.getBean(LoginResource.class);

        // Locale derived from the Browser language settings
        loc = new Locale("en_US");
        logger.debug("Browser/Session locale: " + loc.toString());


        LoginForm frmLogin = new LoginForm();
        frmLogin.setForgotPasswordButtonVisible(true);
        frmLogin.addLoginListener(e -> {
            try {
                loginResource.getCheckLogin(e.getUsername(), e.getPassword());
                getUI().ifPresent(ui -> ui.navigate("Home"));
            } catch (InvalidCredentialsException ex) {
                frmLogin.setError(true);
                ex.printStackTrace();
            }
        });

        this.add(frmLogin);
        this.setAlignItems(Alignment.CENTER);
        this.setSizeFull();
    }
}
