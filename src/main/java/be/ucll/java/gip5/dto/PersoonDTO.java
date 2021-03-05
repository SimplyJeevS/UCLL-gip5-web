package be.ucll.java.gip5.dto;

import be.ucll.java.gip5.model.Rol;

public class PersoonDTO {
    private String voornaam;
    private String naam;
    private String geboortedatum;
    private String geslacht;
    private String adres;
    private String telefoon;
    private String gsm;
    private String email;
    private String wachtwoord;
    private Rol defaultRol;

    public PersoonDTO(){}
    public PersoonDTO(String voornaam, String naam, String geboortedatum, String geslacht, String adres, String telefoon, String gsm, String email) {
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

    public PersoonDTO(String voornaam, String naam, String geboortedatum, String geslacht, String adres, String telefoon, String gsm, String email,String wachtwoord) {
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

    public PersoonDTO(String voornaam, String naam, String geboortedatum, String geslacht, String adres, String telefoon, String gsm, String email,String wachtwoord, Rol defaultRol) {
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

    public PersoonDTO(String username, String password) {
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

    public String getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(String geboortedatum) {
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
}
