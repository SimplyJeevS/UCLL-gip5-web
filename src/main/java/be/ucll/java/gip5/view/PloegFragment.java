package be.ucll.java.gip5.view;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDate;
import java.util.Locale;

public class PloegFragment extends FormLayout {
    // Public fields for ease of access
    public Label lblID;
    public TextField txtNaam;
    public TextField txtOmschrijving;



    public PloegFragment() {
        super();

        lblID = new Label("");


        txtNaam = new TextField();
        txtNaam.setRequired(true);
        txtNaam.setMaxLength(128);
        txtNaam.setErrorMessage("Verplicht veld");

        txtOmschrijving = new TextField();
        txtOmschrijving.setRequired(true);
        txtOmschrijving.setMaxLength(128);
        txtOmschrijving.setErrorMessage("Verplicht veld");


        addFormItem(txtNaam, "Naam");
        addFormItem(txtOmschrijving, "Omschrijving");
    }

    public void resetForm() {
        lblID.setText("");
        txtNaam.clear();
        txtNaam.setInvalid(false);
        txtOmschrijving.clear();
        txtOmschrijving.setInvalid(false);
    }

    public boolean isformValid() {
        boolean result = true;
        if (txtNaam.getValue() == null) {
            txtNaam.setInvalid(true);
            result = false;
        }
        if (txtNaam.getValue().trim().length() == 0) {
            txtNaam.setInvalid(true);
            result = false;
        }
        if (txtOmschrijving.getValue() == null) {
            txtOmschrijving.setInvalid(true);
            result = false;
        }
        if (txtOmschrijving.getValue().trim().length() == 0) {
            txtOmschrijving.setInvalid(true);
            result = false;
        }
        return result;
    }
}
