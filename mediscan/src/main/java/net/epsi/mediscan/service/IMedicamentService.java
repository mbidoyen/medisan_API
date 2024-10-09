package net.epsi.mediscan.service;

import java.util.List;

import net.epsi.mediscan.entities.Medicament;

public interface IMedicamentService {

    Medicament save(Medicament medicament);

    void update(Medicament medicament);

    void delete(Medicament medicament);

    Medicament getById(long id);

    List<Medicament> getALl();
}
