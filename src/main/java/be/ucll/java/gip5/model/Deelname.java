package be.ucll.java.gip5.model;

import javax.persistence.*;

@Entity
@Table(name ="deelname", schema = "gip5")
public class Deelname {
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

    private Deelname(DeelnameBuilder builder){

    }
    public Deelname(){}

    public static final class DeelnameBuilder{
        private Long id;
        private Long persoonId;
        private Long rolId;
        private Long ploegId;

        public DeelnameBuilder(){}
        public static DeelnameBuilder Deelname(){return new DeelnameBuilder();}
        public DeelnameBuilder(Deelname copy){
            this.id = copy.id;
            this.persoonId = copy.persoonId;
            this.rolId = copy.rolId;
            this.ploegId = copy.ploegId;
        }
        public DeelnameBuilder id(Long id){
            this.id = id;
            return this;
        }
        public DeelnameBuilder persoonId(Long persoonId){
            this.persoonId = persoonId;
            return this;
        }
        public DeelnameBuilder rolId(Long rolId){
            this.rolId = rolId;
            return this;
        }
        public DeelnameBuilder ploegId(Long ploegId){
            this.ploegId = ploegId;
            return this;
        }
        public Deelname build(){return new Deelname(this);}
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
