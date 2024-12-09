package facturation.prixBanque.facturation.controller;

import facturation.prixBanque.facturation.exception.ResourceNotFoundException;
import facturation.prixBanque.facturation.model.Facture;
import facturation.prixBanque.facturation.service.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/factures")
public class FactureController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FactureService factureService;

    @PostMapping("/create/{clientId}/{accountId}")
    public ResponseEntity<String> creerFacture(@PathVariable Long clientId, @PathVariable Long accountId, @RequestBody Facture facture) {
        String accountServiceUrl = "http://gestionCompte:8083/accounts/" + clientId + "/validate/" + accountId;
        ResponseEntity<Object> response = restTemplate.getForEntity(accountServiceUrl, Object.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ResourceNotFoundException("Le compte avec ID " + accountId + " n'appartient pas au client avec ID " + clientId + " !");
        }

        facture.setClientId(clientId);
        facture.setAccountId(accountId);

        factureService.creerFacture(facture);

        return ResponseEntity.ok("Facture créée avec succès pour le client " + clientId + " et le compte " + accountId + " !");
    }

    // Payer une facture
    @PostMapping("/payer/{id}")
    public ResponseEntity<String> payerFacture(@PathVariable Long id) {
        Facture facture = factureService.obtenirFactureParId(id);

        if (facture == null) {
            throw new ResourceNotFoundException("Facture avec ID " + id + " introuvable !");
        }

        if (facture.getPaye()) {
            throw new IllegalArgumentException("Facture avec ID " + id + " déjà payée !");
        }

        facture.setPaye(true); // Marquer la facture comme payée
        factureService.mettreAJourFacture(facture);

        return ResponseEntity.ok("Facture payée avec succès !");
    }

    // Consulter toutes les factures
    @GetMapping
    public ResponseEntity<List<Facture>> consulterToutesLesFactures() {
        return ResponseEntity.ok(factureService.obtenirToutesLesFactures());
    }

    // Récupérer une facture par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Facture> obtenirFactureParId(@PathVariable Long id) {
        Facture facture = factureService.obtenirFactureParId(id);

        if (facture == null) {
            throw new ResourceNotFoundException("Facture avec ID " + id + " introuvable !");
        }

        return ResponseEntity.ok(facture);
    }

    // Supprimer une facture
    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerFacture(@PathVariable Long id) {
        Facture facture = factureService.obtenirFactureParId(id);

        if (facture == null) {
            throw new ResourceNotFoundException("Facture avec ID " + id + " introuvable !");
        }

        factureService.supprimerFacture(id);
        return ResponseEntity.ok("Facture supprimée avec succès !");
    }
}
