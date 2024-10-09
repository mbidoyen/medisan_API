package net.epsi.mediscan.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Data
public class Medicament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private Integer dose;
    private Integer nbrPriseMatin;
    private Integer nbrPriseMidi;
    private Integer nbrPriseSoir;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    @ManyToOne
    @JoinColumn(name = "ordonnance_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Ordonnance ordonnance;
}
