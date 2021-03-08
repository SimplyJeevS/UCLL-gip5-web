package be.ucll.java.gip5.view;

import be.ucll.java.gip5.dao.PloegRepository;
import be.ucll.java.gip5.dto.PloegDTO;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.rest.PloegResource;
import be.ucll.java.gip5.util.BeanUtil;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@UIScope
public class PloegenView extends VerticalLayout {

    private final PloegResource ploegResource;
    private final PloegRepository ploegRepository;
    private final MessageSource msgSource;

    private SplitLayout splitLayout;
    private VerticalLayout lpvLayout; // Left Panel Vertical Layout
    private HorizontalLayout lphLayout;
    private VerticalLayout rpvLayout; // Right Panel Vertical Layout
    private HorizontalLayout rphLayout;
    private PloegFragment frm;

    private Label lblNaam;
    private TextField txtNaam;

    private Grid<PloegDTO> grid;
    private Grid<Persoon>  gridp;

    private Button btnCancel;
    private Button btnCreate;
    private Button btnUpdate;
    private Button btnDelete;
    private Button btnShowPlayers;
    public PloegenView() {
        super();

        ploegRepository = BeanUtil.getBean(PloegRepository.class);
        ploegResource = BeanUtil.getBean(PloegResource.class);
        ploegResource.setLocale(VaadinSession.getCurrent().getLocale());
        msgSource = BeanUtil.getBean(MessageSource.class);

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
        lblNaam = new Label("search");
        txtNaam = new TextField();
        txtNaam.setValueChangeMode(ValueChangeMode.EAGER);
        txtNaam.addValueChangeListener(e -> handleClickSearch(null));
        txtNaam.setClearButtonVisible(true);
        lphLayout.add(lblNaam);
        lphLayout.add(txtNaam);

        grid = new Grid<>();
        grid.setItems(new ArrayList<PloegDTO>(0));
        grid.addColumn(PloegDTO::getNaam).setHeader("Naam").setSortable(true);
        //when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

        lpvLayout.add(lphLayout);
        lpvLayout.add(grid);
        lpvLayout.setWidth("70%");
        return lpvLayout;
    }



    private Component createEditorLayout() {
        rpvLayout = new VerticalLayout();

        frm = new PloegFragment();

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

        btnShowPlayers = new Button("Toon spelers");
        btnShowPlayers.addClickListener(e -> {
            try {
                handleClickShowPLayers();
            } catch (InvalidCredentialsException ex) {
                ex.printStackTrace();
            } catch (NotFoundException ex) {
                ex.printStackTrace();
            }
        });

        rphLayout.add(btnCancel, btnCreate, btnUpdate, btnDelete, btnShowPlayers);

        rpvLayout.add(frm);
        rpvLayout.add(rphLayout);
        rpvLayout.setWidth("30%");

        return rpvLayout;
    }
    public void loadData() {
        if (ploegResource != null) {
            List<PloegDTO> lst = ploegResource.getAllPloegen();
            grid.setItems(lst);
        } else {
            System.err.println("Autowiring failed");
        }
    }
    private void handleClickSearch(Object o) {
        if (txtNaam.getValue().trim().length() == 0) {
            grid.setItems(ploegResource.getAllPloegen());
        } else {
            String searchterm = txtNaam.getValue().trim();
            grid.setItems(ploegResource.getSearchPloegen(searchterm));
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
            PloegDTO s = new PloegDTO( frm.txtNaam.getValue());
            ResponseEntity i = ploegResource.postPloeg(s, "");

            Notification.show("Ploeg created (id: " + i + ")", 3000, Notification.Position.TOP_CENTER);
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
        }
    }
    private void handleClickUpdate(ClickEvent event) {
        if (!frm.isformValid()) {
            Notification.show("Er zijn validatiefouten", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            PloegDTO pdto = new PloegDTO(frm.txtNaam.getValue());
            ploegResource.putPloeg(Long.parseLong(frm.lblID.getText()), pdto, "");

            Notification.show("Ploeg aangepast", 3000, Notification.Position.TOP_CENTER);
            frm.resetForm();
            handleClickSearch(null);
        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage(), 5000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
        } catch (ParameterInvalidException e) {
            e.printStackTrace();
        } catch (InvalidCredentialsException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
    private void handleClickDelete(ClickEvent event) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);

        Button confirmButton = new Button("Bevestig Ploeg verwijderen", event2 -> {
            try {
                ploegResource.deletePloeg(Long.parseLong(frm.lblID.getText()), "");
                Notification.show("Ploeg verwijderd", 3000, Notification.Position.TOP_CENTER);
            } catch (IllegalArgumentException e) {
                Notification.show("Het is NIET mogelijk de ploeg te verwijderen wegens geregistreerde toewijzigingen.", 5000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
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
    private void handleClickShowPLayers() throws InvalidCredentialsException, NotFoundException {
        List<Persoon> playersInPloeg = (List<Persoon>) ploegResource.getAllSpelersInPloeg(Long.parseLong(frm.lblID.getText()) ,"").getBody();
        if(gridp != null)
        lpvLayout.remove(gridp);
        gridp = new Grid<>();

        gridp.setItems(playersInPloeg);
        gridp.addColumn(Persoon::getVoornaam).setHeader("Voornaam").setSortable(true);
        gridp.addColumn(Persoon::getNaam).setHeader("Naam").setSortable(true);
        gridp.addColumn(Persoon::getGeslacht).setHeader("Geslacht").setSortable(true);
        gridp.addColumn(Persoon::getAdres).setHeader("Adres").setSortable(true);
        gridp.addColumn(Persoon::getTelefoon).setHeader("Telefoon").setSortable(true);
        gridp.addColumn(Persoon::getGsm).setHeader("Gsm").setSortable(true);
        gridp.addColumn(Persoon::getEmail).setHeader("E-mail").setSortable(true);
        gridp.addColumn(Persoon::getGeboortedatum).setHeader("Geboortedatum").setSortable(true);

        lpvLayout.add(gridp);
    }
    private void populateForm(PloegDTO p) {
        btnCreate.setVisible(false);
        btnUpdate.setVisible(true);
        btnDelete.setVisible(true);

        if (p != null) {
            frm.lblID.setText("" + p.getId());
            if (p.getNaam() != null){
                frm.txtNaam.setValue(p.getNaam());
            }
            else {
                frm.txtNaam.setValue("");
            }
        }

    }
}
