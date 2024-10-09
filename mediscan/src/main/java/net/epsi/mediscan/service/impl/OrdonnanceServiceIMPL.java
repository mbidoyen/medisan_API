package net.epsi.mediscan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.epsi.mediscan.entities.Ordonnance;
import net.epsi.mediscan.repository.OrdonnanceRepository;
import net.epsi.mediscan.service.IOrdonnanceService;

@Service
public class OrdonnanceServiceIMPL implements IOrdonnanceService{

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

}
