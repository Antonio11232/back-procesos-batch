package com.example.batch.batch.service.impl;

import com.example.batch.batch.entity.Person;
import com.example.batch.batch.persistence.IPersonDAO;
import com.example.batch.batch.service.IPersonService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class PersonServiceImpl implements IPersonService {

    private final IPersonDAO personDAO;

    public PersonServiceImpl(IPersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    @Transactional
    public void saveAll(List<Person> listadoPersonas) {
        personDAO.saveAll(listadoPersonas);
    }
}
