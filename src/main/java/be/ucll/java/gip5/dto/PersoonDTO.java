package be.ucll.java.gip5.dto;

import be.ucll.java.gip5.model.Rol;

import java.util.Date;

public class PersoonDTO {
    private String voornaam;
    private String naam;
    private Date geboortedatum;
    private String geslacht;
    private String adres;
    private String telefoon;
    private String gsm;
    private String email;
    private String wachtwoord;
    private Rol defaultRol;
    private Long id;
    private String api;

    public PersoonDTO(){}

    public PersoonDTO(Long id, String voornaam, String naam, Date geboortedatum, String geslacht, String adres, String telefoon, String gsm, String email) {
        this.id = id;
        this.voornaam = voornaam;
        this.naam = naam;
        this.geboortedatum = geboortedatum;
        this.geslacht = geslacht;
        this.adres = adres;
        this.telefoon = telefoon;
        this.gsm = gsm;
        this.email = email;
        this.defaultRol = Rol.GUEST;
    }
    public PersoonDTO(String voornaam, String naam, Date geboortedatum, String geslacht, String adres, String telefoon, String gsm, String email) {
        this.voornaam = voornaam;
        this.naam = naam;
        this.geboortedatum = geboortedatum;
        this.geslacht = geslacht;
        this.adres = adres;
        this.telefoon = telefoon;
        this.gsm = gsm;
        this.email = email;
        this.defaultRol = Rol.GUEST;
    }

    public PersoonDTO(String voornaam, String naam, Date geboortedatum, String geslacht, String adres, String telefoon, String gsm, String email,String wachtwoord) {
        this.voornaam = voornaam;
        this.naam = naam;
        this.geboortedatum = geboortedatum;
        this.geslacht = geslacht;
        this.adres = adres;
        this.telefoon = telefoon;
        this.gsm = gsm;
        this.email = email;
        this.wachtwoord = wachtwoord;
        this.defaultRol = Rol.GUEST;
    }

    public PersoonDTO(String voornaam, String naam, Date geboortedatum, String geslacht, String adres, String telefoon, String gsm, String email,String wachtwoord, Rol defaultRol) {
        this.voornaam = voornaam;
        this.naam = naam;
        this.geboortedatum = geboortedatum;
        this.geslacht = geslacht;
        this.adres = adres;
        this.telefoon = telefoon;
        this.gsm = gsm;
        this.email = email;
        this.wachtwoord = wachtwoord;
        this.defaultRol = defaultRol;
    }
    public PersoonDTO(Long id, String voornaam, String naam, Date geboortedatum, String geslacht, String adres, String telefoon, String gsm, String email, Rol defaultRol, String api) {
        this.voornaam = voornaam;
        this.naam = naam;
        this.geboortedatum = geboortedatum;
        this.geslacht = geslacht;
        this.adres = adres;
        this.telefoon = telefoon;
        this.gsm = gsm;
        this.email = email;
        this.defaultRol = defaultRol;
        this.api = api;
    }

    public PersoonDTO(String username, String password) {
    }


    public void setId(long id) {
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

    public Rol getDefaultRol() {
        return defaultRol;
    }

    public void setDefaultRol(Rol defaultRol) {
        this.defaultRol = defaultRol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }
}
