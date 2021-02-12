package be.ucll.java.gip5.model;

import javax.persistence.*;
import java.util.Date;

import java.time.LocalDateTime;
@Entity
@Table(name ="wedstrijd", schema = "gip5")
public class Wedstrijd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name="tijdstip")
    private LocalDateTime tijdstip;
    @Column(name="locatie")
    private String locatie;
    @Column(name="thuisPloeg")
    private Long thuisPloeg;
    @Column(name="tegenstander")
    private Long tegenstander;

    private Wedstrijd(WedstrijdBuilder builder){}
    public Wedstrijd(){}

    private static final class WedstrijdBuilder{
        private Long id;
        private LocalDateTime tijdstip;
        private String locatie;
        private Long thuisPloeg;
        private Long tegenstander;
        public WedstrijdBuilder(){}
        public static WedstrijdBuilder Wedstrijd(){return new WedstrijdBuilder();}
        public WedstrijdBuilder(Wedstrijd copy){
            this.id = copy.id;
            this.tijdstip = copy.tijdstip;
            this.locatie = copy.locatie;
            this.thuisPloeg = copy.thuisPloeg;
            this.tegenstander = copy.tegenstander;
        }
        public WedstrijdBuilder id(Long id){
            this.id = id;
            return this;
        }
        public WedstrijdBuilder uur(LocalDateTime tijdstip){
            this.tijdstip = tijdstip;
            return this;
        }
        public WedstrijdBuilder locatie(String locatie){
            this.locatie = locatie;
            return this;
        }
        public WedstrijdBuilder thuisPloeg(Long thuisPloeg){
            this.thuisPloeg = thuisPloeg;
            return this;
        }
        public WedstrijdBuilder tegenstander(Long tegenstander){
            this.tegenstander = tegenstander;
            return this;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Date getDatum() {
            return datum;
        }

        public void setDatum(Date datum) {
            this.datum = datum;
        }

        public LocalTime getUur() {
            return uur;
        }

        public void setUur(LocalTime uur) {
            this.uur = uur;
        }

        public String getLocatie() {
            return locatie;
        }

        public void setLocatie(String locatie) {
            this.locatie = locatie;
        }

        public Long getThuisPloeg() {
            return thuisPloeg;
        }

        public void setThuisPloeg(Long thuisPloeg) {
            this.thuisPloeg = thuisPloeg;
        }

        public Long getTegenstander() {
            return tegenstander;
        }

        public void setTegenstander(Long tegenstander) {
            this.tegenstander = tegenstander;
        }
    }


}
