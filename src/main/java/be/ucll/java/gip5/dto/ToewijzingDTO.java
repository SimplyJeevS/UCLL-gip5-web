package be.ucll.java.gip5.dto;

public class ToewijzingDTO {
    private Long persoonId;
    private Long rolId;
    private Long ploegId;
    private Long wedstrijdId;

    public ToewijzingDTO(){}
    public ToewijzingDTO(Long persoonId, Long rolId, Long ploegId){
        this.persoonId = persoonId;
        this.rolId = rolId;
        this.ploegId = ploegId;
    }

    public Long getPersoonId() {
        return persoonId;
    }

    public void setPersoonId(Long persoonId) {
        this.persoonId = persoonId;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    public Long getPloegId() {
        return ploegId;
    }

    public void setPloegId(Long ploegId) {
        this.ploegId = ploegId;
    }
}
