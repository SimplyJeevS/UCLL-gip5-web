package be.ucll.java.gip5.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class Wedstrijd {
    private Long id;
    private LocalDateTime tijdstip;
    private String locatie;
    private Long thuisPloeg;
    private Long tegenstander;

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
}
