package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Dept;
import com.mycompany.myapp.repository.DeptRepository;
import com.mycompany.myapp.service.dto.DeptDTO;
import com.mycompany.myapp.service.mapper.DeptMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Dept}.
 */
@Service
public class DeptService {

    private final Logger log = LoggerFactory.getLogger(DeptService.class);

    private final DeptRepository deptRepository;

    private final DeptMapper deptMapper;

    public DeptService(DeptRepository deptRepository, DeptMapper deptMapper) {
        this.deptRepository = deptRepository;
        this.deptMapper = deptMapper;
    }

    /**
     * Save a dept.
     *
     * @param deptDTO the entity to save.
     * @return the persisted entity.
     */
    public DeptDTO save(DeptDTO deptDTO) {
        log.debug("Request to save Dept : {}", deptDTO);
        Dept dept = deptMapper.toEntity(deptDTO);
        dept = deptRepository.save(dept);
        return deptMapper.toDto(dept);
    }

    /**
     * Update a dept.
     *
     * @param deptDTO the entity to save.
     * @return the persisted entity.
     */
    public DeptDTO update(DeptDTO deptDTO) {
        log.debug("Request to update Dept : {}", deptDTO);
        Dept dept = deptMapper.toEntity(deptDTO);
        dept = deptRepository.save(dept);
        return deptMapper.toDto(dept);
    }

    /**
     * Partially update a dept.
     *
     * @param deptDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeptDTO> partialUpdate(DeptDTO deptDTO) {
        log.debug("Request to partially update Dept : {}", deptDTO);

        return deptRepository
            .findById(deptDTO.getId())
            .map(existingDept -> {
                deptMapper.partialUpdate(existingDept, deptDTO);

                return existingDept;
            })
            .map(deptRepository::save)
            .map(deptMapper::toDto);
    }

    /**
     * Get all the depts.
     *
     * @return the list of entities.
     */
    public List<DeptDTO> findAll() {
        log.debug("Request to get all Depts");
        return deptRepository.findAll().stream().map(deptMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one dept by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<DeptDTO> findOne(String id) {
        log.debug("Request to get Dept : {}", id);
        return deptRepository.findById(id).map(deptMapper::toDto);
    }

    /**
     * Delete the dept by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Dept : {}", id);
        deptRepository.deleteById(id);
    }
}
