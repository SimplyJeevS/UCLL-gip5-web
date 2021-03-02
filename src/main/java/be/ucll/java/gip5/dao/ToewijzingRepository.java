package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.model.Deelname;
import be.ucll.java.gip5.model.Rol;
import be.ucll.java.gip5.model.Toewijzing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToewijzingRepository  extends JpaRepository<Toewijzing, Long> {
    Optional<Toewijzing> findToewijzingById(Long id);
    List<Toewijzing> findAll();
    Optional<List<Toewijzing>> findAllByPersoonId(Long persoonId);
    Optional<List<Toewijzing>> findAllByPloegId(Long ploegId);
    Optional<List<Toewijzing>> findAllByRol(Rol rol);
}
