package be.ucll.java.gip5.model;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name ="persoon", schema = "gip5")
public class Persoon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "voornaam")
    private String voornaam;

    @Column(name = "naam")
    private String naam;

    @Column(name = "geboortedatum")
    private Date geboortedatum;

    @Column(name = "geslacht")
    private Boolean geslacht;

    @Column(name = "adres")
    private String adres;

    @Column(name = "telefoon")
    private String telefoon;

    @Column(name = "gsm")
    private String gsm;

    @Column(name = "email")
    private String email;

    @Column(name = "wachtwoord")
    private String wachtwoord;

    @Column(name = "ploegId")
    private Long ploegId;

    @Column(name = "rolId")
    private Long rolId;

    private Persoon(PersoonBuilder builder){

    }

    public Persoon(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Boolean getGeslacht() {
        return geslacht;
    }

    public void setGeslacht(Boolean geslacht) {
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

    public Long getPloegId() {
        return ploegId;
    }

    public void setPloegId(Long ploegId) {
        this.ploegId = ploegId;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    public static final class PersoonBuilder {
        private Long id;
        private String voornaam;
        private String naam;
        private Date geboortedatum;
        private Boolean geslacht;
        private String adres;
        private String telefoon;
        private String gsm;
        private String email;
        private String wachtwoord;
        private Long ploegId;
        private Long rolId;

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
            this.ploegId = copy.ploegId;
            this.rolId = copy.rolId;
        }

        public PersoonBuilder id(Long id){
            this.id = id;
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
        public PersoonBuilder geslacht(boolean geslacht){
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
        }public PersoonBuilder ploegId(Long ploegId){
            this.ploegId = ploegId;
            return this;
        }public PersoonBuilder rolId(Long rolId){
            this.rolId = rolId;
            return this;
        }
        public Persoon build(){return new Persoon(this);}
    }
}
