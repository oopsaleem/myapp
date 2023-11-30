package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Emp;
import com.mycompany.myapp.service.dto.EmpDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Emp} and its DTO {@link EmpDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmpMapper extends EntityMapper<EmpDTO, Emp> {}
