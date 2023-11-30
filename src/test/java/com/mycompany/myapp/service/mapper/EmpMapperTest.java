package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class EmpMapperTest {

    private EmpMapper empMapper;

    @BeforeEach
    public void setUp() {
        empMapper = new EmpMapperImpl();
    }
}
