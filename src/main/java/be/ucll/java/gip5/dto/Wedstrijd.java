package be.ucll.java.gip5.dto;

import java.time.LocalTime;
import java.util.Date;

public class Wedstrijd {
    private Long id;
    private Date datum;
    private LocalTime uur;
    private String locatie;
    private Long thuisPloeg;
    private Long tegenstander;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public LocalTime getUur() {
        return uur;
    }

    public void setUur(LocalTime uur) {
        this.uur = uur;
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
