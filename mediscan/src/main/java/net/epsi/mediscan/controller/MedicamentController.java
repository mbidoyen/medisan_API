package net.epsi.mediscan.controller;

import net.epsi.mediscan.entities.Medicament;
import net.epsi.mediscan.entities.Ordonnance;
import net.epsi.mediscan.service.IMedicamentService;
import net.epsi.mediscan.service.IOrdonnanceService;

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
    @Autowired
    private IOrdonnanceService ordonnanceService;

    // Ajouter un médicament
    @PostMapping
    public ResponseEntity<Medicament> saveMedicament(@RequestBody Medicament medicament) {

        if (medicament.getDateFin().isBefore(medicament.getDateDebut())) {
            return ResponseEntity.badRequest().build();
        }

        Ordonnance ordonnance = ordonnanceService.getById(medicament.getOrdonnance().getId());

        if (ordonnance == null) {
            return ResponseEntity.notFound().build();
        }

        medicament.setOrdonnance(ordonnance);

        Medicament createdMedicament = medicamentService.save(medicament);

        return new ResponseEntity<>(createdMedicament, HttpStatus.CREATED);
    }

    // Récupérer un médicament par ID
    @GetMapping("/{id}")
    public ResponseEntity<Medicament> getMedicamentById(@PathVariable Long id) {
        Medicament medicament = this.medicamentService.getById(id);
        if (medicament != null) {
            return new ResponseEntity<>(medicament, HttpStatus.OK);
        } 
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);  
    }

    // Mettre à jour un médicament
    @PatchMapping("/{id}")
    public ResponseEntity<Boolean> updateMedicament(@PathVariable Long id, @RequestBody Medicament medicament) {
        Medicament medicamentToUpdate = this.medicamentService.getById(id);
        if (medicamentToUpdate == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }

        medicamentToUpdate.setNom(medicament.getNom());
        medicamentToUpdate.setDose(medicament.getDose());
        medicamentToUpdate.setNbrPriseMatin(medicament.getNbrPriseMatin());
        medicamentToUpdate.setNbrPriseMidi(medicament.getNbrPriseMidi());
        medicamentToUpdate.setNbrPriseSoir(medicament.getNbrPriseSoir());
        medicamentToUpdate.setDateDebut(medicament.getDateDebut());
        medicamentToUpdate.setDateFin(medicament.getDateFin());

        this.medicamentService.update(medicamentToUpdate);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    // Récupérer tous les médicaments
    @GetMapping
    public ResponseEntity<List<Medicament>> getAll() {
        List<Medicament> medicaments = this.medicamentService.getALl();

        if (!medicaments.isEmpty()) {
            return new ResponseEntity<>(medicaments, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Supprimer un médicament
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Medicament getMedicament = this.medicamentService.getById(id);
        if (getMedicament == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }

        this.medicamentService.delete(getMedicament);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
