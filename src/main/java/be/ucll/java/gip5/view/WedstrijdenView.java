package be.ucll.java.gip5.view;

import be.ucll.java.gip5.dao.DeelnameRepository;
import be.ucll.java.gip5.dao.WedstrijdRepository;
import be.ucll.java.gip5.dto.DeelnameDTO;
import be.ucll.java.gip5.dto.WedstrijdDTO;
import be.ucll.java.gip5.dto.WedstrijdMetPloegenDTO;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Deelname;
import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.model.Status;
import be.ucll.java.gip5.rest.DeelnameResource;
import be.ucll.java.gip5.rest.PersoonResource;
import be.ucll.java.gip5.rest.WedstrijdResource;
import be.ucll.java.gip5.util.BeanUtil;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@UIScope
public class WedstrijdenView extends VerticalLayout {
    // Spring controllers
    private final WedstrijdResource wedstrijdResource;
    private WedstrijdRepository wedstrijdRepository;
    private final MessageSource msgSource;
    private PersoonResource persoonResource;
    private DeelnameResource deelnameResource;
    private DeelnameRepository deelnameRepository;
    private DeelnameDTO selectedDeelname = new DeelnameDTO();

    private SplitLayout splitLayout;
    private VerticalLayout lpvLayout; // Left Panel Vertical Layout
    private HorizontalLayout lphLayout;
    private VerticalLayout rpvLayout; // Right Panel Vertical Layout
    private HorizontalLayout rphLayout;
    private WedstrijdFragment frm;

    private Label lblLocatie;
    private TextField txtLocatie;

    private Grid<WedstrijdMetPloegenDTO> grid;

    private Button btnCancel;
    private Button btnCreate;
    private Button btnUpdate;
    private Button btnDelete;
    private Button btnStatus;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public WedstrijdenView() {
        super();

        wedstrijdRepository = BeanUtil.getBean(WedstrijdRepository.class);
        wedstrijdResource = BeanUtil.getBean(WedstrijdResource.class);
        persoonResource = BeanUtil.getBean(PersoonResource.class);
        deelnameResource = BeanUtil.getBean(DeelnameResource.class);
        deelnameRepository = BeanUtil.getBean(DeelnameRepository.class);
        msgSource = BeanUtil.getBean(MessageSource.class);
        wedstrijdResource.setLocale(VaadinSession.getCurrent().getLocale());

        this.setSizeFull();
        this.setPadding(false);

        splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.addToPrimary(createGridLayout());
        splitLayout.addToSecondary(createEditorLayout());
        add(splitLayout);
    }

    private Component createGridLayout() {
        lpvLayout = new VerticalLayout();
        lpvLayout.setWidthFull();

        lphLayout = new HorizontalLayout();
        lblLocatie = new Label("search"); //new Label(msgSource.getMessage("persoonresource.lblNaam", null, getLocale()));
        txtLocatie = new TextField();
        txtLocatie.setValueChangeMode(ValueChangeMode.EAGER);
        txtLocatie.addValueChangeListener(e -> handleClickSearch(null));
        txtLocatie.setClearButtonVisible(true);
        lphLayout.add(lblLocatie);
        lphLayout.add(txtLocatie);

        grid = new Grid<>();
        grid.setItems(new ArrayList<WedstrijdMetPloegenDTO>(0));
        grid.addColumn(WedstrijdMetPloegenDTO::getLocatie).setHeader("Locatie").setSortable(true);
        grid.addColumn(WedstrijdMetPloegenDTO::getThuisploeg).setHeader("Thuisploeg").setSortable(true);
        grid.addColumn(WedstrijdMetPloegenDTO::getTegenstander).setHeader("Tegenstander").setSortable(true);
        grid.addColumn(WedstrijdMetPloegenDTO::getTijdstip).setHeader("Tijdstip").setSortable(true);

        grid.setHeightFull();

        //when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

        lpvLayout.add(lphLayout);
        lpvLayout.add(grid);
        lpvLayout.setWidth("70%");
        return lpvLayout;
    }



    private Component createEditorLayout() {
        rpvLayout = new VerticalLayout();

        frm = new WedstrijdFragment();

        rphLayout = new HorizontalLayout();
        rphLayout.setWidthFull();
        rphLayout.setSpacing(true);

        btnCancel = new Button("Annuleren");
        btnCancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnCancel.addClickListener(e -> handleClickCancel(e));

        btnCreate = new Button("Toevoegen");
        btnCreate.addClickListener(e -> handleClickCreate(e));

        btnUpdate = new Button("Opslaan");
        btnUpdate.addClickListener(e -> handleClickUpdate(e));
        btnUpdate.setVisible(false);

        btnDelete = new Button("Verwijderen");
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDelete.addClickListener(e -> handleClickDelete(e));
        btnDelete.setVisible(false);

        btnStatus = new Button("Status");
        btnStatus.addClickListener(e -> {
            try {
                handleClickStatus(e);
            } catch (ParameterInvalidException ex) {
                ex.printStackTrace();
            } catch (NotFoundException ex) {
                ex.printStackTrace();
            } catch (InvalidCredentialsException ex) {
                ex.printStackTrace();
            }
        });

        rphLayout.add(btnCancel, btnCreate, btnUpdate, btnDelete, btnStatus);

        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("Status");
        radioGroup.setItems("Beschikbaar", "Onbeschikbaar", "Reserve");
        radioGroup.addThemeVariants(RadioGroupVariant.LUMO_HELPER_ABOVE_FIELD);

        Div value = new Div();
        radioGroup.addValueChangeListener(event -> {
            Persoon currentPersoon = new Persoon();
            try {
                currentPersoon = (Persoon) persoonResource.getLoginInfo("").getBody();
            } catch (InvalidCredentialsException e) {
                e.printStackTrace();
            }
            selectedDeelname.setPersoonId(currentPersoon.getId());
            selectedDeelname.setWedstrijdId(Long.parseLong(frm.lblID.getText()));
            switch (event.getValue())
            {
                case "Beschikbaar":  selectedDeelname.setStatus(Status.AVAILABLE);
                    break;
                case "Onbeschikbaar": selectedDeelname.setStatus(Status.UNAVAILABLE);
                    break;
                case "Reserve": selectedDeelname.setStatus(Status.RESERVE);
                    break;
            }
        });

        rpvLayout.add(frm);
        rpvLayout.add(radioGroup);
        rpvLayout.add(rphLayout);
        rpvLayout.setWidth("30%");

        return rpvLayout;
    }




    public void loadData() {
        if (wedstrijdResource != null) {
            List<WedstrijdMetPloegenDTO> lst = wedstrijdResource.getAllWedstrijden();
            grid.setItems(lst);
        } else {
            System.err.println("Autowiring failed");
        }
    }

    private void handleClickSearch(Object o) {
        if (txtLocatie.getValue().trim().length() == 0) {
            grid.setItems(wedstrijdResource.getAllWedstrijden());
        } else {
            String searchterm = txtLocatie.getValue().trim();
            grid.setItems(wedstrijdResource.getSearchWedstrijden(searchterm));
        }
    }
    private void handleClickCancel(ClickEvent event) {
        grid.asSingleSelect().clear();
        frm.resetForm();
        btnCreate.setVisible(true);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
    }
    private void handleClickCreate(ClickEvent event) {
        if (!frm.isformValid()) {
            Notification.show("Er zijn validatiefouten", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            LocalDateTime d = frm.datTijdstip.getValue();

            WedstrijdDTO w = new WedstrijdDTO(d, frm.txtLocatie.getValue(), frm.selectedTegenstander.getId(), frm.selectedThuis.getId());
            ResponseEntity i = wedstrijdResource.postWedstrijd(w, "");

            Notification.show("Wedstrijd created (id: " + i + ")", 3000, Notification.Position.TOP_CENTER);
            frm.resetForm();
            handleClickSearch(null);
        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage(), 5000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
        } catch (ParameterInvalidException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (InvalidCredentialsException e) {
            e.printStackTrace();
        }
    }
    private void handleClickUpdate(ClickEvent event) {
        if (!frm.isformValid()) {
            Notification.show("Er zijn validatiefouten", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            LocalDateTime d = frm.datTijdstip.getValue();

            WedstrijdDTO w = new WedstrijdDTO(d, frm.txtLocatie.getValue(), frm.selectedTegenstander.getId(), frm.selectedThuis.getId());
            wedstrijdResource.putWedstrijd(Long.parseLong(frm.lblID.getText()), w, "");
            Notification.show("Wedstrijd aangepast", 3000, Notification.Position.TOP_CENTER);
            frm.resetForm();
            handleClickSearch(null);
        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage(), 5000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
        } catch (ParameterInvalidException e) {
            e.printStackTrace();
        } catch (InvalidCredentialsException e) {
            Notification.show("Je bent niet ingelogd", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            UI.getCurrent().navigate("login");
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
    private void handleClickDelete(ClickEvent event) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);

        Button confirmButton = new Button("Bevestig Wedstrijd verwijderen", event2 -> {
            try {
                wedstrijdResource.deleteWedstrijd(Long.parseLong(frm.lblID.getText()), "");
                Notification.show("Wedstrijd verwijderd", 3000, Notification.Position.TOP_CENTER);
            } catch (IllegalArgumentException e) {
                Notification.show("Het is NIET mogelijk de wedstrijd te verwijderen wegens geregistreerde toewijzigingen.", 5000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ParameterInvalidException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (InvalidCredentialsException e) {
                Notification.show("Je bent niet ingelogd", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                UI.getCurrent().navigate("login");
                e.printStackTrace();
            }
            frm.resetForm();
            handleClickSearch(null);
            btnCreate.setVisible(true);
            btnUpdate.setVisible(false);
            btnDelete.setVisible(false);

            dialog.close();
        });
        Button cancelButton = new Button("Cancel", event2 -> {
            dialog.close();
        });

        dialog.add(confirmButton, new Html("<span>&nbsp;</span>"), cancelButton);

        dialog.open();
    }
    private void handleClickStatus(ClickEvent event) throws ParameterInvalidException, NotFoundException, InvalidCredentialsException {
        DeelnameDTO deelnameDTO = new DeelnameDTO(selectedDeelname.getPersoonId(), selectedDeelname.getWedstrijdId(), selectedDeelname.getStatus());
        if(deelnameRepository.findByPersoonIdAndWedstrijdId(selectedDeelname.getPersoonId(), selectedDeelname.getWedstrijdId()).isPresent())
        {
            Optional<Deelname> foundDeelname = deelnameRepository.findByPersoonIdAndWedstrijdId(selectedDeelname.getPersoonId(), selectedDeelname.getWedstrijdId());
            ResponseEntity i = deelnameResource.putDeelname(foundDeelname.get().getId(), deelnameDTO, "");
            Notification.show("Deelname updated met (id: " + i + ")", 3000, Notification.Position.TOP_CENTER);
        }

        else
        {
            ResponseEntity i = deelnameResource.postDeelname(deelnameDTO, "");
            Notification.show("Deelname created (id: " + i + ")", 3000, Notification.Position.TOP_CENTER);
        }

        frm.resetForm();
        handleClickSearch(null);

    }
    private void populateForm(WedstrijdMetPloegenDTO w) {
        btnCreate.setVisible(false);
        btnUpdate.setVisible(true);
        btnDelete.setVisible(true);

        if (w != null) {
            // Copy the ID in a hidden field
            if (w.getId() != null) {
                frm.lblID.setText(w.getId().toString());
            } else {
                frm.lblID.setText("");
            }

            if (w.getLocatie() != null) {
                frm.txtLocatie.setValue(w.getLocatie());
            } else {
                frm.txtLocatie.setValue("");
            }
            if (w.getThuisploeg() != null) {
                frm.cmbThuisPloegen.setPlaceholder(w.getThuisploeg());
            } else {
                frm.cmbThuisPloegen.setPlaceholder("Kies een thuisploeg");
            }
            if (w.getTegenstander() != null) {
                frm.cmbTegenstanders.setPlaceholder(w.getTegenstander());
            } else {
                frm.cmbTegenstanders.setPlaceholder("Kies een thuisploeg");
            }
            if (w.getTijdstip() != null) {
                try {
                    frm.datTijdstip.setValue(w.getTijdstip());
                } catch (NullPointerException e) {
                    frm.datTijdstip.setValue(null);
                }
            } else {
                frm.datTijdstip.setValue(null);
            }
        }
    }

}
