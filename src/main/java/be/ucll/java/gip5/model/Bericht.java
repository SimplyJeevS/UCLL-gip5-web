package be.ucll.java.gip5.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name ="bericht", schema = "gip5")
public class Bericht {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "dseq", sequenceName = "bericht_sequence", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
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
            if(wedstrijdId==null || wedstrijdId <= 0){
                throw new IllegalArgumentException("WedstrijdId werd niet juist gegeven");
            }
            this.wedstrijdId = wedstrijdId;
            return this;
        }
        public BerichtBuilder boodschap(String boodschap){
            if(boodschap.isEmpty() || boodschap.trim().length() <= 0){
                throw new IllegalArgumentException("boodschap werd niet juist gegeven");
            }
            this.boodschap = boodschap;
            return this;
        }
        public BerichtBuilder afzenderId(Long afzenderId){
            if(afzenderId==null || afzenderId <= 0){
                throw new IllegalArgumentException("afzenderId werd niet juist gegeven");
            }
            this.afzenderId = afzenderId;
            return this;
        }
        public BerichtBuilder tijdstip(LocalDateTime tijdstip){
            if(tijdstip.equals(null) ){
                throw new IllegalArgumentException("tijdstip mag niet leeg zijn");
            }else if(tijdstip.isAfter(LocalDateTime.now().plusMinutes(1))){
                throw new IllegalArgumentException("tijdstip mag niet in de toekomst liggen");
            }
            this.tijdstip = tijdstip;
            return this;
        }
        public Bericht build(){return new Bericht(this);}
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
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
