package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.model.Bericht;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BerichtRepository extends JpaRepository<Bericht, Long> {
    Optional<Bericht> findBerichtById(Long id);
    List<Bericht> findAll();
    Optional<List<Bericht>> findAllByWedstrijdId(Long wedstrijdId);
    Optional<List<Bericht>> findAllByAfzenderId(Long afzenderId);
}
