package be.ucll.java.gip5.model;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name ="persoon", schema = "gip5")
public class Persoon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "voornaam")
    private String voornaam;

    @Column(name = "naam")
    private String naam;

    @Column(name = "geboortedatum")
    private Date geboortedatum;

    @Column(name = "geslacht")
    private String geslacht;

    @Column(name = "adres")
    private String adres;

    @Column(name = "telefoon", length=15) //met lengte zetten
    private String telefoon;

    @Column(name = "gsm")
    private String gsm;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "wachtwoord")
    private String wachtwoord;

    @Column(name="default_rol")
    private Rol default_rol;

    @Column(name="api")
    private String api;

    private Persoon(PersoonBuilder builder){
        setWachtwoord(builder.wachtwoord);
        setGsm(builder.gsm);
        setGeboortedatum(builder.geboortedatum);
        setEmail(builder.email);
        setDefault_rol(builder.default_rol);
        setAdres(builder.adres);
        setGeslacht(builder.geslacht);
        setId(builder.id);
        setEmail(builder.email);
        setGsm(builder.gsm);
        setNaam(builder.naam);
        setVoornaam(builder.voornaam);
        setTelefoon(builder.telefoon);
        setApi(builder.api);
    }

    public Persoon(){};

    public String getApi(){return api;}
    private void setApi(String api){
        this.api = api;
    }

    public Rol getDefault_rol() {
        return default_rol;
    }

    public void setDefault_rol(Rol default_rol) {
        this.default_rol = Rol.GUEST;
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public String getGeslacht() {
        return geslacht;
    }

    public void setGeslacht(String geslacht) {
        this.geslacht = geslacht;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getTelefoon() {
        return telefoon;
    }

    public void setTelefoon(String telefoon) {
        this.telefoon = telefoon;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public static final class PersoonBuilder {
        private Long id;
        private String voornaam;
        private String naam;
        private Date geboortedatum;
        private String geslacht;
        private String adres;
        private String telefoon;
        private String gsm;
        private String email;
        private String wachtwoord;
        private Rol default_rol;
        private String api;

        public PersoonBuilder() {
        }

        public static PersoonBuilder Persoon() {
            return new PersoonBuilder();
        }
        public PersoonBuilder(Persoon copy){
            this.id = copy.id;
            this.voornaam = copy.voornaam;
            this.naam = copy.naam;
            this.geboortedatum = copy.geboortedatum;
            this.geslacht = copy.geslacht;
            this.adres = copy.adres;
            this.telefoon = copy.telefoon;
            this.gsm = copy.gsm;
            this.email = copy.email;
            this.wachtwoord = copy.wachtwoord;
            this.default_rol = copy.default_rol;
            this.api = copy.api;
        }

        public PersoonBuilder id(Long id){
            this.id = id;
            return this;
        }
        public PersoonBuilder api(){
            this.api = UUID.randomUUID().toString().replace("-", "");
            return this;
        }
        public PersoonBuilder voornaam(String voornaam){
            this.voornaam = voornaam;
            return this;
        }
        public PersoonBuilder naam(String naam){
            this.naam = naam;
            return this;
        }
        public PersoonBuilder geboortedatum(Date geboortedatum){
            this.geboortedatum = geboortedatum;
            return this;
        }
        public PersoonBuilder geslacht(String geslacht){
            this.geslacht = geslacht;
            return this;
        }
        public PersoonBuilder adres(String adres){
            this.adres = adres;
            return this;
        }
        public PersoonBuilder telefoon(String telefoon){
            this.telefoon = telefoon;
            return this;
        }public PersoonBuilder gsm(String gsm){
            this.gsm = gsm;
            return this;
        }public PersoonBuilder email(String email){
            this.email = email;
            return this;
        }public PersoonBuilder wachtwoord(String wachtwoord){
            this.wachtwoord = wachtwoord;
            return this;
        }
        public PersoonBuilder default_rol(){
            this.default_rol = Rol.GUEST;
            return this;
        }
        public Persoon build(){return new Persoon(this);}
    }
}
