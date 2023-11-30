package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Dept;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Dept entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeptRepository extends MongoRepository<Dept, String> {}
