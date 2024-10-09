package net.epsi.mediscan.dto;

import net.epsi.mediscan.entities.Medicament;
import java.time.LocalDate;
import java.util.List;

public class OrdonnanceDTO {
    private Long id;
    private LocalDate datePrescription;
    private List<Medicament> medicaments;
}
