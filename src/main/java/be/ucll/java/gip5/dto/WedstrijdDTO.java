package be.ucll.java.gip5.dto;

import liquibase.pro.packaged.S;

import java.time.LocalDateTime;
import java.util.Date;

public class WedstrijdDTO {
    private LocalDateTime tijdstip;
    private String locatie;
    private Long thuisPloeg;
    private Long tegenstander;

    public WedstrijdDTO(){}
    public WedstrijdDTO(LocalDateTime tijdstip, String locatie, Long thuisPloeg){
        this.tijdstip = tijdstip;
        this.locatie = locatie;
        this.thuisPloeg = thuisPloeg;
    }

    public LocalDateTime tijdstip() {
        return tijdstip;
    }

    public void setDatum(LocalDateTime tijdstip) {
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
}
