package be.ucll.java.gip5.dto;

public class RolDTO {
    private String naam;
    public RolDTO(){}
    public RolDTO(String naam){
        this.naam = naam;
    }
    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }
}
