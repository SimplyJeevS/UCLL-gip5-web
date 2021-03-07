package be.ucll.java.gip5.view;


import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.dao.ToewijzingRepository;
import be.ucll.java.gip5.dto.PersoonDTO;
import be.ucll.java.gip5.dto.PloegDTO;
import be.ucll.java.gip5.dto.ToewijzingDTO;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.model.Ploeg;
import be.ucll.java.gip5.model.Toewijzing;
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

import java.util.*;

public class ToewijzingDialog extends Dialog {
    private Logger logger = LoggerFactory.getLogger(ToewijzingDialog.class);

    // Spring beans/services
    private ToewijzingResource toewijzingMngr;
    private PloegResource PloegMngr;
    private ToewijzingRepository toewijzingRepository;
    private PersoonRepository persoonRepository;
    private PersoonFragment frm;

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

    public ToewijzingDialog(PersoonDTO persoonDTO) throws NotFoundException, InvalidCredentialsException, ParameterInvalidException {
        super();

        // Load Spring beans
        toewijzingMngr = BeanUtil.getBean(ToewijzingResource.class);
        toewijzingRepository = BeanUtil.getBean(ToewijzingRepository.class);
        PloegMngr = BeanUtil.getBean(PloegResource.class);
        persoonRepository = BeanUtil.getBean(PersoonRepository.class);
        //selectedToewijzing = BeanUtil.getBean(ToewijzingDTO.class);

        lblStudInfo = new Label("Inschrijvingen voor persoon: " + persoonDTO.getVoornaam() + " " + persoonDTO.getNaam() + " (" + persoonDTO.getGeboortedatum() + ")");
        lblStudInfo.setId("bold-label");
        add(lblStudInfo);

        lstInsch = new ListBox<>();
        Optional<Persoon> p = persoonRepository.findPersoonByEmailIgnoreCase(persoonDTO.getEmail());
        List<Toewijzing> inschrijvingen = (List<Toewijzing>) toewijzingMngr.getToewijzingListVanPersoon(p.get().getId(), "").getBody();
        if (inschrijvingen != null && inschrijvingen.size() > 0) {
            inschrijvingen.forEach(i -> {
                lstInsch.setItems(new ToewijzingDTO(i.getPersoonId(), i.getRol(), i.getPloegId()));
            });
        }
        lstInsch.setMaxHeight("50%");
        lstInsch.addValueChangeListener(e -> {
            selectedToewijzing = e.getValue();
            btnUitschrijven.setVisible(true);
        });
        add(lstInsch);

        btnUitschrijven = new Button("Uitschrijven");
        btnUitschrijven.setVisible(false);
        btnUitschrijven.addClickListener(e -> {
            try {
                toewijzingMngr.deleteToewijzing(p.get().getId(), "");
                lstInsch.setItems((Collection<ToewijzingDTO>) toewijzingMngr.getToewijzingList(p.get().getId().toString()));
                Notification.show("Student uitgeschreven", 3000, Notification.Position.TOP_CENTER);
                this.close();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
                Notification.show(ex.getMessage(), 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ParameterInvalidException ex) {
                ex.printStackTrace();
            } catch (InvalidCredentialsException ex) {
                ex.printStackTrace();
            } catch (NotFoundException ex) {
                ex.printStackTrace();
            }
        });
        add(btnUitschrijven);

        hruler = new Html("<span><br/><hr/><br/></span>");
        add(hruler);

        hl1 = new HorizontalLayout();
        hl1.setWidthFull();
        cmbPloegen = new ComboBox<>();
        cmbPloegen.setItemLabelGenerator(PloegDTO::getNaam);
        try {
            List<Ploeg> ploegen = (List<Ploeg>) PloegMngr.getPloegen("").getBody();
            ArrayList<PloegDTO> ploegDTOList = new ArrayList<>();
            for (int i = 0; i < ploegen.size(); i++){
                PloegDTO ploeg = new PloegDTO(ploegen.get(i).getNaam());
                ploegDTOList.add(ploeg);
            }
            cmbPloegen.setItems(ploegDTOList);
        } catch (NotFoundException | InvalidCredentialsException e) {
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
                    toewijzingMngr.postToewijzing(selectedToewijzing,"");
                } catch (ParameterInvalidException parameterInvalidException) {
                    parameterInvalidException.printStackTrace();
                } catch (NotFoundException notFoundException) {
                    notFoundException.printStackTrace();
                } catch (InvalidCredentialsException invalidCredentialsException) {
                    invalidCredentialsException.printStackTrace();
                }
                lstInsch.setItems((Collection<ToewijzingDTO>) toewijzingMngr.getToewijzingList(p.get().getId().toString()));
                Notification.show("Student ingeschreven", 3000, Notification.Position.TOP_CENTER);
                chkBetaald.setValue(false);
                this.close();
            } catch (IllegalArgumentException | NotFoundException | InvalidCredentialsException ex) {
                Notification.show(ex.getMessage(), 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        add(btnInschrijven);
    }

}

