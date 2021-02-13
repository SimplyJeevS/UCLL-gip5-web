package be.ucll.java.gip5.model;

import javax.persistence.*;

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

    private Bericht(BerichtBuilder builder){

    }

    public Bericht(){

    }

    public static final class BerichtBuilder{
        private Long id;
        private Long wedstrijdId;
        private String boodschap;
        public BerichtBuilder(){}
        public static BerichtBuilder Bericht(){return new BerichtBuilder();}
        public BerichtBuilder(Bericht copy){
            this.id = copy.id;
            this.wedstrijdId = copy.wedstrijdId;
            this.boodschap = copy.boodschap;
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
}
