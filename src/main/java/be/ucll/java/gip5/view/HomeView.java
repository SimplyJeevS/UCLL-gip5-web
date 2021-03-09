package be.ucll.java.gip5.view;


import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.rest.PersoonResource;
import be.ucll.java.gip5.util.BeanUtil;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Route("Home")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@PageTitle("Home UCLL")
public class HomeView extends AppLayout {

    @Autowired
    private PersoonResource persoonResource;

    @Autowired
    private MessageSource messageSrc;
    private Locale loc;

    // Content views
    private PersoonView pView;
    private PloegenView plView;
    private WedstrijdenView wView;
    // Left navigation tabs
    private Tab tab1;
    private static final String TABNAME1 = "Personen";
    private Tab tab2;
    private static final String TABNAME2 = "Ploegen";
    private Tab tab5;
    private static final String TABNAME5 = "Wedstrijden";
    private Tabs tabs;

    private Button btnLogout;

    public HomeView() {
        messageSrc = BeanUtil.getBean(MessageSource.class);

        // Locale derived from the Browser language settings
        loc = VaadinSession.getCurrent().getLocale();

        // Header / Menu bar on the top of the page
        H3 header = new H3("Hometest"); //    new H3(messageSrc.getMessage("app.title", null, loc));
        header.setId("header-layout");

        btnLogout = new Button("Log uit");
        btnLogout.addClickListener(e -> {
            handleLogoutClicked(e);
        });
        btnLogout.setId("aligneer-knop-rechts");

        addToNavbar(new DrawerToggle(),
                new Html("<span>&nbsp;&nbsp;</span>"),
                header,
                new Html("<span>&nbsp;-&nbsp;</span>"),
                new Icon(VaadinIcon.ACADEMY_CAP),
                btnLogout);

        // Tabs on the left side drawer
        tab1 = new Tab(TABNAME1);
        tab2 = new Tab(TABNAME2);
        tab5 = new Tab(TABNAME5);
        tabs = new Tabs(tab1, tab2, tab5);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addSelectedChangeListener(event -> {
            handleTabClicked(event);
        });
        addToDrawer(tabs);

    }

    @PostConstruct
    public void init() throws InvalidCredentialsException, NotFoundException {
        pView = new PersoonView();
        pView.loadData();

        plView = new PloegenView();
        plView.loadData();

        wView = new WedstrijdenView();
        wView.loadData();
        // As default load the persoonView
        setContent(pView);
    }


    private void handleTabClicked(Tabs.SelectedChangeEvent event) {
        Tab selTab = tabs.getSelectedTab();
        if (selTab.getLabel() != null) {
            if (selTab.getLabel().equals(TABNAME1)) {
                setContent(pView);
            } else if (selTab.getLabel().equals(TABNAME2)) {
                setContent(plView);
            } else if (selTab.getLabel().equals(TABNAME5)) {
                setContent(wView);
            } else {
                setContent(new Label("Te implementeren scherm voor Admins only"));
            }
        }
    }

    private void handleLogoutClicked(ClickEvent event) {
        //PersoonResource.reset();
        getUI().ifPresent(ui -> ui.navigate("login"));
    }


}
