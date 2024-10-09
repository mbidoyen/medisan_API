package net.epsi.mediscan.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import net.epsi.mediscan.entities.Medicament;
import net.epsi.mediscan.entities.User;

@Data
public class OrdonnanceRequest {
    private LocalDate datePrescription;
    private User user; // DTO pour l'utilisateur
    private List<Medicament> medicaments; // Votre classe Medicament
    private MultipartFile file;
}
