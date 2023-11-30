package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.EmpRepository;
import com.mycompany.myapp.service.EmpService;
import com.mycompany.myapp.service.dto.EmpDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Emp}.
 */
@RestController
@RequestMapping("/api/emps")
public class EmpResource {

    private final Logger log = LoggerFactory.getLogger(EmpResource.class);

    private static final String ENTITY_NAME = "emp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmpService empService;

    private final EmpRepository empRepository;

    public EmpResource(EmpService empService, EmpRepository empRepository) {
        this.empService = empService;
        this.empRepository = empRepository;
    }

    /**
     * {@code POST  /emps} : Create a new emp.
     *
     * @param empDTO the empDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new empDTO, or with status {@code 400 (Bad Request)} if the emp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmpDTO> createEmp(@RequestBody EmpDTO empDTO) throws URISyntaxException {
        log.debug("REST request to save Emp : {}", empDTO);
        if (empDTO.getId() != null) {
            throw new BadRequestAlertException("A new emp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmpDTO result = empService.save(empDTO);
        return ResponseEntity
            .created(new URI("/api/emps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /emps/:id} : Updates an existing emp.
     *
     * @param id the id of the empDTO to save.
     * @param empDTO the empDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empDTO,
     * or with status {@code 400 (Bad Request)} if the empDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the empDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpDTO> updateEmp(@PathVariable(value = "id", required = false) final String id, @RequestBody EmpDTO empDTO)
        throws URISyntaxException {
        log.debug("REST request to update Emp : {}, {}", id, empDTO);
        if (empDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmpDTO result = empService.update(empDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, empDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /emps/:id} : Partial updates given fields of an existing emp, field will ignore if it is null
     *
     * @param id the id of the empDTO to save.
     * @param empDTO the empDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated empDTO,
     * or with status {@code 400 (Bad Request)} if the empDTO is not valid,
     * or with status {@code 404 (Not Found)} if the empDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the empDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmpDTO> partialUpdateEmp(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody EmpDTO empDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Emp partially : {}, {}", id, empDTO);
        if (empDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, empDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!empRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmpDTO> result = empService.partialUpdate(empDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, empDTO.getId()));
    }

    /**
     * {@code GET  /emps} : get all the emps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emps in body.
     */
    @GetMapping("")
    public List<EmpDTO> getAllEmps() {
        log.debug("REST request to get all Emps");
        return empService.findAll();
    }

    /**
     * {@code GET  /emps/:id} : get the "id" emp.
     *
     * @param id the id of the empDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the empDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpDTO> getEmp(@PathVariable String id) {
        log.debug("REST request to get Emp : {}", id);
        Optional<EmpDTO> empDTO = empService.findOne(id);
        return ResponseUtil.wrapOrNotFound(empDTO);
    }

    /**
     * {@code DELETE  /emps/:id} : delete the "id" emp.
     *
     * @param id the id of the empDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmp(@PathVariable String id) {
        log.debug("REST request to delete Emp : {}", id);
        empService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
