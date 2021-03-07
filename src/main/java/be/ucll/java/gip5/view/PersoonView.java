package be.ucll.java.gip5.view;

import be.ucll.java.gip5.dto.PersoonDTO;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.rest.PersoonResource;
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
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@UIScope
public class PersoonView extends VerticalLayout {

    // Spring controllers
    private final PersoonResource persoonResource;
    private final MessageSource msgSource;

    private SplitLayout splitLayout;
    private VerticalLayout lpvLayout; // Left Panel Vertical Layout
    private HorizontalLayout lphLayout;
    private VerticalLayout rpvLayout; // Right Panel Vertical Layout
    private HorizontalLayout rphLayout;
    private PersoonFragment frm;

    private Label lblNaam;
    private TextField txtNaam;

    private Grid<PersoonDTO> grid;

    private Button btnCancel;
    private Button btnCreate;
    private Button btnUpdate;
    private Button btnDelete;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public PersoonView() {
        super();

        persoonResource = BeanUtil.getBean(PersoonResource.class);
        persoonResource.setLocale(VaadinSession.getCurrent().getLocale());
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
        lblNaam = new Label("search"); //new Label(msgSource.getMessage("persoonresource.lblNaam", null, getLocale()));
        txtNaam = new TextField();
        txtNaam.setValueChangeMode(ValueChangeMode.EAGER);
        txtNaam.addValueChangeListener(e -> handleClickSearch(null));
        txtNaam.setClearButtonVisible(true);
        lphLayout.add(lblNaam);
        lphLayout.add(txtNaam);

        grid = new Grid<>();
        grid.setItems(new ArrayList<PersoonDTO>(0));
        grid.addColumn(PersoonDTO::getVoornaam).setHeader("Voornaam").setSortable(true);
        //grid.addColumn(persoon -> persoon.getVoornaam()).setHeader("Voornaam").setSortable(true);
        grid.addColumn(PersoonDTO::getNaam).setHeader("Naam").setSortable(true);
        //grid.addColumn(PersoonDTO::getGeboortedatum).setHeader("Geboortedatum");
        grid.addColumn(PersoonDTO::getGeslacht).setHeader("Geslacht").setSortable(true);
        grid.addColumn(PersoonDTO::getAdres).setHeader("Adres").setSortable(true);
        grid.addColumn(PersoonDTO::getTelefoon).setHeader("Telefoon").setSortable(true);
        grid.addColumn(PersoonDTO::getGsm).setHeader("Gsm").setSortable(true);
        grid.addColumn(PersoonDTO::getEmail).setHeader("E-mail").setSortable(true);
        grid.addColumn(PersoonDTO::getGeboortedatum).setHeader("Geboortedatum").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(pers -> {
            Button b = new Button(new Icon(VaadinIcon.BULLETS));
            b.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            //b.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("inschrijvingen/" + pers.getId())));
            b.addClickListener(e -> {
                ToewijzingDialog dialog = new ToewijzingDialog(pers);
                UI.getCurrent().getPage().retrieveExtendedClientDetails(receiver -> {
                    int browserInnerWindowsWidth = receiver.getWindowInnerWidth();
                    int browserInnerWindowsHeight = receiver.getWindowInnerHeight();

                    dialog.setWidth((browserInnerWindowsWidth / 2) + "px");
                    dialog.setHeight((browserInnerWindowsHeight / 2) + "px");
                });
                dialog.open();
            });
            return b;
        })).setHeader("Toewijzing");
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

        frm = new PersoonFragment();

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

        rphLayout.add(btnCancel, btnCreate, btnUpdate, btnDelete);

        rpvLayout.add(frm);
        rpvLayout.add(rphLayout);
        rpvLayout.setWidth("30%");

        return rpvLayout;
    }

    public void loadData() {
        if (persoonResource != null) {
            List<PersoonDTO> lst = persoonResource.getAllPersonen();
            grid.setItems(lst);
        } else {
            System.err.println("Autowiring failed");
        }
    }

    private void handleClickSearch(ClickEvent event) {
        if (txtNaam.getValue().trim().length() == 0) {
            grid.setItems(persoonResource.getAllPersonen());
        } else {
            String searchterm = txtNaam.getValue().trim();
            grid.setItems(persoonResource.getSearchPersonen(searchterm));
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
            Date d = Date.from(frm.datGeboorte.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

            PersoonDTO s = new PersoonDTO( frm.txtVoornaam.getValue(),frm.txtNaam.getValue(),frm.txtWachtwoord.getValue(), java.sql.Date.valueOf(frm.datGeboorte.getValue()) ,frm.txtGeslacht.getValue(),frm.txtAdres.getValue(),frm.txtTelefoon.getValue(),frm.txtGsm.getValue(),frm.txtEmail.getValue());
            ResponseEntity i = persoonResource.postPersoon(s, "");

            Notification.show("Persoon created (id: " + i + ")", 3000, Notification.Position.TOP_CENTER);
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
            Date d = Date.from(frm.datGeboorte.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            //PersoonDTO p = new PersoonDTO(frm.txtVoornaam.getValue(), frm.txtNaam.getValue(), frm.datGeboorte.getValue(), frm.txtGeslacht.getValue(), frm.txtAdres.getValue(), frm.txtTelefoon.getValue(), frm.txtGsm.getValue(), frm.txtEmail.getValue());
            //persoonResource.putPersoon(p);

            Notification.show("Persoon aangepast", 3000, Notification.Position.TOP_CENTER);
            frm.resetForm();
            handleClickSearch(null);
        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage(), 5000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void handleClickDelete(ClickEvent event) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);

        Button confirmButton = new Button("Bevestig Persoon verwijderen", event2 -> {
            try {
                persoonResource.deletePersoon(Long.parseLong(frm.lblID.getText()), "");
                Notification.show("Persoon verwijderd", 3000, Notification.Position.TOP_CENTER);
            } catch (IllegalArgumentException e) {
                Notification.show("Het is NIET mogelijk de persson te verwijderen wegens geregistreerde toewijzigingen.", 5000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ParameterInvalidException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (InvalidCredentialsException e) {
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

    private void populateForm(PersoonDTO p) {
        btnCreate.setVisible(true);
        btnUpdate.setVisible(true);
        btnDelete.setVisible(true);

        if (p != null) {
            // Copy the ID in a hidden field
            frm.lblID.setText(p.getId().toString());

            if (p.getVoornaam() != null) {
                frm.txtVoornaam.setValue(p.getVoornaam());
            } else {
                frm.txtVoornaam.setValue("");
            }
            if (p.getNaam() != null) {
                frm.txtNaam.setValue(p.getNaam());
            } else {
                frm.txtNaam.setValue("");
            }
            if (p.getWachtwoord() != null) {
                frm.txtWachtwoord.setValue(p.getWachtwoord());
            } else {
                frm.txtWachtwoord.setValue("");
            }
            if (p.getGeslacht() != null) {
                frm.txtGeslacht.setValue(p.getGeslacht());
            } else {
                frm.txtGeslacht.setValue("");
            }
            if (p.getAdres() != null) {
                frm.txtAdres.setValue(p.getAdres());
            } else {
                frm.txtAdres.setValue("");
            }
            if (p.getTelefoon() != null) {
                frm.txtTelefoon.setValue(p.getTelefoon());
            } else {
                frm.txtTelefoon.setValue("");
            }
            if (p.getGsm() != null) {
                frm.txtGsm.setValue(p.getGsm());
            } else {
                frm.txtGsm.setValue("");
            }
            if (p.getEmail() != null) {
                frm.txtEmail.setValue(p.getEmail());
            } else {
                frm.txtEmail.setValue("");
            }

            if (p.getGeboortedatum() != null) {
                try {
                    frm.datGeboorte.setValue(p.getGeboortedatum().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                } catch (NullPointerException e) {
                    frm.datGeboorte.setValue(null);
                }
            } else {
                frm.datGeboorte.setValue(null);
            }
        }
    }

}


