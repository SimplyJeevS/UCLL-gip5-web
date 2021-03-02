package be.ucll.java.gip5.dto;

import be.ucll.java.gip5.model.Rol;

public class ToewijzingDTO {
    private Long persoonId;
    private Rol rol;
    private Long ploegId;
    private Long wedstrijdId;

    public ToewijzingDTO(){}
    public ToewijzingDTO(Long persoonId, Rol rol, Long ploegId){
        this.persoonId = persoonId;
        this.rol = rol;
        this.ploegId = ploegId;
    }

    public Long getPersoonId() {
        return persoonId;
    }

    public void setPersoonId(Long persoonId) {
        this.persoonId = persoonId;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Long getPloegId() {
        return ploegId;
    }

    public void setPloegId(Long ploegId) {
        this.ploegId = ploegId;
    }
}
