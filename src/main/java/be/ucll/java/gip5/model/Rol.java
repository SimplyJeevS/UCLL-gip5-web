package be.ucll.java.gip5.model;

import javax.persistence.*;

@Entity
@Table(name ="rol", schema = "gip5")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dseq")
    private Long id;

    @Column(name = "naam")
    private String naam;

    private Rol(RolBuilder builder){
    }

    public Rol(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public static final class RolBuilder {
        private Long id;
        private String naam;

        public RolBuilder() {
        }

        public static RolBuilder Rol() {
            return new RolBuilder();
        }

        public RolBuilder(Rol copy){
            this.id = copy.id;
            this.naam = copy.naam;
        }

        public RolBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RolBuilder naam(String naam) {
            this.naam = naam;
            return this;
        }

        public Rol build() {
            return new Rol(this);
        }
    }

}
