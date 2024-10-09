package net.epsi.mediscan.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.epsi.mediscan.entities.Ordonnance;
import net.epsi.mediscan.entities.User;
import net.epsi.mediscan.repository.OrdonnanceRepository;
import net.epsi.mediscan.service.IOrdonnanceService;

@Service
public class OrdonnanceServiceIMPL implements IOrdonnanceService {

    @Autowired
    private OrdonnanceRepository ordonnanceRepository;

    @Override
    public Ordonnance save(Ordonnance ordonnance) {
        return this.ordonnanceRepository.save(ordonnance);
    }

    @Override
    public void update(Ordonnance ordonnance) {
        this.ordonnanceRepository.save(ordonnance);
    }

    @Override
    public void delete(Ordonnance ordonnance) {
        this.ordonnanceRepository.delete(ordonnance);
    }

    @Override
    public Ordonnance getById(long id) {
        return this.ordonnanceRepository.findById(id).orElse(null);
    }

    @Override
    public List<Ordonnance> getALl() {
        return this.ordonnanceRepository.findAll();
    }

    @Override
    public String processImage(MultipartFile file, Ordonnance ordonnance) throws IOException {
        // Normaliser le nom du fichier
        @SuppressWarnings("null")
        String normalizedFileName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.\\-_]", "_");

        // Utiliser un chemin absolu
        String baseDir = System.getProperty("user.dir") + "/images"; // Répertoire d'images
        String imagePath = baseDir + "/" + ordonnance.getId() + "_" + normalizedFileName;

        // Créer le répertoire si nécessaire
        File imagesDir = new File(baseDir);
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }

        Path destinationPath = Paths.get(imagePath);

        // Vérifier si le fichier existe déjà
        if (Files.exists(destinationPath)) {
            throw new IOException("Le fichier existe déjà : " + destinationPath.toString());
        }

        // Vérifier si le fichier est vide
        if (file.isEmpty()) {
            throw new IOException("Le fichier est vide : " + file.getOriginalFilename());
        }

        // Essayer de transférer le fichier
        try {
            file.transferTo(destinationPath.toFile());
        } catch (Exception e) {
            e.printStackTrace(); // Imprime la trace de l'exception pour le débogage
            throw new IOException("Erreur lors du traitement du fichier : " + e.getMessage(), e);
        }

        // Retourner le chemin de l'image
        return destinationPath.toString(); // Retourne le chemin absolu
    }

    @Override
    public List<Ordonnance> getAllByUser(User user) {
        return this.ordonnanceRepository.findByUser(user);
    }

}
