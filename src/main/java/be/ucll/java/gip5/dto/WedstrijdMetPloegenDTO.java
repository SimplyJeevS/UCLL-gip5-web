package be.ucll.java.gip5.dto;

import java.time.LocalDateTime;

public class WedstrijdMetPloegenDTO {

    private LocalDateTime tijdstip;
    private String locatie;
    private Long thuisploegId;
    private Long tegenstanderId;
    private String tegenstander;
    private String thuisploeg;

    public WedstrijdMetPloegenDTO(LocalDateTime tijdstip, String locatie, Long thuisploegId, Long tegenstanderId, String tegenstander, String thuisploeg) {
        this.tijdstip = tijdstip;
        this.locatie = locatie;
        this.thuisploegId = thuisploegId;
        this.tegenstanderId = tegenstanderId;
        this.tegenstander = tegenstander;
        this.thuisploeg = thuisploeg;
    }

    public LocalDateTime getTijdstip() {
        return tijdstip;
    }

    public void setTijdstip(LocalDateTime tijdstip) {
        this.tijdstip = tijdstip;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public Long getThuisploegId() {
        return thuisploegId;
    }

    public void setThuisploegId(Long thuisploegId) {
        this.thuisploegId = thuisploegId;
    }

    public Long getTegenstanderId() {
        return tegenstanderId;
    }

    public void setTegenstanderId(Long tegenstanderId) {
        this.tegenstanderId = tegenstanderId;
    }

    public String getTegenstander() {
        return tegenstander;
    }

    public void setTegenstander(String tegenstander) {
        this.tegenstander = tegenstander;
    }

    public String getThuisploeg() {
        return thuisploeg;
    }

    public void setThuisploeg(String thuisploeg) {
        this.thuisploeg = thuisploeg;
    }
}
