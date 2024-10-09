package net.epsi.mediscan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.epsi.mediscan.entities.Ordonnance;

@Repository
public interface OrdonnanceRepository extends JpaRepository<Ordonnance, Long> {

}
