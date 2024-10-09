package net.epsi.mediscan.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Medicament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identifiant unique du médicament

    private String nom; // Nom du médicament
    private Integer dose; // Dose du médicament
    private Integer nbrPriseMatin; // Nombre de prises le matin
    private Integer nbrPriseMidi; // Nombre de prises à midi
    private Integer nbrPriseSoir; // Nombre de prises le soir
    private LocalDate dateDebut; // Date de début de prise
    private LocalDate dateFin; // Date de fin de prise

    @ManyToOne // Relation ManyToOne avec Ordonnance
    @JoinColumn(name = "ordonnance_id")
    @JsonIgnore // Colonne qui représente la clé étrangère dans la table Medicament
    private Ordonnance ordonnance; // Ordonnance associée à ce médicament
}
