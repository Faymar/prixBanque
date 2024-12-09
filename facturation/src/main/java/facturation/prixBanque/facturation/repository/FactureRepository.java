package facturation.prixBanque.facturation.repository;

import facturation.prixBanque.facturation.model.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureRepository extends JpaRepository<Facture, Long> {
}

