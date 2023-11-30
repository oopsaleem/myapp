package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.DeptRepository;
import com.mycompany.myapp.service.DeptService;
import com.mycompany.myapp.service.dto.DeptDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Dept}.
 */
@RestController
@RequestMapping("/api/depts")
public class DeptResource {

    private final Logger log = LoggerFactory.getLogger(DeptResource.class);

    private static final String ENTITY_NAME = "dept";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeptService deptService;

    private final DeptRepository deptRepository;

    public DeptResource(DeptService deptService, DeptRepository deptRepository) {
        this.deptService = deptService;
        this.deptRepository = deptRepository;
    }

    /**
     * {@code POST  /depts} : Create a new dept.
     *
     * @param deptDTO the deptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deptDTO, or with status {@code 400 (Bad Request)} if the dept has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DeptDTO> createDept(@RequestBody DeptDTO deptDTO) throws URISyntaxException {
        log.debug("REST request to save Dept : {}", deptDTO);
        if (deptDTO.getId() != null) {
            throw new BadRequestAlertException("A new dept cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeptDTO result = deptService.save(deptDTO);
        return ResponseEntity
            .created(new URI("/api/depts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /depts/:id} : Updates an existing dept.
     *
     * @param id the id of the deptDTO to save.
     * @param deptDTO the deptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deptDTO,
     * or with status {@code 400 (Bad Request)} if the deptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DeptDTO> updateDept(@PathVariable(value = "id", required = false) final String id, @RequestBody DeptDTO deptDTO)
        throws URISyntaxException {
        log.debug("REST request to update Dept : {}, {}", id, deptDTO);
        if (deptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeptDTO result = deptService.update(deptDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deptDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /depts/:id} : Partial updates given fields of an existing dept, field will ignore if it is null
     *
     * @param id the id of the deptDTO to save.
     * @param deptDTO the deptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deptDTO,
     * or with status {@code 400 (Bad Request)} if the deptDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deptDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeptDTO> partialUpdateDept(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DeptDTO deptDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dept partially : {}, {}", id, deptDTO);
        if (deptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeptDTO> result = deptService.partialUpdate(deptDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deptDTO.getId())
        );
    }

    /**
     * {@code GET  /depts} : get all the depts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depts in body.
     */
    @GetMapping("")
    public List<DeptDTO> getAllDepts() {
        log.debug("REST request to get all Depts");
        return deptService.findAll();
    }

    /**
     * {@code GET  /depts/:id} : get the "id" dept.
     *
     * @param id the id of the deptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeptDTO> getDept(@PathVariable String id) {
        log.debug("REST request to get Dept : {}", id);
        Optional<DeptDTO> deptDTO = deptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deptDTO);
    }

    /**
     * {@code DELETE  /depts/:id} : delete the "id" dept.
     *
     * @param id the id of the deptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDept(@PathVariable String id) {
        log.debug("REST request to delete Dept : {}", id);
        deptService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
