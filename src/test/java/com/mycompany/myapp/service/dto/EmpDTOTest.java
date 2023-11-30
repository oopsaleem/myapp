package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmpDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmpDTO.class);
        EmpDTO empDTO1 = new EmpDTO();
        empDTO1.setId("id1");
        EmpDTO empDTO2 = new EmpDTO();
        assertThat(empDTO1).isNotEqualTo(empDTO2);
        empDTO2.setId(empDTO1.getId());
        assertThat(empDTO1).isEqualTo(empDTO2);
        empDTO2.setId("id2");
        assertThat(empDTO1).isNotEqualTo(empDTO2);
        empDTO1.setId(null);
        assertThat(empDTO1).isNotEqualTo(empDTO2);
    }
}
