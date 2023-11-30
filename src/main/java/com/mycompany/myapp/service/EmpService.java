package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Emp;
import com.mycompany.myapp.repository.EmpRepository;
import com.mycompany.myapp.service.dto.EmpDTO;
import com.mycompany.myapp.service.mapper.EmpMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Emp}.
 */
@Service
public class EmpService {

    private final Logger log = LoggerFactory.getLogger(EmpService.class);

    private final EmpRepository empRepository;

    private final EmpMapper empMapper;

    public EmpService(EmpRepository empRepository, EmpMapper empMapper) {
        this.empRepository = empRepository;
        this.empMapper = empMapper;
    }

    /**
     * Save a emp.
     *
     * @param empDTO the entity to save.
     * @return the persisted entity.
     */
    public EmpDTO save(EmpDTO empDTO) {
        log.debug("Request to save Emp : {}", empDTO);
        Emp emp = empMapper.toEntity(empDTO);
        emp = empRepository.save(emp);
        return empMapper.toDto(emp);
    }

    /**
     * Update a emp.
     *
     * @param empDTO the entity to save.
     * @return the persisted entity.
     */
    public EmpDTO update(EmpDTO empDTO) {
        log.debug("Request to update Emp : {}", empDTO);
        Emp emp = empMapper.toEntity(empDTO);
        emp = empRepository.save(emp);
        return empMapper.toDto(emp);
    }

    /**
     * Partially update a emp.
     *
     * @param empDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmpDTO> partialUpdate(EmpDTO empDTO) {
        log.debug("Request to partially update Emp : {}", empDTO);

        return empRepository
            .findById(empDTO.getId())
            .map(existingEmp -> {
                empMapper.partialUpdate(existingEmp, empDTO);

                return existingEmp;
            })
            .map(empRepository::save)
            .map(empMapper::toDto);
    }

    /**
     * Get all the emps.
     *
     * @return the list of entities.
     */
    public List<EmpDTO> findAll() {
        log.debug("Request to get all Emps");
        return empRepository.findAll().stream().map(empMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one emp by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<EmpDTO> findOne(String id) {
        log.debug("Request to get Emp : {}", id);
        return empRepository.findById(id).map(empMapper::toDto);
    }

    /**
     * Delete the emp by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Emp : {}", id);
        empRepository.deleteById(id);
    }
}
