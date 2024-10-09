package net.epsi.mediscan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicamentDTO {
    private Long id;
    private String nom;
    private Integer dose;
    private Integer nbrPriseMatin; 
    private Integer nbrPriseMidi; 
    private Integer nbrPriseSoir;
    private LocalDate dateDebut; 
    private LocalDate dateFin;
}
