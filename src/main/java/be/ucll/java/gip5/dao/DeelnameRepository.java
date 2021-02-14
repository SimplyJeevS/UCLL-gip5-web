package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.model.Toewijzing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeelnameRepository  extends JpaRepository<Toewijzing, Long> {
    Optional<Toewijzing> findDeelnameById(Long id);
    List<Toewijzing> findAll();
}
