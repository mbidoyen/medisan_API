package net.epsi.mediscan.controller;

import net.epsi.mediscan.entities.Ordonnance;
import net.epsi.mediscan.entities.User;
import net.epsi.mediscan.service.IOrdonnanceService; // Assurez-vous que ce service existe
import net.epsi.mediscan.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ordonnances")
public class OrdonnanceController {

    @Autowired
    private IOrdonnanceService ordonnanceService; // Service pour gérer les ordonnances

    @Autowired
    private IUserService userService;

    // Créer une nouvelle ordonnance
    @PostMapping
    public ResponseEntity<Ordonnance> add(@RequestBody Ordonnance ordonnance) {
        User user = this.userService.getById(ordonnance.getUser().getId());
        ordonnance.setUser(user);
        Ordonnance createdOrdonnance = ordonnanceService.save(ordonnance); // Enregistrer l'ordonnance
        return new ResponseEntity<>(createdOrdonnance, HttpStatus.CREATED); // Retourner l'ordonnance créée avec un statut 201
    }

    // Récupérer une ordonnance par ID
    @GetMapping("/{id}")
    public ResponseEntity<Ordonnance> getOrdonnanceById(@PathVariable Long id) {
        Ordonnance ordonnance = this.ordonnanceService.getById(id);
        if (ordonnance != null) {
            if (ordonnance.getMedicaments() == null) {
                ordonnance.setMedicaments(new ArrayList<>()); // Initialiser si null
            }
            return new ResponseEntity<>(ordonnance, HttpStatus.OK); // Renvoie l'ordonnance avec un statut 200 OK
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Renvoie un statut 404 si l'ordonnance n'est pas trouvée
        }
    }

    // Récupérer toutes les ordonnances
    @GetMapping
    public ResponseEntity<List<Ordonnance>> getAll() {
        List<Ordonnance> ordonnances = this.ordonnanceService.getALl();
        if (!ordonnances.isEmpty()) {
            return new ResponseEntity<>(ordonnances, HttpStatus.OK); // Renvoie la liste avec un statut 200 OK
        }
        for (Ordonnance ordonnance : ordonnances) {
            if (ordonnance.getMedicaments() == null) {
                ordonnance.setMedicaments(new ArrayList<>()); // Initialiser si null
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Renvoie un statut 204 si la liste est vide
    }

    // Mettre à jour une ordonnance
    @PatchMapping("/{id}") // Utilisez @PathVariable pour obtenir l'ID
    public ResponseEntity<Boolean> updateOrdonnance(@PathVariable Long id, @RequestBody Ordonnance ordonnance) {
        Ordonnance ordonnanceToUpdate = this.ordonnanceService.getById(id);
        if (ordonnanceToUpdate == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }

        // Mettre à jour les propriétés de l'ordonnance
        ordonnanceToUpdate.setDatePrescription(ordonnance.getDatePrescription());
        ordonnanceToUpdate.setUser(ordonnance.getUser());
        ordonnanceToUpdate.setMedicaments(ordonnance.getMedicaments());

        this.ordonnanceService.update(ordonnanceToUpdate);
        return new ResponseEntity<>(true, HttpStatus.OK); // Indiquer que la mise à jour a réussi
    }

    // Supprimer une ordonnance
    @DeleteMapping("/{id}") // Utilisez @PathVariable pour obtenir l'ID
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Ordonnance ordonnance = this.ordonnanceService.getById(id);
        if (ordonnance == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }

        this.ordonnanceService.delete(ordonnance);
        return new ResponseEntity<>(true, HttpStatus.OK); // Renvoie un statut 200 OK après suppression
    }
}
