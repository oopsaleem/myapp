package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DeptTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dept.class);
        Dept dept1 = getDeptSample1();
        Dept dept2 = new Dept();
        assertThat(dept1).isNotEqualTo(dept2);

        dept2.setId(dept1.getId());
        assertThat(dept1).isEqualTo(dept2);

        dept2 = getDeptSample2();
        assertThat(dept1).isNotEqualTo(dept2);
    }
}
