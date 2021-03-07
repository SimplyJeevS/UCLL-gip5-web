package be.ucll.java.gip5.dto;

public class PloegDTO {
    private Long id;
    private String naam;

    public PloegDTO(){}
    public PloegDTO(String naam){
        this.naam = naam;
    }
    public PloegDTO(Long id, String naam){
        this.naam = naam;
        this.id = id;
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
