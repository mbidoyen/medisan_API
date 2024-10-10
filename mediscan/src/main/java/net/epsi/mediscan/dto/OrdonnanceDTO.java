package net.epsi.mediscan.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import net.epsi.mediscan.entities.Medicament;

@Data
public class OrdonnanceDTO {
    private Long id;
    private LocalDate datePrescription;
    private List<Medicament> medicaments;
}
