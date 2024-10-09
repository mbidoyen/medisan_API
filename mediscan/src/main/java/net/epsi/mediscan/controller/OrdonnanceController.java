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

import java.io.IOException;
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
            // Récupérer l'utilisateur à partir de l'ID
            User user = this.userService.getById(ordonnance.getUser().getId());

            // Associer l'utilisateur à l'ordonnance
            ordonnance.setUser(user);

            // Sauvegarder d'abord l'ordonnance pour obtenir son ID
            Ordonnance ordonnanceTemp = ordonnanceService.save(ordonnance);

            // Configurer les médicaments associés à l'ordonnance
            MediscanUtil.setOrdonnanceInMedicament(ordonnanceTemp, ordonnance.getMedicaments());

            // Sauvegarder à nouveau l'ordonnance avec le chemin d'image
            Ordonnance createdOrdonnance = ordonnanceService.save(ordonnanceTemp);

            // Retourner la réponse
            return new ResponseEntity<>(createdOrdonnance, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Gérer l'erreur de manière appropriée
        }
    }

    @PatchMapping("/image/{ordonnance_id}")
    public ResponseEntity<Boolean> uploadFile(@PathVariable("ordonnance_id") Long id,
                                               @RequestPart("file") MultipartFile file) {
        try {
            // Récupérer l'ordonnance par ID
            Ordonnance ordonnance = this.ordonnanceService.getById(id);
            if (ordonnance == null) {
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND); // Ordonnance non trouvée
            }

            // Traitement de l'image
            String imagePath = ordonnanceService.processImage(file, ordonnance); // Implémentez cette méthode pour gérer le stockage du fichier
            ordonnance.setImagePath(imagePath); // Mettre à jour le chemin de l'image

            // Sauvegarder l'ordonnance mise à jour
            ordonnanceService.save(ordonnance); 

            return new ResponseEntity<>(true, HttpStatus.OK); // Renvoie true si tout s'est bien passé
        } catch (IOException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR); // Gérer l'erreur de manière appropriée
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST); // Autres erreurs
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrdonnanceDTO> getOrdonnanceById(@PathVariable Long id) {
        Ordonnance ordonnance = this.ordonnanceService.getById(id);

        // Vérifier si l'ordonnance existe
        if (ordonnance == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        OrdonnanceDTO ordonnanceDTO = new OrdonnanceDTO();
        ordonnanceDTO.setId(ordonnance.getId());
        ordonnanceDTO.setDatePrescription(ordonnance.getDatePrescription());

        // Assurer que la liste des médicaments est initialisée
        ordonnanceDTO.setMedicaments(ordonnance.getMedicaments() != null ? ordonnance.getMedicaments() : new ArrayList<>());

        // Convertir l'image en tableau d'octets
        String imagePath = ordonnance.getImagePath();
        if (imagePath != null) {
            try {
                ordonnanceDTO.setImageBytes(MediscanUtil.convertImageToByteArray(imagePath));
            } catch (IOException e) {
                e.printStackTrace(); // Considérer une meilleure gestion d'erreur ici
            }
        }

        // Retourner l'ordonnanceDTO avec un statut OK
        return new ResponseEntity<>(ordonnanceDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrdonnanceDTO>> getAll() {
        List<Ordonnance> ordonnances = this.ordonnanceService.getALl();

        // Vérification si la liste est vide
        if (ordonnances.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Création d'une liste pour les DTO
        List<OrdonnanceDTO> ordonnanceDTOs = new ArrayList<>();

        // Parcourir chaque ordonnance pour les convertir en DTO
        for (Ordonnance ordonnance : ordonnances) {
            // Initialisation d'un DTO pour chaque ordonnance
            OrdonnanceDTO ordonnanceDTO = new OrdonnanceDTO();
            ordonnanceDTO.setId(ordonnance.getId());
            ordonnanceDTO.setDatePrescription(ordonnance.getDatePrescription());

            // Vérification et initialisation de la liste des médicaments
            ordonnanceDTO.setMedicaments(
                    ordonnance.getMedicaments() != null ? ordonnance.getMedicaments() : new ArrayList<>());

            // Gestion de l'image
            String imagePath = ordonnance.getImagePath();
            if (imagePath != null) {
                try {
                    ordonnanceDTO.setImageBytes(MediscanUtil.convertImageToByteArray(imagePath));
                } catch (IOException e) {
                    e.printStackTrace(); // Considérer une meilleure gestion d'erreur ici
                }
            }

            // Ajouter le DTO dans la liste des DTOs
            ordonnanceDTOs.add(ordonnanceDTO);
        }

        // Retourner la liste des DTOs avec statut OK
        return new ResponseEntity<>(ordonnanceDTOs, HttpStatus.OK);
    }

    @GetMapping("{user_id}")
    public ResponseEntity<List<OrdonnanceDTO>> getAllOrdonnanceByUserId(@PathVariable Long user_id) {
        User user = this.userService.getById(user_id);
        List<Ordonnance> ordonnances = this.ordonnanceService.getAllByUser(user);
        
        // Vérification si la liste est vide
        if (ordonnances.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Création d'une liste pour les DTO
        List<OrdonnanceDTO> ordonnanceDTOs = new ArrayList<>();

        // Parcourir chaque ordonnance pour les convertir en DTO
        for (Ordonnance ordonnance : ordonnances) {
            // Initialisation d'un DTO pour chaque ordonnance
            OrdonnanceDTO ordonnanceDTO = new OrdonnanceDTO();
            ordonnanceDTO.setId(ordonnance.getId());
            ordonnanceDTO.setDatePrescription(ordonnance.getDatePrescription());

            // Vérification et initialisation de la liste des médicaments
            ordonnanceDTO.setMedicaments(
                    ordonnance.getMedicaments() != null ? ordonnance.getMedicaments() : new ArrayList<>());

            // Gestion de l'image
            String imagePath = ordonnance.getImagePath();
            if (imagePath != null) {
                try {
                    ordonnanceDTO.setImageBytes(MediscanUtil.convertImageToByteArray(imagePath));
                } catch (IOException e) {
                    e.printStackTrace(); // Considérer une meilleure gestion d'erreur ici
                }
            }

            // Ajouter le DTO dans la liste des DTOs
            ordonnanceDTOs.add(ordonnanceDTO);
        }

        // Retourner la liste des DTOs avec statut OK
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
