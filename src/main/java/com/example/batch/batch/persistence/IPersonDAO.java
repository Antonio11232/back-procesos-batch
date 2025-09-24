package com.example.batch.batch.persistence;

import com.example.batch.batch.entity.Person;
import org.springframework.data.repository.CrudRepository;


public interface IPersonDAO extends CrudRepository<Person,Long> {


}
