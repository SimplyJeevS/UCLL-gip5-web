package be.ucll.java.gip5.model;

import javax.persistence.*;

@Entity
@Table(name ="toewijzing", schema = "gip5")
public class Toewijzing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="persoonId")
    private Long persoonId;

    @Column(name="rolId")
    private Long rolId;

    @Column(name="ploegId")
    private Long ploegId;

    private Toewijzing(ToewijzingBuilder builder){

    }
    public Toewijzing(){}

    public static final class ToewijzingBuilder{
        private Long id;
        private Long persoonId;
        private Long rolId;
        private Long ploegId;

        public ToewijzingBuilder(){}
        public static ToewijzingBuilder Deelname(){return new ToewijzingBuilder();}
        public ToewijzingBuilder(Toewijzing copy){
            this.id = copy.id;
            this.persoonId = copy.persoonId;
            this.rolId = copy.rolId;
            this.ploegId = copy.ploegId;
        }
        public ToewijzingBuilder id(Long id){
            this.id = id;
            return this;
        }
        public ToewijzingBuilder persoonId(Long persoonId){
            this.persoonId = persoonId;
            return this;
        }
        public ToewijzingBuilder rolId(Long rolId){
            this.rolId = rolId;
            return this;
        }
        public ToewijzingBuilder ploegId(Long ploegId){
            this.ploegId = ploegId;
            return this;
        }
        public Toewijzing build(){return new Toewijzing(this);}
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
