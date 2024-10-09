package net.epsi.mediscan.controller;

import net.epsi.mediscan.entities.Medicament;
import net.epsi.mediscan.entities.Ordonnance;
import net.epsi.mediscan.entities.User;
import net.epsi.mediscan.service.IOrdonnanceService; // Assurez-vous que ce service existe
import net.epsi.mediscan.service.IUserService;
import net.epsi.mediscan.utils.MediscanUtil;

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
    private IOrdonnanceService ordonnanceService;

    @Autowired
    private IUserService userService;

    @PostMapping
    public ResponseEntity<Ordonnance> add(@RequestBody Ordonnance ordonnance) {
        User user = this.userService.getById(ordonnance.getUser().getId());
        ordonnance.setUser(user);
        Ordonnance ordonnanceTemp = ordonnanceService.save(ordonnance);
        MediscanUtil.setOrdonnanceInMedicament(ordonnanceTemp, ordonnance.getMedicaments());
        Ordonnance createdOrdonnance = ordonnanceService.save(ordonnanceTemp);
        return new ResponseEntity<>(createdOrdonnance, HttpStatus.CREATED); // Retourner l'ordonnance créée avec un statut 201
    }


    @GetMapping("/{id}")
    public ResponseEntity<Ordonnance> getOrdonnanceById(@PathVariable Long id) {
        Ordonnance ordonnance = this.ordonnanceService.getById(id);
        if (ordonnance != null) {
            if (ordonnance.getMedicaments() == null) {
                ordonnance.setMedicaments(new ArrayList<>());
            }
            return new ResponseEntity<>(ordonnance, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Ordonnance>> getAll() {
        List<Ordonnance> ordonnances = this.ordonnanceService.getALl();
        if (!ordonnances.isEmpty()) {
            return new ResponseEntity<>(ordonnances, HttpStatus.OK);
        }
        for (Ordonnance ordonnance : ordonnances) {
            if (ordonnance.getMedicaments() == null) {
                ordonnance.setMedicaments(new ArrayList<>());
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Boolean> updateOrdonnance(@PathVariable Long id, @RequestBody Ordonnance ordonnance) {
        Ordonnance ordonnanceToUpdate = this.ordonnanceService.getById(id);
        boolean invalidDate = false;
        if (ordonnanceToUpdate == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
        
        for(Medicament medicament : ordonnance.getMedicaments()){
            if (medicament.getDateFin().isBefore(medicament.getDateDebut())) {
                invalidDate = true;
                break;
            }
        }    

        if (invalidDate) {
            return ResponseEntity.badRequest().build();
        }
        
        ordonnanceToUpdate.setDatePrescription(ordonnance.getDatePrescription());
        ordonnanceToUpdate.setUser(ordonnance.getUser());
        ordonnanceToUpdate.setMedicaments(ordonnance.getMedicaments());

        this.ordonnanceService.update(ordonnanceToUpdate);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Ordonnance ordonnance = this.ordonnanceService.getById(id);
        if (ordonnance == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }

        this.ordonnanceService.delete(ordonnance);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
