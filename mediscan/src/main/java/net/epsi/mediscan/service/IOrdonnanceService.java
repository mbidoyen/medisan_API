package net.epsi.mediscan.service;

import java.util.List;

import net.epsi.mediscan.entities.Ordonnance;

public interface IOrdonnanceService {
    Ordonnance save(Ordonnance ordonnance);

    void update(Ordonnance ordonnance);

    void delete(Ordonnance ordonnance);

    Ordonnance getById(long id);

    List<Ordonnance> getALl();
}
