package com.example.batch.batch.service;

import com.example.batch.batch.entity.Person;

import java.util.List;

public interface IPersonService {

    void saveAll(List<Person> listadoPersonas);
}
