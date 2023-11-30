package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeptDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeptDTO.class);
        DeptDTO deptDTO1 = new DeptDTO();
        deptDTO1.setId("id1");
        DeptDTO deptDTO2 = new DeptDTO();
        assertThat(deptDTO1).isNotEqualTo(deptDTO2);
        deptDTO2.setId(deptDTO1.getId());
        assertThat(deptDTO1).isEqualTo(deptDTO2);
        deptDTO2.setId("id2");
        assertThat(deptDTO1).isNotEqualTo(deptDTO2);
        deptDTO1.setId(null);
        assertThat(deptDTO1).isNotEqualTo(deptDTO2);
    }
}
