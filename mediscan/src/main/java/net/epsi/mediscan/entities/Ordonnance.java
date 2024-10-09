package net.epsi.mediscan.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Data
public class Ordonnance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identifiant unique de l'ordonnance

    private LocalDate datePrescription; // Date de prescription

    @ManyToOne // Relation ManyToOne avec User
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user; // Utilisateur associé à cette ordonnance

    @OneToMany(mappedBy = "ordonnance", fetch = FetchType.EAGER) // Relation avec Medicament
    private List<Medicament> medicaments; // Liste des médicaments associés à cette ordonnance
}
