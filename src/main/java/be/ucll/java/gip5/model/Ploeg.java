package be.ucll.java.gip5.model;

import javax.persistence.*;

@Entity
@Table(name ="ploeg", schema = "gip5")
public class Ploeg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "dseq", sequenceName = "ploeg_sequence", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name="naam", unique = true)
    private String naam;
    @Column(name="omschrijving")
    private String omschrijving;

    private Ploeg(PloegBuilder builder){
        setId(builder.id);
        setNaam(builder.naam);
        setOmschrijving(builder.omschrijving);
    }
    public Ploeg(){}

    public static final class PloegBuilder {
        private Long id;
        private String naam;
        private String omschrijving;

        public PloegBuilder() {
        }

        public static PloegBuilder Ploeg() {
            return new PloegBuilder();
        }

        public PloegBuilder(Ploeg copy) {
            this.id = copy.id;
            this.naam = copy.naam;
            this.omschrijving = copy.omschrijving;
        }

        public PloegBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PloegBuilder naam(String naam) {
            this.naam = naam;
            return this;
        }
        public PloegBuilder omschrijving(String omschrijving) {
            this.omschrijving = omschrijving;
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

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }
}
