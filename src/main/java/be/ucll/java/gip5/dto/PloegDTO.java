package be.ucll.java.gip5.dto;

public class PloegDTO {
    private Long id;
    private String naam;
    private String omschrijving;

    public PloegDTO(){}
    public PloegDTO(String naam){
        this.naam = naam;
    }
    public PloegDTO(Long id, String naam){
        this.naam = naam;
        this.id = id;
    }
    public PloegDTO(String naam, String omschrijving){
        this.naam = naam;
        this.omschrijving = omschrijving;
    }
    public PloegDTO(Long id, String naam, String omschrijving){
        this.naam = naam;
        this.id = id;
        this.omschrijving = omschrijving;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }
}
