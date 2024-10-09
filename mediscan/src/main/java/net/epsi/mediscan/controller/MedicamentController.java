package net.epsi.mediscan.controller;

import net.epsi.mediscan.entities.Medicament;
import net.epsi.mediscan.service.IMedicamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicaments")
public class MedicamentController {

    @Autowired
    private IMedicamentService medicamentService;

    // Ajouter un médicament
    @PostMapping
    public ResponseEntity<Medicament> add(@RequestBody Medicament medicament) {
        Medicament createdMedicament = medicamentService.save(medicament); // Appeler le service pour sauvegarder
        return new ResponseEntity<>(createdMedicament, HttpStatus.CREATED); // Retourner le médicament créé avec statut 201
    }

    // Récupérer un médicament par ID
    @GetMapping("/{id}")
    public ResponseEntity<Medicament> getMedicamentById(@PathVariable Long id) {
        Medicament medicament = this.medicamentService.getById(id);
        if (medicament != null) {
            return new ResponseEntity<>(medicament, HttpStatus.OK); // Renvoie le médicament avec un statut 200 OK
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Renvoie un statut 404 si le médicament n'est pas trouvé
        }
    }

    // Mettre à jour un médicament
    @PatchMapping("/{id}") // Utilisez @PathVariable pour obtenir l'ID
    public ResponseEntity<Boolean> updateMedicament(@PathVariable Long id, @RequestBody Medicament medicament) {
        Medicament medicamentToUpdate = this.medicamentService.getById(id);
        if (medicamentToUpdate == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }

        // Mettre à jour les propriétés
        medicamentToUpdate.setNom(medicament.getNom());
        medicamentToUpdate.setDose(medicament.getDose());
        medicamentToUpdate.setNbrPriseMatin(medicament.getNbrPriseMatin());
        medicamentToUpdate.setNbrPriseMidi(medicament.getNbrPriseMidi());
        medicamentToUpdate.setNbrPriseSoir(medicament.getNbrPriseSoir());
        medicamentToUpdate.setDateDebut(medicament.getDateDebut());
        medicamentToUpdate.setDateFin(medicament.getDateFin());

        this.medicamentService.update(medicamentToUpdate);
        return new ResponseEntity<>(true, HttpStatus.OK); // Indiquer que la mise à jour a réussi
    }

    // Récupérer tous les médicaments
    @GetMapping
    public ResponseEntity<List<Medicament>> getAll() {
        List<Medicament> medicaments = this.medicamentService.getALl(); // Corriger l'appel pour le service

        if (!medicaments.isEmpty()) {
            return new ResponseEntity<>(medicaments, HttpStatus.OK); // Renvoie la liste avec un statut 200 OK
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Renvoie un statut 204 si la liste est vide
    }

    // Supprimer un médicament
    @DeleteMapping("/{id}") // Utilisez @PathVariable pour obtenir l'ID
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Medicament getMedicament = this.medicamentService.getById(id);
        if (getMedicament == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }

        this.medicamentService.delete(getMedicament);
        return new ResponseEntity<>(true, HttpStatus.OK); // Renvoie un statut 200 OK après suppression
    }
}
