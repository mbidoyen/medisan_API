package net.epsi.mediscan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.epsi.mediscan.entities.Medicament;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long>{

}
