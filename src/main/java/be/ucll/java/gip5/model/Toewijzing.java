package be.ucll.java.gip5.model;

import javax.persistence.*;

@Entity
@Table(name ="toewijzing", schema = "gip5")
public class Toewijzing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "dseq", sequenceName = "toewijzing_sequence", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name="persoonId")
    private Long persoonId;

    @Column(name="rol")
    private Rol rol;

    @Column(name="ploegId")
    private Long ploegId;

    private Toewijzing(ToewijzingBuilder builder){
        setId(builder.id);
        setPloegId(builder.ploegId);
        setRol(builder.rol);
        setPersoonId(builder.persoonId);
    }
    public Toewijzing(){}

    public static final class ToewijzingBuilder{
        private Long id;
        private Long persoonId;
        private Rol rol;
        private Long ploegId;

        public ToewijzingBuilder(){}
        public static ToewijzingBuilder Deelname(){return new ToewijzingBuilder();}
        public ToewijzingBuilder(Toewijzing copy){
            this.id = copy.id;
            this.persoonId = copy.persoonId;
            this.rol = copy.rol;
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
        public ToewijzingBuilder rol(Rol rol){
            this.rol = rol;
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

    public Rol getRolId() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Long getPloegId() {
        return ploegId;
    }

    public void setPloegId(Long ploegId) {
        this.ploegId = ploegId;
    }
}
