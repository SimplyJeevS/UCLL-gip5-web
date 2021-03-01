package be.ucll.java.gip5.dto;

import be.ucll.java.gip5.model.Status;

public class DeelnameDTO {
    private Long persoonId;
    private Long wedstrijdId;
    private String commentaar;
    private Status status;

    public DeelnameDTO(){}
    public DeelnameDTO(Long persoonId, Long wedstrijdId, String commentaar, Status status){
        setPersoonId(persoonId);
        setWedstrijdId(wedstrijdId);
        setPersoonId(persoonId);
        setCommentaar(commentaar);
        setStatus(status);
    }

    public Long getPersoonId() {
        return persoonId;
    }

    public void setPersoonId(Long persoonId) {
        this.persoonId = persoonId;
    }

    public Long getWedstrijdId() {
        return wedstrijdId;
    }

    public void setWedstrijdId(Long wedstrijdId) {
        this.wedstrijdId = wedstrijdId;
    }

    public String getCommentaar() {
        return commentaar;
    }

    public void setCommentaar(String commentaar) {
        this.commentaar = commentaar;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
