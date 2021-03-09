package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.model.Wedstrijd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WedstrijdRepository extends JpaRepository<Wedstrijd, Long> {
    Optional<Wedstrijd> findWedstrijdById(Long id);
    List<Wedstrijd> findAll();
    Optional<List<Wedstrijd>> findWedstrijdByLocatieContaining(String locatie);
    Optional<List<Wedstrijd>> findWedstrijdByThuisPloeg(Long id);
    Optional<List<Wedstrijd>> findWedstrijdByThuisPloegAndLocatieContainingIgnoreCase(Long id, String searchtTerm);
    Optional<List<Wedstrijd>> findWedstrijdByTegenstander(Long id);
    List<Wedstrijd> findAllByLocatieContainingIgnoreCase(String locatie);
}
