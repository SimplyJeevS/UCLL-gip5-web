package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.model.Deelname;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeelnameRepository  extends JpaRepository<Deelname, Long> {
    Optional<Deelname> findDeelnameById(Long id);
}
