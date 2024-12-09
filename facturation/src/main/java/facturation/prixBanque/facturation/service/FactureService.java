package facturation.prixBanque.facturation.service;


import facturation.prixBanque.facturation.model.Facture;
import facturation.prixBanque.facturation.repository.FactureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactureService {

    private final FactureRepository factureRepository;

    public FactureService(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    // Créer une facture
    public Facture creerFacture(Facture facture) {
        return factureRepository.save(facture);
    }

    // Obtenir une facture par ID
    public Facture obtenirFactureParId(Long id) {
        return factureRepository.findById(id).orElse(null);
    }

    // Obtenir toutes les factures
    public List<Facture> obtenirToutesLesFactures() {
        return factureRepository.findAll();
    }

    // Mettre à jour une facture
    public Facture mettreAJourFacture(Facture facture) {
        return factureRepository.save(facture);
    }

    // Supprimer une facture par ID
    public void supprimerFacture(Long id) {
        factureRepository.deleteById(id);
    }
}


