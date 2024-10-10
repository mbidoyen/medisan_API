package net.epsi.mediscan.controller;

import net.epsi.mediscan.dto.OrdonnanceDTO;
import net.epsi.mediscan.entities.Medicament;
import net.epsi.mediscan.entities.Ordonnance;
import net.epsi.mediscan.entities.User;
import net.epsi.mediscan.service.IOrdonnanceService;
import net.epsi.mediscan.service.IUserService;
import net.epsi.mediscan.utils.MediscanUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ordonnances")
public class OrdonnanceController {

    @Autowired
    private IOrdonnanceService ordonnanceService;

    @Autowired
    private IUserService userService;

    @PostMapping
    public ResponseEntity<Ordonnance> add(@RequestBody Ordonnance ordonnance) {
        try {
            User user = this.userService.getById(ordonnance.getUser().getId());

            ordonnance.setUser(user);

            Ordonnance ordonnanceTemp = ordonnanceService.save(ordonnance);

            MediscanUtil.setOrdonnanceInMedicament(ordonnanceTemp, ordonnance.getMedicaments());

            Ordonnance createdOrdonnance = ordonnanceService.save(ordonnanceTemp);

            return new ResponseEntity<>(createdOrdonnance, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Gérer l'erreur de manière appropriée
        }
    }

    @PatchMapping("/upload")
    public ResponseEntity<Boolean> uploadFile(@RequestPart("file") MultipartFile file) {
        try {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrdonnanceDTO> getOrdonnanceById(@PathVariable Long id) {
        Ordonnance ordonnance = this.ordonnanceService.getById(id);

        if (ordonnance == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        OrdonnanceDTO ordonnanceDTO = new OrdonnanceDTO();
        ordonnanceDTO.setId(ordonnance.getId());
        ordonnanceDTO.setDatePrescription(ordonnance.getDatePrescription());

        ordonnanceDTO.setMedicaments(ordonnance.getMedicaments() != null ? ordonnance.getMedicaments() : new ArrayList<>());

        return new ResponseEntity<>(ordonnanceDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrdonnanceDTO>> getAll() {
        List<Ordonnance> ordonnances = this.ordonnanceService.getALl();

        if (ordonnances.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<OrdonnanceDTO> ordonnanceDTOs = new ArrayList<>();

        for (Ordonnance ordonnance : ordonnances) {
            OrdonnanceDTO ordonnanceDTO = new OrdonnanceDTO();
            ordonnanceDTO.setId(ordonnance.getId());
            ordonnanceDTO.setDatePrescription(ordonnance.getDatePrescription());

            ordonnanceDTO.setMedicaments(
                    ordonnance.getMedicaments() != null ? ordonnance.getMedicaments() : new ArrayList<>());

            ordonnanceDTOs.add(ordonnanceDTO);
        }

        return new ResponseEntity<>(ordonnanceDTOs, HttpStatus.OK);
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<OrdonnanceDTO>> getAllOrdonnanceByUserId(@PathVariable Long user_id) {
        User user = this.userService.getById(user_id);
        List<Ordonnance> ordonnances = this.ordonnanceService.getAllByUser(user);
        
        if (ordonnances.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<OrdonnanceDTO> ordonnanceDTOs = new ArrayList<>();

        for (Ordonnance ordonnance : ordonnances) {
            OrdonnanceDTO ordonnanceDTO = new OrdonnanceDTO();
            ordonnanceDTO.setId(ordonnance.getId());
            ordonnanceDTO.setDatePrescription(ordonnance.getDatePrescription());

            ordonnanceDTO.setMedicaments(
                    ordonnance.getMedicaments() != null ? ordonnance.getMedicaments() : new ArrayList<>());

            ordonnanceDTOs.add(ordonnanceDTO);
        }

        return new ResponseEntity<>(ordonnanceDTOs, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Boolean> updateOrdonnance(@PathVariable Long id, @RequestBody Ordonnance ordonnance) {
        Ordonnance ordonnanceToUpdate = this.ordonnanceService.getById(id);
        boolean invalidDate = false;
        if (ordonnanceToUpdate == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }

        for (Medicament medicament : ordonnance.getMedicaments()) {
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
