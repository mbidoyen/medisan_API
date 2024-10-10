package net.epsi.mediscan.utils;

import net.epsi.mediscan.entities.Medicament;
import net.epsi.mediscan.entities.Ordonnance;
import net.epsi.mediscan.entities.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static byte[] convertImageToByteArray(String imagePath) throws IOException {
        Path path = Path.of(imagePath);
        if (!Files.exists(path)) {
            throw new IOException("Le fichier d'image n'existe pas : " + imagePath);
        }

        return Files.readAllBytes(path);
    }
}
