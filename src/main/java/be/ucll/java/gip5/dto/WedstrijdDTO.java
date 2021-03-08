package be.ucll.java.gip5.dto;


import java.time.LocalDateTime;
import java.util.Date;

public class WedstrijdDTO {
    private Long id;
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
    public WedstrijdDTO(Long id, LocalDateTime tijdstip, String locatie, Long thuisPloeg, Long tegenstander){
        this.id = id;
        this.tijdstip = tijdstip;
        this.locatie = locatie;
        this.thuisPloeg = thuisPloeg;
        this.tegenstander = tegenstander;
    }
    public WedstrijdDTO(LocalDateTime tijdstip, String locatie, Long thuisPloeg, Long tegenstander){
        this.tijdstip = tijdstip;
        this.locatie = locatie;
        this.thuisPloeg = thuisPloeg;
        this.tegenstander = tegenstander;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getTijdstip() {
        return tijdstip;
    }

    public void setTijdstip(LocalDateTime tijdstip) {
        this.tijdstip = tijdstip;
    }
}
