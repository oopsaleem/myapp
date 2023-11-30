package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EmpTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmpTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Emp.class);
        Emp emp1 = getEmpSample1();
        Emp emp2 = new Emp();
        assertThat(emp1).isNotEqualTo(emp2);

        emp2.setId(emp1.getId());
        assertThat(emp1).isEqualTo(emp2);

        emp2 = getEmpSample2();
        assertThat(emp1).isNotEqualTo(emp2);
    }
}
