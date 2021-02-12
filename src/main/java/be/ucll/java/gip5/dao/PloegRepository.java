package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.model.Ploeg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PloegRepository extends JpaRepository<Ploeg, Long> {
    Optional<Ploeg> findPloegById(Long id);
}
