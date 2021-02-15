package be.ucll.java.gip5.dto;

import java.time.LocalDateTime;

public class BerichtDTO {
    private Long wedstrijdId;
    private String boodschap;
    private Long afzenderId;
    private String tijdstip;
    public BerichtDTO(){}
    public BerichtDTO(Long wedstrijdId, String boodschap,Long afzenderId, String tijdstip){
        this.wedstrijdId = wedstrijdId;
        this.boodschap = boodschap;
        this.afzenderId = afzenderId;
        this.tijdstip = tijdstip;
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

    public Long getAfzenderId() {
        return afzenderId;
    }

    public void setAfzenderId(Long afzenderId) {
        this.afzenderId = afzenderId;
    }

    public String getTijdstip() {
        return tijdstip;
    }

    public void setTijdstip(String tijdstip) {
        this.tijdstip = tijdstip;
    }
}
