package net.epsi.mediscan.utils;

import net.epsi.mediscan.entities.Medicament;
import net.epsi.mediscan.entities.Ordonnance;
import net.epsi.mediscan.entities.User;
import java.util.List;

public class MediscanUtil {

    public static void setUserInOrdonnance(User user, List<Ordonnance> ordonnances) {
        if (!ordonnances.isEmpty()) {
            for (Ordonnance ordonnance : ordonnances) {
                ordonnance.setUser(user);
            }
        }
    }

    public static void setOrdonnanceInMedicament(Ordonnance ordonnance, List<Medicament> medicaments) {
        if (!medicaments.isEmpty()) {
            for (Medicament medicament : medicaments) {
                medicament.setOrdonnance(ordonnance);
            }
        }
    }
}
