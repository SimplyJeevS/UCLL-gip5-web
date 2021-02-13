package be.ucll.java.gip5.dto;

import java.time.LocalDateTime;

public class BerichtDTO {
    private Long wedstrijdId;
    private String boodschap;
    private Long afzenderId;
    private LocalDateTime tijdstip;
    public BerichtDTO(){}
    public BerichtDTO(Long wedstrijdId, String boodschap,Long afzenderId, LocalDateTime tijdstip){
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

    public LocalDateTime getTijdstip() {
        return tijdstip;
    }

    public void setTijdstip(LocalDateTime tijdstip) {
        this.tijdstip = tijdstip;
    }
}
