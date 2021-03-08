package be.ucll.java.gip5.view;

import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.dao.WedstrijdRepository;
import be.ucll.java.gip5.dto.PersoonDTO;
import be.ucll.java.gip5.dto.WedstrijdDTO;
import be.ucll.java.gip5.dto.WedstrijdMetPloegenDTO;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.exceptions.ParameterInvalidException;
import be.ucll.java.gip5.model.Persoon;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@UIScope
public class WedstrijdenView extends VerticalLayout {
    // Spring controllers
    private final WedstrijdResource wedstrijdResource;
    private WedstrijdRepository wedstrijdRepository;
    private final MessageSource msgSource;

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

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public WedstrijdenView() {
        super();

        wedstrijdRepository = BeanUtil.getBean(WedstrijdRepository.class);
        wedstrijdResource = BeanUtil.getBean(WedstrijdResource.class);
        wedstrijdResource.setLocale(VaadinSession.getCurrent().getLocale());
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

        rphLayout.add(btnCancel, btnCreate, btnUpdate, btnDelete);

        rpvLayout.add(frm);
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

    private void handleClickSearch(ClickEvent event) {
        if (txtLocatie.getValue().trim().length() == 0) {
            grid.setItems(wedstrijdResource.getAllWedstrijden());
        } else {
            String searchterm = txtLocatie.getValue().trim();
            grid.setItems(wedstrijdResource.getSearchWedstrijden(searchterm));
        }
    }
    private void handleClickCancel(ClickEvent e) {
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
    private void handleClickUpdate(ClickEvent e) {
    }
    private void handleClickDelete(ClickEvent e) {
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
