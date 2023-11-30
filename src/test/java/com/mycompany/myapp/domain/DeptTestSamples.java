package com.mycompany.myapp.domain;

import java.util.UUID;

public class DeptTestSamples {

    public static Dept getDeptSample1() {
        return new Dept().id("id1").deptName("deptName1").deptAddress("deptAddress1");
    }

    public static Dept getDeptSample2() {
        return new Dept().id("id2").deptName("deptName2").deptAddress("deptAddress2");
    }

    public static Dept getDeptRandomSampleGenerator() {
        return new Dept().id(UUID.randomUUID().toString()).deptName(UUID.randomUUID().toString()).deptAddress(UUID.randomUUID().toString());
    }
}
