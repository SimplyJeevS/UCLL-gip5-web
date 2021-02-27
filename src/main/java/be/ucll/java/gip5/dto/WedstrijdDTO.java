package be.ucll.java.gip5.dto;

import liquibase.pro.packaged.S;

import java.time.LocalDateTime;
import java.util.Date;

public class WedstrijdDTO {
    private String tijdstip;
    private String locatie;
    private Long thuisPloeg;
    private Long tegenstander;

    public WedstrijdDTO(){}
    public WedstrijdDTO(String tijdstip, String locatie, Long thuisPloeg){
        this.tijdstip = tijdstip;
        this.locatie = locatie;
        this.thuisPloeg = thuisPloeg;
    }

    public String tijdstip() {
        return tijdstip;
    }

    public void setDatum(String tijdstip) {
        this.tijdstip = tijdstip;
    }


    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public Long getThuisPloeg() {
        return thuisPloeg;
    }

    public void setThuisPloeg(Long thuisPloeg) {
        this.thuisPloeg = thuisPloeg;
    }

    public Long getTegenstander() {
        return tegenstander;
    }

    public void setTegenstander(Long tegenstander) {
        this.tegenstander = tegenstander;
    }

    public String getTijdstip() {
        return tijdstip;
    }

    public void setTijdstip(String tijdstip) {
        this.tijdstip = tijdstip;
    }
}
