package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Dept;
import com.mycompany.myapp.repository.DeptRepository;
import com.mycompany.myapp.service.dto.DeptDTO;
import com.mycompany.myapp.service.mapper.DeptMapper;
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
 * Integration tests for the {@link DeptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeptResourceIT {

    private static final String DEFAULT_DEPT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEPT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_DEPT_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/depts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private MockMvc restDeptMockMvc;

    private Dept dept;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dept createEntity() {
        Dept dept = new Dept().deptName(DEFAULT_DEPT_NAME).deptAddress(DEFAULT_DEPT_ADDRESS);
        return dept;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dept createUpdatedEntity() {
        Dept dept = new Dept().deptName(UPDATED_DEPT_NAME).deptAddress(UPDATED_DEPT_ADDRESS);
        return dept;
    }

    @BeforeEach
    public void initTest() {
        deptRepository.deleteAll();
        dept = createEntity();
    }

    @Test
    void createDept() throws Exception {
        int databaseSizeBeforeCreate = deptRepository.findAll().size();
        // Create the Dept
        DeptDTO deptDTO = deptMapper.toDto(dept);
        restDeptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deptDTO)))
            .andExpect(status().isCreated());

        // Validate the Dept in the database
        List<Dept> deptList = deptRepository.findAll();
        assertThat(deptList).hasSize(databaseSizeBeforeCreate + 1);
        Dept testDept = deptList.get(deptList.size() - 1);
        assertThat(testDept.getDeptName()).isEqualTo(DEFAULT_DEPT_NAME);
        assertThat(testDept.getDeptAddress()).isEqualTo(DEFAULT_DEPT_ADDRESS);
    }

    @Test
    void createDeptWithExistingId() throws Exception {
        // Create the Dept with an existing ID
        dept.setId("existing_id");
        DeptDTO deptDTO = deptMapper.toDto(dept);

        int databaseSizeBeforeCreate = deptRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dept in the database
        List<Dept> deptList = deptRepository.findAll();
        assertThat(deptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDepts() throws Exception {
        // Initialize the database
        deptRepository.save(dept);

        // Get all the deptList
        restDeptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dept.getId())))
            .andExpect(jsonPath("$.[*].deptName").value(hasItem(DEFAULT_DEPT_NAME)))
            .andExpect(jsonPath("$.[*].deptAddress").value(hasItem(DEFAULT_DEPT_ADDRESS)));
    }

    @Test
    void getDept() throws Exception {
        // Initialize the database
        deptRepository.save(dept);

        // Get the dept
        restDeptMockMvc
            .perform(get(ENTITY_API_URL_ID, dept.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dept.getId()))
            .andExpect(jsonPath("$.deptName").value(DEFAULT_DEPT_NAME))
            .andExpect(jsonPath("$.deptAddress").value(DEFAULT_DEPT_ADDRESS));
    }

    @Test
    void getNonExistingDept() throws Exception {
        // Get the dept
        restDeptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingDept() throws Exception {
        // Initialize the database
        deptRepository.save(dept);

        int databaseSizeBeforeUpdate = deptRepository.findAll().size();

        // Update the dept
        Dept updatedDept = deptRepository.findById(dept.getId()).orElseThrow();
        updatedDept.deptName(UPDATED_DEPT_NAME).deptAddress(UPDATED_DEPT_ADDRESS);
        DeptDTO deptDTO = deptMapper.toDto(updatedDept);

        restDeptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deptDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dept in the database
        List<Dept> deptList = deptRepository.findAll();
        assertThat(deptList).hasSize(databaseSizeBeforeUpdate);
        Dept testDept = deptList.get(deptList.size() - 1);
        assertThat(testDept.getDeptName()).isEqualTo(UPDATED_DEPT_NAME);
        assertThat(testDept.getDeptAddress()).isEqualTo(UPDATED_DEPT_ADDRESS);
    }

    @Test
    void putNonExistingDept() throws Exception {
        int databaseSizeBeforeUpdate = deptRepository.findAll().size();
        dept.setId(UUID.randomUUID().toString());

        // Create the Dept
        DeptDTO deptDTO = deptMapper.toDto(dept);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dept in the database
        List<Dept> deptList = deptRepository.findAll();
        assertThat(deptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDept() throws Exception {
        int databaseSizeBeforeUpdate = deptRepository.findAll().size();
        dept.setId(UUID.randomUUID().toString());

        // Create the Dept
        DeptDTO deptDTO = deptMapper.toDto(dept);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dept in the database
        List<Dept> deptList = deptRepository.findAll();
        assertThat(deptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDept() throws Exception {
        int databaseSizeBeforeUpdate = deptRepository.findAll().size();
        dept.setId(UUID.randomUUID().toString());

        // Create the Dept
        DeptDTO deptDTO = deptMapper.toDto(dept);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dept in the database
        List<Dept> deptList = deptRepository.findAll();
        assertThat(deptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDeptWithPatch() throws Exception {
        // Initialize the database
        deptRepository.save(dept);

        int databaseSizeBeforeUpdate = deptRepository.findAll().size();

        // Update the dept using partial update
        Dept partialUpdatedDept = new Dept();
        partialUpdatedDept.setId(dept.getId());

        partialUpdatedDept.deptName(UPDATED_DEPT_NAME).deptAddress(UPDATED_DEPT_ADDRESS);

        restDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDept.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDept))
            )
            .andExpect(status().isOk());

        // Validate the Dept in the database
        List<Dept> deptList = deptRepository.findAll();
        assertThat(deptList).hasSize(databaseSizeBeforeUpdate);
        Dept testDept = deptList.get(deptList.size() - 1);
        assertThat(testDept.getDeptName()).isEqualTo(UPDATED_DEPT_NAME);
        assertThat(testDept.getDeptAddress()).isEqualTo(UPDATED_DEPT_ADDRESS);
    }

    @Test
    void fullUpdateDeptWithPatch() throws Exception {
        // Initialize the database
        deptRepository.save(dept);

        int databaseSizeBeforeUpdate = deptRepository.findAll().size();

        // Update the dept using partial update
        Dept partialUpdatedDept = new Dept();
        partialUpdatedDept.setId(dept.getId());

        partialUpdatedDept.deptName(UPDATED_DEPT_NAME).deptAddress(UPDATED_DEPT_ADDRESS);

        restDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDept.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDept))
            )
            .andExpect(status().isOk());

        // Validate the Dept in the database
        List<Dept> deptList = deptRepository.findAll();
        assertThat(deptList).hasSize(databaseSizeBeforeUpdate);
        Dept testDept = deptList.get(deptList.size() - 1);
        assertThat(testDept.getDeptName()).isEqualTo(UPDATED_DEPT_NAME);
        assertThat(testDept.getDeptAddress()).isEqualTo(UPDATED_DEPT_ADDRESS);
    }

    @Test
    void patchNonExistingDept() throws Exception {
        int databaseSizeBeforeUpdate = deptRepository.findAll().size();
        dept.setId(UUID.randomUUID().toString());

        // Create the Dept
        DeptDTO deptDTO = deptMapper.toDto(dept);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deptDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dept in the database
        List<Dept> deptList = deptRepository.findAll();
        assertThat(deptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDept() throws Exception {
        int databaseSizeBeforeUpdate = deptRepository.findAll().size();
        dept.setId(UUID.randomUUID().toString());

        // Create the Dept
        DeptDTO deptDTO = deptMapper.toDto(dept);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dept in the database
        List<Dept> deptList = deptRepository.findAll();
        assertThat(deptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDept() throws Exception {
        int databaseSizeBeforeUpdate = deptRepository.findAll().size();
        dept.setId(UUID.randomUUID().toString());

        // Create the Dept
        DeptDTO deptDTO = deptMapper.toDto(dept);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeptMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dept in the database
        List<Dept> deptList = deptRepository.findAll();
        assertThat(deptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDept() throws Exception {
        // Initialize the database
        deptRepository.save(dept);

        int databaseSizeBeforeDelete = deptRepository.findAll().size();

        // Delete the dept
        restDeptMockMvc
            .perform(delete(ENTITY_API_URL_ID, dept.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dept> deptList = deptRepository.findAll();
        assertThat(deptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
