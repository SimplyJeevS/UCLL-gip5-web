package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.model.Persoon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersoonRepository extends JpaRepository<Persoon, Long> {

}
