package net.epsi.mediscan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.epsi.mediscan.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
