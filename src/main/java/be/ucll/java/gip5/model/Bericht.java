package be.ucll.java.gip5.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name ="bericht", schema = "gip5")
public class Bericht {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name="wedstrijdId")
    private Long wedstrijdId;
    @Column(name="boodschap")
    private String boodschap;
    @Column(name="afzenderId")
    private Long afzenderId;
    @Column(name="tijdstip")
    private LocalDateTime tijdstip;
    private Bericht(BerichtBuilder builder){

    }

    public Bericht(){

    }

    public static final class BerichtBuilder{
        private Long id;
        private Long wedstrijdId;
        private String boodschap;
        private Long afzenderId;
        private LocalDateTime tijdstip;
        public BerichtBuilder(){}
        public static BerichtBuilder Bericht(){return new BerichtBuilder();}
        public BerichtBuilder(Bericht copy){
            this.id = copy.id;
            this.wedstrijdId = copy.wedstrijdId;
            this.boodschap = copy.boodschap;
            this.afzenderId = copy.afzenderId;
            this.tijdstip = copy.tijdstip;
        }
        public BerichtBuilder id(Long id){
            this.id = id;
            return this;
        }
        public BerichtBuilder wedstrijdId(Long wedstrijdId){
            this.wedstrijdId = wedstrijdId;
            return this;
        }
        public BerichtBuilder boodschap(String boodschap){
            this.boodschap = boodschap;
            return this;
        }
        public BerichtBuilder afzenderId(Long afzenderId){
            this.afzenderId = afzenderId;
            return this;
        }
        public BerichtBuilder tijdstip(LocalDateTime tijdstip){
            this.tijdstip = tijdstip;
            return this;
        }
        public Bericht build(){return new Bericht(this);}
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
