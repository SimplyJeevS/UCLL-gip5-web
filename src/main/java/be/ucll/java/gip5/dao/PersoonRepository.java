package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.model.Persoon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersoonRepository extends JpaRepository<Persoon, Long> {
    Optional<Persoon> findPersoonById(Long id);
    List<Persoon> findAll();
    Optional<List<Persoon>> findAllByVoornaamContaining(String voornaam);
    Optional<List<Persoon>> findAllByNaamContaining(String voornaam);
    Optional<List<Persoon>> findAllByGeslacht(Boolean geslacht);
    Optional<List<Persoon>> findAllByAdresContaining(String adres);
    Optional<Persoon> findPersoonByEmailAndWachtwoord(String email, String wachtwoord);
    Optional<Persoon> findPersoonByEmailIgnoreCase(String email);
}
