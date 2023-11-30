package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Dept;
import com.mycompany.myapp.service.dto.DeptDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dept} and its DTO {@link DeptDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeptMapper extends EntityMapper<DeptDTO, Dept> {}
