package be.ucll.java.gip5.view;

import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;import java.awt.*;
import com.vaadin.flow.component.textfield.PasswordField;import java.time.LocalDate;
import com.vaadin.flow.component.textfield.TextField;import java.time.LocalDateTime;
import java.util.Locale;

public class WedstrijdFragment extends FormLayout {

    public Label lblID;
    public TextField txtLocatie;
    public DateTimePicker datTijdstip;
    public TextField txtTegenstander;
    public TextField txtThuisploeg;

    public WedstrijdFragment() {
        super();

        lblID = new Label("");

        txtLocatie = new TextField();
        txtLocatie.setRequired(true);
        txtLocatie.setMaxLength(128);
        txtLocatie.setErrorMessage("Verplicht veld");

        txtTegenstander = new TextField();
        txtTegenstander.setRequired(true);
        txtTegenstander.setMaxLength(128);
        txtTegenstander.setErrorMessage("Verplicht veld");


        txtThuisploeg = new TextField();
        txtThuisploeg.setRequired(true);
        txtThuisploeg.setMaxLength(128);
        txtThuisploeg.setErrorMessage("Verplicht veld");


        datTijdstip = new DateTimePicker();
        LocalDate now = LocalDate.now();
        datTijdstip.setDatePlaceholder("dd/mm/jjjj");
        datTijdstip.setTimePlaceholder("H:MM");

        addFormItem(txtLocatie, "Locatie");
        addFormItem(txtThuisploeg, "Thuisploeg");
        addFormItem(txtTegenstander, "Tegenstander");
        addFormItem(datTijdstip, "Tijdstip");
    }

    public void resetForm() {
        lblID.setText("");
        txtLocatie.clear();
        txtLocatie.setInvalid(false);
        txtThuisploeg.clear();
        txtThuisploeg.setInvalid(false);
        txtTegenstander.clear();
        txtTegenstander.setInvalid(false);
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
        if (txtThuisploeg.getValue() == null) {
            txtThuisploeg.setInvalid(true);
            result = false;
        }
        if (txtTegenstander.getValue().trim().length() == 0) {
            txtTegenstander.setInvalid(true);
            result = false;
        }
        if (datTijdstip.getValue() == null) {
            datTijdstip.setInvalid(true);
            result = false;
        }
        return result;
    }
}
