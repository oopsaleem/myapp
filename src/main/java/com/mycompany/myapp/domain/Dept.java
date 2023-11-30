package com.mycompany.myapp.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Dept.
 */
@Document(collection = "dept")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("dept_name")
    private String deptName;

    @Field("dept_address")
    private String deptAddress;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Dept id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public Dept deptName(String deptName) {
        this.setDeptName(deptName);
        return this;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptAddress() {
        return this.deptAddress;
    }

    public Dept deptAddress(String deptAddress) {
        this.setDeptAddress(deptAddress);
        return this;
    }

    public void setDeptAddress(String deptAddress) {
        this.deptAddress = deptAddress;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dept)) {
            return false;
        }
        return getId() != null && getId().equals(((Dept) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dept{" +
            "id=" + getId() +
            ", deptName='" + getDeptName() + "'" +
            ", deptAddress='" + getDeptAddress() + "'" +
            "}";
    }
}
