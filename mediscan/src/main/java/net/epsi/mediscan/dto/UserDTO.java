package net.epsi.mediscan.dto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    private Long id; 
    private String prenom;
    private String nom;
    private LocalDate birthday;
    private List<OrdonnanceDTO> ordonnances = new ArrayList<>();
}
