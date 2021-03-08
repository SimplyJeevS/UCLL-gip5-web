package be.ucll.java.gip5.view;

import be.ucll.java.gip5.dto.PloegDTO;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.exceptions.NotFoundException;
import be.ucll.java.gip5.model.Ploeg;
import be.ucll.java.gip5.rest.PloegResource;
import be.ucll.java.gip5.util.BeanUtil;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;import java.awt.*;
import com.vaadin.flow.component.textfield.PasswordField;import java.time.LocalDate;
import com.vaadin.flow.component.textfield.TextField;
import org.apache.commons.lang3.builder.Diff;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WedstrijdFragment extends FormLayout {

    private PloegResource PloegMngr;
    public Label lblID;
    public TextField txtLocatie;
    public DateTimePicker datTijdstip;
    public ComboBox<PloegDTO> cmbThuisPloegen;
    public ComboBox<PloegDTO> cmbTegenstanders;

    public PloegDTO selectedThuis;
    public PloegDTO selectedTegenstander;

    public WedstrijdFragment() {
        super();

        PloegMngr = BeanUtil.getBean(PloegResource.class);
        lblID = new Label("");

        txtLocatie = new TextField();
        txtLocatie.setRequired(true);
        txtLocatie.setMaxLength(128);
        txtLocatie.setErrorMessage("Verplicht veld");

        cmbThuisPloegen = new ComboBox<>();
        cmbThuisPloegen.setItemLabelGenerator(PloegDTO::getNaam);
        try {
            java.util.List<Ploeg> ploegen = (List<Ploeg>) PloegMngr.getPloegen("").getBody();
            ArrayList<PloegDTO> ploegDTOList = new ArrayList<>();
            for (int i = 0; i < ploegen.size(); i++){
                PloegDTO ploeg = new PloegDTO(ploegen.get(i).getId(),ploegen.get(i).getNaam());
                ploegDTOList.add(ploeg);
            }
            cmbThuisPloegen.setItems(ploegDTOList);
        } catch (NotFoundException | InvalidCredentialsException e) {
            e.printStackTrace();
        }
        cmbThuisPloegen.setWidth("500px");
        Div Value = new Div();
        Value.setText("kies een ploeg");
        cmbThuisPloegen.addValueChangeListener(event -> {
            Value.setText("geselecteerde ploeg: " + event.getValue().getId());
            selectedThuis = event.getValue();
            selectedThuis.setId(event.getValue().getId());
        });
        cmbTegenstanders = new ComboBox<>();
        cmbTegenstanders.setItemLabelGenerator(PloegDTO::getNaam);
        try {
            java.util.List<Ploeg> ploegen = (List<Ploeg>) PloegMngr.getPloegen("").getBody();
            ArrayList<PloegDTO> ploegDTOList = new ArrayList<>();
            for (int i = 0; i < ploegen.size(); i++){
                PloegDTO ploeg = new PloegDTO(ploegen.get(i).getId(), ploegen.get(i).getNaam());
                ploegDTOList.add(ploeg);
            }
            cmbTegenstanders.setItems(ploegDTOList);
        } catch (NotFoundException | InvalidCredentialsException e) {
            e.printStackTrace();
        }
        cmbTegenstanders.setWidth("500px");
        cmbTegenstanders.addValueChangeListener(event -> {
            selectedTegenstander = event.getValue();
            selectedTegenstander.setId(event.getValue().getId());
        });


        datTijdstip = new DateTimePicker();
        LocalDate now = LocalDate.now();
        datTijdstip.setDatePlaceholder("dd/mm/jjjj");
        datTijdstip.setTimePlaceholder("H:MM");

        addFormItem(txtLocatie, "Locatie");
        addFormItem(cmbThuisPloegen, "Thuisploeg");
        addFormItem(cmbTegenstanders, "Tegenstander");
        addFormItem(datTijdstip, "Tijdstip");
    }

    public void resetForm() {
        lblID.setText("");
        txtLocatie.clear();
        txtLocatie.setInvalid(false);
        cmbTegenstanders.clear();
        cmbTegenstanders.setInvalid(false);
        cmbThuisPloegen.clear();
        cmbThuisPloegen.setInvalid(false);
        datTijdstip.clear();
        datTijdstip.setInvalid(false);
    }

    public boolean isformValid() {
        boolean result = true;
        if (txtLocatie.getValue() == null) {
            txtLocatie.setInvalid(true);
            result = false;
        }
        if (txtLocatie.getValue().trim().length() == 0) {
            txtLocatie.setInvalid(true);
            result = false;
        }
        if (cmbThuisPloegen.getValue() == null) {
            cmbThuisPloegen.setInvalid(true);
            result = false;
        }
        if (cmbTegenstanders.getValue() == null) {
            cmbTegenstanders.setInvalid(true);
            result = false;
        }
        if (datTijdstip.getValue() == null) {
            datTijdstip.setInvalid(true);
            result = false;
        }
        return result;
    }
}
