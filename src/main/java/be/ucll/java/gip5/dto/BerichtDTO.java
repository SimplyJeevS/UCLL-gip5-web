package be.ucll.java.gip5.dto;

public class BerichtDTO {
    private Long wedstrijdId;
    private String boodschap;

    public BerichtDTO(){}
    public BerichtDTO(Long wedstrijdId, String boodschap){
        this.wedstrijdId = wedstrijdId;
        this.boodschap = boodschap;
    }

    public Long getWedstrijdId() {
        return wedstrijdId;
    }

    public void setWedstrijdId(Long wedstrijdId) {
        this.wedstrijdId = wedstrijdId;
    }

    public String getBoodschap() {
        return boodschap;
    }

    public void setBoodschap(String boodschap) {
        this.boodschap = boodschap;
    }
}
