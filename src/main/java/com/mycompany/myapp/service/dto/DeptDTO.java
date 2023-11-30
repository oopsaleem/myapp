package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Dept} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeptDTO implements Serializable {

    private String id;

    private String deptName;

    private String deptAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptAddress() {
        return deptAddress;
    }

    public void setDeptAddress(String deptAddress) {
        this.deptAddress = deptAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeptDTO)) {
            return false;
        }

        DeptDTO deptDTO = (DeptDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deptDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeptDTO{" +
            "id='" + getId() + "'" +
            ", deptName='" + getDeptName() + "'" +
            ", deptAddress='" + getDeptAddress() + "'" +
            "}";
    }
}
