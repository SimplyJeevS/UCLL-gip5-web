package be.ucll.java.gip5.view;

import be.ucll.java.gip5.util.SpringVars;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Route("")
@Theme(value = Lumo.class)
@PageTitle("Hello UCLL")
public class HelloView extends VerticalLayout {

    @Autowired
    private SpringVars vars;

    @PostConstruct
    public void init() {
        add(new H1("Welkom op de UCLL Gip5 Starter pagina"));
        add(new Text("Dit is een vrijwel lege Vaadin toepassing die vervangen moet worden door de opdracht van de Gip 5."));
        add(new Paragraph("Deze dummy web app bevat een kleine JPA Entity die even de connectiviteit met de databank test door er een dummy tabel aan te maken."));
        add(new Paragraph(new Text("Deze dummy web app bevat een Test Rest service:"), new Anchor("rest/v1/test", "Test Rest service")));
        add(new Paragraph("De toepassing is opgestart op: " + vars.getStartupTime() + " (UTC Time)"));
    }

}
