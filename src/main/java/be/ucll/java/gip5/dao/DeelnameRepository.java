package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.model.Deelname;
import be.ucll.java.gip5.model.Toewijzing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeelnameRepository  extends JpaRepository<Deelname, Long> {
    Optional<Deelname> findDeelnameById(Long id);
    List<Deelname> findAll();
    Optional<List<Deelname>> findAllByWedstrijdId(Long id);
    Optional<List<Deelname>> findAllByPersoonId(Long id);
}
