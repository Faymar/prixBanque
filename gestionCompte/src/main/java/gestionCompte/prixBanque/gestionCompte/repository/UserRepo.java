package gestionCompte.prixBanque.gestionCompte.repository;

import gestionCompte.prixBanque.gestionCompte.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {

    Users findByUsername(String username);
}
