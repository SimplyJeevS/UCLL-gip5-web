package be.ucll.java.gip5.dto;

import java.util.Date;

public class PersoonDTO {
    private String voornaam;
    private Date geboortedatum;
    private Boolean geslacht;
    private String adres;
    private String telefoon;
    private String gsm;
    private String email;
    private String wachtwoord;
    private Long ploegId;
    private Long rolId;

    public PersoonDTO(){}

    public PersoonDTO(String voornaam, Date geboortedatum, Boolean geslacht, String adres, String telefoon, String gsm, String email, String wachtwoord, Long ploegId, Long rolId) {
        this.voornaam = voornaam;
        this.geboortedatum = geboortedatum;
        this.geslacht = geslacht;
        this.adres = adres;
        this.telefoon = telefoon;
        this.gsm = gsm;
        this.email = email;
        this.wachtwoord = wachtwoord;
        this.ploegId = ploegId;
        this.rolId = rolId;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
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
}
