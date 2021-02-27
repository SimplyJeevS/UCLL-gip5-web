package be.ucll.java.gip5.model;

import javax.persistence.*;

@Entity
@Table(name ="ploeg", schema = "gip5")
public class Ploeg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name="naam")
    private String naam;

    private Ploeg(PloegBuilder builder){

    }
    public Ploeg(){}

    public static final class PloegBuilder {
        private Long id;
        private String naam;

        public PloegBuilder() {
        }

        public static PloegBuilder Ploeg() {
            return new PloegBuilder();
        }

        public PloegBuilder(Ploeg copy) {
            this.id = copy.id;
            this.naam = copy.naam;
        }

        public PloegBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PloegBuilder naam(String naam) {
            this.naam = naam;
            return this;
        }

        public Ploeg build() {
            return new Ploeg(this);
        }
    }

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
}
