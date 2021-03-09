package be.ucll.java.gip5.view;


import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.dto.PersoonDTO;
import be.ucll.java.gip5.dto.PloegDTO;
import be.ucll.java.gip5.dto.ToewijzingDTO;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.model.Ploeg;
import be.ucll.java.gip5.model.Rol;
import be.ucll.java.gip5.model.Toewijzing;
import be.ucll.java.gip5.rest.PloegResource;
import be.ucll.java.gip5.rest.ToewijzingResource;
import be.ucll.java.gip5.util.BeanUtil;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ToewijzingDialog extends Dialog {
    private Logger logger = LoggerFactory.getLogger(ToewijzingDialog.class);

    // Spring beans/services
    private ToewijzingResource toewijzingMngr;
    private PloegResource PloegMngr;
    private PersoonRepository persoonRepository;
    private PloegDTO selectedPloeg;
    private ToewijzingDTO selectedToewijzing = new ToewijzingDTO();


    private Label lblStudInfo;
    private ListBox<ToewijzingDTO> lstToewijzing;
    private Button btnDeleteToewijzing;
    private Html hruler;
    private HorizontalLayout hl1;
    private ComboBox<PloegDTO> cmbPloegen;
    private Button btnInschrijven;


    public ToewijzingDialog(Persoon persoonDTO) throws NotFoundException, InvalidCredentialsException, ParameterInvalidException {
        super();

        // Load Spring beans
        toewijzingMngr = BeanUtil.getBean(ToewijzingResource.class);
        PloegMngr = BeanUtil.getBean(PloegResource.class);
        persoonRepository = BeanUtil.getBean(PersoonRepository.class);

        lblStudInfo = new Label("Toewijzingen voor persoon: " + persoonDTO.getId() + " - " + persoonDTO.getVoornaam() + " " + persoonDTO.getNaam());
        lblStudInfo.setId("bold-label");
        add(lblStudInfo);

        lstToewijzing = new ListBox<>();
        Optional<Persoon> p = persoonRepository.findPersoonByEmailIgnoreCase(persoonDTO.getEmail());
        List<Toewijzing> toewijzingen = (List<Toewijzing>) toewijzingMngr.getToewijzingListVanPersoon(p.get().getId(), "").getBody();

        if (toewijzingen != null && toewijzingen.size() > 0) {
            toewijzingen.forEach(i -> {
                lstToewijzing.setItems(new ToewijzingDTO(i.getPersoonId(), i.getRol(), i.getPloegId()));
            });
        }

        lstToewijzing.setMaxHeight("50%");
        lstToewijzing.addValueChangeListener(e -> {
            btnDeleteToewijzing.setVisible(true);
        });
        add(lstToewijzing);

        btnDeleteToewijzing = new Button("Toewijzing verwijderen");
        btnDeleteToewijzing.setVisible(false);
        btnDeleteToewijzing.addClickListener(e -> {
            try {
                toewijzingMngr.deleteToewijzing(p.get().getId(), "");
                lstToewijzing.setItems((Collection<ToewijzingDTO>) toewijzingMngr.getToewijzingList(p.get().getId().toString()));
                Notification.show("Toewijzing verwijderd", 3000, Notification.Position.TOP_CENTER);
                this.close();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
                Notification.show(ex.getMessage(), 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ParameterInvalidException ex) {
                ex.printStackTrace();
            } catch (InvalidCredentialsException ex) {
                Notification.show("Je bent niet ingelogd", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                UI.getCurrent().navigate("login");
                ex.printStackTrace();
            } catch (NotFoundException ex) {
                ex.printStackTrace();
            }
        });
        add(btnDeleteToewijzing);

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
                PloegDTO ploeg = new PloegDTO(ploegen.get(i).getId(), ploegen.get(i).getNaam());
                ploegDTOList.add(ploeg);
            }
            cmbPloegen.setLabel("Ploeg");
            cmbPloegen.setItems(ploegDTOList);
        } catch (NotFoundException | InvalidCredentialsException e) {
            e.printStackTrace();
        }

        cmbPloegen.setWidth("400px");
        cmbPloegen.addValueChangeListener(event -> {
            Optional<Persoon> selectedPersoon = persoonRepository.findPersoonByEmailIgnoreCase(persoonDTO.getEmail());
            selectedToewijzing.setPloegId(event.getValue().getId());
            selectedToewijzing.setPersoonId(selectedPersoon.get().getId());
        });
        add(cmbPloegen);

        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("Rol");
        radioGroup.setItems("Coach", "Speler", "Journalist", "Supporter");
        radioGroup.addThemeVariants(RadioGroupVariant.LUMO_HELPER_ABOVE_FIELD);

        Div value = new Div();
        radioGroup.addValueChangeListener(event -> {
            switch (event.getValue())
            {
                case "Coach":  selectedToewijzing.setRol(Rol.COACH);
                break;
                case "Speler": selectedToewijzing.setRol(Rol.SPELER);
                break;
                case "Journalist": selectedToewijzing.setRol(Rol.JOURNALIST);
                break;
                case "Supporter": selectedToewijzing.setRol(Rol.SUPPORTER);
                break;
            }
        });


        hl1.add(cmbPloegen);
        hl1.add(radioGroup, value);
        add(hl1);

        btnInschrijven = new Button("Toewijzen");
        btnInschrijven.setEnabled(true);
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
                lstToewijzing.setItems((Collection<ToewijzingDTO>) toewijzingMngr.getToewijzingList("").getBody());
                Notification.show("Persoon toegewezen", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                this.close();
            } catch (IllegalArgumentException | NotFoundException | InvalidCredentialsException ex) {
                Notification.show(ex.getMessage(), 5000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        add(btnInschrijven);
    }

}

