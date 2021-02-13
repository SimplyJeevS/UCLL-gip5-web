package be.ucll.java.gip5.dao;

import be.ucll.java.gip5.model.Toewijzing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToewijzingRepository  extends JpaRepository<Toewijzing, Long> {
    Optional<Toewijzing> findToewijzingById(Long id);
}
