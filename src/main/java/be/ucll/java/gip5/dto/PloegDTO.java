package be.ucll.java.gip5.dto;

public class PloegDTO {
    private String naam;

    public PloegDTO(){}
    public PloegDTO(String naam){
        this.naam = naam;
    }
    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }
}
