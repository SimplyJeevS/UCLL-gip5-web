package be.ucll.java.gip5.model;

import javax.persistence.*;

@Entity
public class DummyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dseq")
    @SequenceGenerator(name = "dseq", sequenceName = "dummy_sequence", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column (length = 128)
    private String dummy;
}
