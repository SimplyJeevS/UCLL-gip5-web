package be.ucll.java.gip5.view;

import be.ucll.java.gip5.controller.PersoonController;
import be.ucll.java.gip5.dto.PersoonDTO;
import be.ucll.java.gip5.util.BeanUtil;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
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
    private MessageSource messageSrc;
    private Locale loc;

    public LoginView() {
        messageSrc = BeanUtil.getBean(MessageSource.class);

        // Locale derived from the Browser language settings
        loc = new Locale("en_US");
        logger.debug("Browser/Session locale: " + loc.toString());

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle(messageSrc.getMessage("app.title", null, loc));
        i18n.getForm().setUsername(messageSrc.getMessage("login.lbl.userid", null, loc));
        i18n.getForm().setPassword(messageSrc.getMessage("login.lbl.password", null, loc));
        i18n.getErrorMessage().setTitle(messageSrc.getMessage("login.failure.tit", null, loc));
        i18n.getErrorMessage().setMessage(messageSrc.getMessage("login.failure.msg", null, loc));

        LoginForm frmLogin = new LoginForm(i18n);
        frmLogin.setForgotPasswordButtonVisible(true);
        frmLogin.addLoginListener(e -> {
            PersoonDTO loginUser = userCtrl.authenticateUser(new PersoonDTO(e.getUsername(), e.getPassword()));
            if (loginUser == null) {
                frmLogin.setError(true);
                logger.warn("Failed login attempt for user with id: " + (e.getUsername() != null ? e.getUsername() : "<undefined>"));
            } else {
                userCtrl.setUser(loginUser);
                logger.info("User '" + loginUser.getEmail() + "' successfully authenticated");
                getUI().ifPresent(ui -> ui.navigate("main"));
            }
        });

        this.add(frmLogin);
        this.setAlignItems(Alignment.CENTER);
        this.setSizeFull();
    }
}
