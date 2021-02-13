package be.ucll.java.gip5.dto;

public class ToewijzingDTO {
    private Long persoonId;
    private Long wedstrijdId;
    private String commentaar;
    public ToewijzingDTO(){}
    public ToewijzingDTO(Long persoonId, Long wedstrijdId, String commentaar){
        this.persoonId = persoonId;
        this.wedstrijdId = wedstrijdId;
        this.commentaar = commentaar;
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
}
