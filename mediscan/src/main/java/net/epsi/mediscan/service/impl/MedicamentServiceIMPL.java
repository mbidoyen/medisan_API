package net.epsi.mediscan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.epsi.mediscan.entities.Medicament;
import net.epsi.mediscan.repository.MedicamentRepository;
import net.epsi.mediscan.service.IMedicamentService;

@Service
public class MedicamentServiceIMPL implements IMedicamentService{

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Override
    public Medicament save(Medicament medicament) {
       return this.medicamentRepository.save(medicament);
    }

    @Override
    public void update(Medicament medicament) {
        this.medicamentRepository.save(medicament);
    }

    @Override
    public void delete(Medicament medicament) {
        this.medicamentRepository.delete(medicament);
    }

    @Override
    public Medicament getById(long id) {
        return this.medicamentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Medicament> getALl() {
        return this.medicamentRepository.findAll();
    }

}
