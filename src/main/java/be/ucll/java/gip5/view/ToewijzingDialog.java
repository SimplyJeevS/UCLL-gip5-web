package be.ucll.java.gip5.view;



import be.ucll.java.gip5.dto.PersoonDTO;
import be.ucll.java.gip5.dto.PloegDTO;
import be.ucll.java.gip5.dto.ToewijzingDTO;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.rest.PloegResource;
import be.ucll.java.gip5.rest.ToewijzingResource;
import be.ucll.java.gip5.util.BeanUtil;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToewijzingDialog extends Dialog {
    private Logger logger = LoggerFactory.getLogger(ToewijzingDialog.class);

    // Spring beans/services
    private ToewijzingResource toewijzingMngr;
    private PloegResource PloegMngr;

    private Label lblStudInfo;
    private ListBox<ToewijzingDTO> lstInsch;
    private Button btnUitschrijven;

    private Html hruler;

    private HorizontalLayout hl1;
    private ComboBox<PloegDTO> cmbPloegen;
    private Checkbox chkBetaald;
    private Button btnInschrijven;

    private PloegDTO selectedLM;
    private ToewijzingDTO selectedToewijzing;

    public ToewijzingDialog(PersoonDTO p) {
        super();

        // Load Spring beans
        toewijzingMngr = BeanUtil.getBean(ToewijzingResource.class);
        PloegMngr = BeanUtil.getBean(PloegResource.class);

        lblStudInfo = new Label("Inschrijvingen voor persoon: " + p.getVoornaam() + " " + p.getNaam() + " (" + p.getGeboortedatum() + ")");
        lblStudInfo.setId("bold-label");
        add(lblStudInfo);

//        lstInsch = new ListBox<>();
//        List<ToewijzingDTO> inschrijvingen = toewijzingMngr.getToewijzingList(p.getId());
//        if (inschrijvingen != null && inschrijvingen.size() > 0) {
//            lstInsch.setItems(inschrijvingen);
//        }
//        lstInsch.setMaxHeight("50%");
//        lstInsch.addValueChangeListener(e -> {
//            selectedInschr = e.getValue();
//            btnUitschrijven.setVisible(true);
//        });
//        add(lstInsch);

        btnUitschrijven = new Button("Uitschrijven");
        btnUitschrijven.setVisible(false);
        btnUitschrijven.addClickListener(e -> {
            try {
                //toewijzingMngr.deleteToewijzing(p, PloegMngr.getPloegen(selectedInschr.getPloegId()));
               // lstInsch.setItems(toewijzingMngr.getToewijzingList(p.getId()));
                Notification.show("Student uitgeschreven", 3000, Notification.Position.TOP_CENTER);
                //this.close();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
                Notification.show(ex.getMessage(), 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        add(btnUitschrijven);

        hruler = new Html("<span><br/><hr/><br/></span>");
        add(hruler);

        hl1 = new HorizontalLayout();
        hl1.setWidthFull();
        cmbPloegen = new ComboBox<>();
        cmbPloegen.setItemLabelGenerator(PloegDTO::toString);
        try {
            cmbPloegen.setItems((ComboBox.ItemFilter<PloegDTO>) PloegMngr.getPloegen());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        cmbPloegen.setWidth("500px");
        cmbPloegen.addValueChangeListener(event -> {
            selectedLM = cmbPloegen.getValue();
            btnInschrijven.setEnabled(true);
        });

        chkBetaald = new Checkbox("Betaald");

        hl1.add(new Label("Leermodules: "), cmbPloegen, chkBetaald);
        add(hl1);

        btnInschrijven = new Button("Toewijzen");
        btnInschrijven.setEnabled(false);
        btnInschrijven.addClickListener(e -> {
            try {
                try {
                    toewijzingMngr.postToewijzing(selectedToewijzing);
                } catch (ParameterInvalidException parameterInvalidException) {
                    parameterInvalidException.printStackTrace();
                } catch (NotFoundException notFoundException) {
                    notFoundException.printStackTrace();
                }
                //lstInsch.setItems(toewijzingMngr.getToewijzingList(p.getId()));
                Notification.show("Student ingeschreven", 3000, Notification.Position.TOP_CENTER);
                chkBetaald.setValue(false);
                //this.close();
            } catch (IllegalArgumentException ex) {
                Notification.show(ex.getMessage(), 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        add(btnInschrijven);
    }

}

