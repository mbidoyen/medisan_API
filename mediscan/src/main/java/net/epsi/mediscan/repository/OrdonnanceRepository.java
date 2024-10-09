package net.epsi.mediscan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.epsi.mediscan.entities.Ordonnance;
import net.epsi.mediscan.entities.User;

import java.util.List;


@Repository
public interface OrdonnanceRepository extends JpaRepository<Ordonnance, Long> {

    List<Ordonnance> findByUser(User user);

}
