package net.epsi.mediscan.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import net.epsi.mediscan.entities.Ordonnance;
import net.epsi.mediscan.entities.User;

public interface IOrdonnanceService {
    Ordonnance save(Ordonnance ordonnance);

    void update(Ordonnance ordonnance);

    void delete(Ordonnance ordonnance);

    String processImage(MultipartFile file, Ordonnance ordonnance) throws IllegalStateException, IOException;

    Ordonnance getById(long id);

    List<Ordonnance> getALl();
    List<Ordonnance> getAllByUser(User user);
}
