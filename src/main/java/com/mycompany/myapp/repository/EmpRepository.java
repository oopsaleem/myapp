package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Emp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Emp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpRepository extends MongoRepository<Emp, String> {}
