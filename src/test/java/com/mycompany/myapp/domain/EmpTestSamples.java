package com.mycompany.myapp.domain;

import java.util.UUID;

public class EmpTestSamples {

    public static Emp getEmpSample1() {
        return new Emp().id("id1").firstName("firstName1").lastName("lastName1");
    }

    public static Emp getEmpSample2() {
        return new Emp().id("id2").firstName("firstName2").lastName("lastName2");
    }

    public static Emp getEmpRandomSampleGenerator() {
        return new Emp().id(UUID.randomUUID().toString()).firstName(UUID.randomUUID().toString()).lastName(UUID.randomUUID().toString());
    }
}
