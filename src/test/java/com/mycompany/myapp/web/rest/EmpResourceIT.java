package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Emp;
import com.mycompany.myapp.repository.EmpRepository;
import com.mycompany.myapp.service.dto.EmpDTO;
import com.mycompany.myapp.service.mapper.EmpMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link EmpResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmpResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/emps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private EmpRepository empRepository;

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private MockMvc restEmpMockMvc;

    private Emp emp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emp createEntity() {
        Emp emp = new Emp().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME);
        return emp;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emp createUpdatedEntity() {
        Emp emp = new Emp().firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);
        return emp;
    }

    @BeforeEach
    public void initTest() {
        empRepository.deleteAll();
        emp = createEntity();
    }

    @Test
    void createEmp() throws Exception {
        int databaseSizeBeforeCreate = empRepository.findAll().size();
        // Create the Emp
        EmpDTO empDTO = empMapper.toDto(emp);
        restEmpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empDTO)))
            .andExpect(status().isCreated());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeCreate + 1);
        Emp testEmp = empList.get(empList.size() - 1);
        assertThat(testEmp.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmp.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    void createEmpWithExistingId() throws Exception {
        // Create the Emp with an existing ID
        emp.setId("existing_id");
        EmpDTO empDTO = empMapper.toDto(emp);

        int databaseSizeBeforeCreate = empRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEmps() throws Exception {
        // Initialize the database
        empRepository.save(emp);

        // Get all the empList
        restEmpMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emp.getId())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)));
    }

    @Test
    void getEmp() throws Exception {
        // Initialize the database
        empRepository.save(emp);

        // Get the emp
        restEmpMockMvc
            .perform(get(ENTITY_API_URL_ID, emp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emp.getId()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME));
    }

    @Test
    void getNonExistingEmp() throws Exception {
        // Get the emp
        restEmpMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingEmp() throws Exception {
        // Initialize the database
        empRepository.save(emp);

        int databaseSizeBeforeUpdate = empRepository.findAll().size();

        // Update the emp
        Emp updatedEmp = empRepository.findById(emp.getId()).orElseThrow();
        updatedEmp.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);
        EmpDTO empDTO = empMapper.toDto(updatedEmp);

        restEmpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empDTO))
            )
            .andExpect(status().isOk());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
        Emp testEmp = empList.get(empList.size() - 1);
        assertThat(testEmp.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmp.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    void putNonExistingEmp() throws Exception {
        int databaseSizeBeforeUpdate = empRepository.findAll().size();
        emp.setId(UUID.randomUUID().toString());

        // Create the Emp
        EmpDTO empDTO = empMapper.toDto(emp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEmp() throws Exception {
        int databaseSizeBeforeUpdate = empRepository.findAll().size();
        emp.setId(UUID.randomUUID().toString());

        // Create the Emp
        EmpDTO empDTO = empMapper.toDto(emp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(empDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEmp() throws Exception {
        int databaseSizeBeforeUpdate = empRepository.findAll().size();
        emp.setId(UUID.randomUUID().toString());

        // Create the Emp
        EmpDTO empDTO = empMapper.toDto(emp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(empDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEmpWithPatch() throws Exception {
        // Initialize the database
        empRepository.save(emp);

        int databaseSizeBeforeUpdate = empRepository.findAll().size();

        // Update the emp using partial update
        Emp partialUpdatedEmp = new Emp();
        partialUpdatedEmp.setId(emp.getId());

        partialUpdatedEmp.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restEmpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmp))
            )
            .andExpect(status().isOk());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
        Emp testEmp = empList.get(empList.size() - 1);
        assertThat(testEmp.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmp.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    void fullUpdateEmpWithPatch() throws Exception {
        // Initialize the database
        empRepository.save(emp);

        int databaseSizeBeforeUpdate = empRepository.findAll().size();

        // Update the emp using partial update
        Emp partialUpdatedEmp = new Emp();
        partialUpdatedEmp.setId(emp.getId());

        partialUpdatedEmp.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restEmpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmp))
            )
            .andExpect(status().isOk());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
        Emp testEmp = empList.get(empList.size() - 1);
        assertThat(testEmp.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmp.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    void patchNonExistingEmp() throws Exception {
        int databaseSizeBeforeUpdate = empRepository.findAll().size();
        emp.setId(UUID.randomUUID().toString());

        // Create the Emp
        EmpDTO empDTO = empMapper.toDto(emp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, empDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(empDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEmp() throws Exception {
        int databaseSizeBeforeUpdate = empRepository.findAll().size();
        emp.setId(UUID.randomUUID().toString());

        // Create the Emp
        EmpDTO empDTO = empMapper.toDto(emp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(empDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEmp() throws Exception {
        int databaseSizeBeforeUpdate = empRepository.findAll().size();
        emp.setId(UUID.randomUUID().toString());

        // Create the Emp
        EmpDTO empDTO = empMapper.toDto(emp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(empDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Emp in the database
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEmp() throws Exception {
        // Initialize the database
        empRepository.save(emp);

        int databaseSizeBeforeDelete = empRepository.findAll().size();

        // Delete the emp
        restEmpMockMvc.perform(delete(ENTITY_API_URL_ID, emp.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Emp> empList = empRepository.findAll();
        assertThat(empList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
