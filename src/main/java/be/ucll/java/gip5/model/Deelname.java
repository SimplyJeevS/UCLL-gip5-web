package be.ucll.java.gip5.model;

import javax.persistence.*;

@Entity
@Table(name ="deelname", schema = "gip5")
public class Deelname {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "persoonId")
    private Long persoonId;

    @Column(name = "wedstrijdId")
    private Long wedstrijdId;

    @Column(name = "commentaar")
    private String commentaar;
    private Deelname(ToewijzingBuilder builder) {
    }

    public Deelname() {
    }

    public static final class ToewijzingBuilder {

        private Long id;
        private Long persoonId;
        private Long wedstrijdId;
        private String commentaar;

        public ToewijzingBuilder(){
        }

        public static ToewijzingBuilder Toewijzing() {
            return new ToewijzingBuilder();
        }

        public ToewijzingBuilder(Deelname copy) {
            this.id = copy.id;
            this.persoonId = copy.id;
            this.wedstrijdId = copy.wedstrijdId;
            this.commentaar = copy.commentaar;
        }

        public ToewijzingBuilder id(Long id){
            this.id = id;
            return this;
        }
        public ToewijzingBuilder persoonId(Long persoonId){
            this.persoonId = persoonId;
            return this;
        }
        public ToewijzingBuilder wedstrijdId(Long wedstrijdId){
            this.wedstrijdId = wedstrijdId;
            return this;
        }
        public ToewijzingBuilder commentaar(String commentaar){
            this.commentaar = commentaar;
            return this;
        }
        public Deelname build(){
            return new Deelname(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
