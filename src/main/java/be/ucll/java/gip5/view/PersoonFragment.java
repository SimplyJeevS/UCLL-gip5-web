package be.ucll.java.gip5.view;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDate;
import java.util.Locale;


public class PersoonFragment extends FormLayout {

    // Public fields for ease of access
    public Label lblID;
    public TextField txtVoornaam;
    public TextField txtNaam;
    public PasswordField txtWachtwoord;
    public DatePicker datGeboorte;
    public TextField txtGeslacht;
    public TextField txtAdres;
    public TextField txtTelefoon;
    public TextField txtGsm;
    public TextField txtEmail;



    public PersoonFragment() {
        super();

        lblID = new Label("");

        txtVoornaam = new TextField();
        txtVoornaam.setRequired(true);
        txtVoornaam.setMaxLength(128);
        txtVoornaam.setErrorMessage("Verplicht veld");

        txtNaam = new TextField();
        txtNaam.setRequired(true);
        txtNaam.setMaxLength(128);
        txtNaam.setErrorMessage("Verplicht veld");

        txtWachtwoord = new PasswordField();
        txtWachtwoord.setRequired(true);
        txtWachtwoord.setMaxLength(128);
        txtWachtwoord.setErrorMessage("Verplicht veld");

        txtGeslacht = new TextField();
        txtGeslacht.setRequired(true);
        txtGeslacht.setMaxLength(128);
        txtGeslacht.setErrorMessage("Verplicht veld");

        txtAdres = new TextField();
        txtAdres.setRequired(true);
        txtAdres.setMaxLength(128);
        txtAdres.setErrorMessage("Verplicht veld");

        txtTelefoon = new TextField();
        txtTelefoon.setRequired(true);
        txtTelefoon.setMaxLength(128);
        txtTelefoon.setErrorMessage("Verplicht veld");

        txtGsm = new TextField();
        txtGsm.setRequired(true);
        txtGsm.setMaxLength(128);
        txtGsm.setErrorMessage("Verplicht veld");

        txtEmail = new TextField();
        txtEmail.setRequired(true);
        txtEmail.setMaxLength(128);
        txtEmail.setErrorMessage("Verplicht veld");


        datGeboorte = new DatePicker();
        LocalDate now = LocalDate.now();
        datGeboorte.setPlaceholder("dd/mm/jjjj");
        //datGeboorte.setValue(now);
        datGeboorte.setMin(now.minusYears(100));
        datGeboorte.setMax(now);
        datGeboorte.setRequired(true);
        datGeboorte.addInvalidChangeListener(e -> datGeboorte.setErrorMessage("Verplicht veld. Ongeldig datumformaat of datum in de toekomst"));
        datGeboorte.setLocale(new Locale("nl", "BE"));
        datGeboorte.setClearButtonVisible(true);

        addFormItem(txtVoornaam, "Voornaam");
        addFormItem(txtNaam, "Naam");
        addFormItem(txtWachtwoord, "Wachtwoord");
        addFormItem(datGeboorte, "Geboortedatum");
        addFormItem(txtGeslacht, "Geslacht");
        addFormItem(txtAdres, "Adres");
        addFormItem(txtTelefoon, "Telefoon");
        addFormItem(txtGsm, "Gsm");
        addFormItem(txtEmail, "Email");
    }

    public void resetForm() {
        lblID.setText("");
        txtVoornaam.clear();
        txtVoornaam.setInvalid(false);
        txtNaam.clear();
        txtNaam.setInvalid(false);
        txtWachtwoord.clear();
        txtWachtwoord.setInvalid(false);
        datGeboorte.clear();
        datGeboorte.setInvalid(false);
        txtGeslacht.clear();
        txtGeslacht.setInvalid(false);
        txtAdres.clear();
        txtAdres.setInvalid(false);
        txtTelefoon.clear();
        txtTelefoon.setInvalid(false);
        txtGsm.clear();
        txtGsm.setInvalid(false);
        txtEmail.clear();
        txtEmail.setInvalid(false);
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
        if (txtVoornaam.getValue() == null) {
            txtVoornaam.setInvalid(true);
            result = false;
        }
        if (txtVoornaam.getValue().trim().length() == 0) {
            txtVoornaam.setInvalid(true);
            result = false;
        }
        if (txtWachtwoord.getValue() == null) {
            txtWachtwoord.setInvalid(true);
            result = false;
        }
        if (txtWachtwoord.getValue().trim().length() == 0) {
            txtWachtwoord.setInvalid(true);
            result = false;
        }
        if (datGeboorte.getValue() == null) {
            datGeboorte.setInvalid(true);
            result = false;
        }
        if (txtGeslacht.getValue() == null) {
            txtGeslacht.setInvalid(true);
            result = false;
        }
        if (txtAdres.getValue() == null) {
            txtAdres.setInvalid(true);
            result = false;
        }
        if (txtTelefoon.getValue() == null) {
            txtTelefoon.setInvalid(true);
            result = false;
        }
        if (txtGsm.getValue() == null) {
            txtGsm.setInvalid(true);
            result = false;
        }
        if (txtEmail.getValue() == null) {
            txtEmail.setInvalid(true);
            result = false;
        }
        return result;
    }
}




