package net.epsi.mediscan.utils;

import net.epsi.mediscan.entities.Medicament;
import net.epsi.mediscan.entities.Ordonnance;

import java.util.List;

public class DateUtils {

    public static boolean checkDateMedicamentInOrdonnance(List<Ordonnance> ordonnances) {
        if (!ordonnances.isEmpty()) {
            for (Ordonnance ordonnance : ordonnances) {
                if (!checkDateMedicaments(ordonnance.getMedicaments())) {
                    return false;
                }
            }
        }
        
        return true;
    }

    public static boolean checkDateMedicaments(List<Medicament> medicaments) {
        if (!medicaments.isEmpty()) {
            for (Medicament medicament : medicaments) {
                if (!medicamentDateIsValid(medicament)) {
                    return false;
                }
            }
        }
        
        return true; // Tous les médicaments sont valides
    }

    public static boolean medicamentDateIsValid(Medicament medicament) {
        return medicament.getDateDebut().isBefore(medicament.getDateFin());
    }
}
