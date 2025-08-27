package com.example.curdUsingMongoDB.mappers;

import com.example.curdUsingMongoDB.dto.EmployeesDto;
import com.example.curdUsingMongoDB.entity.Employees;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeesMapper {
    EmployeesMapper instance = Mappers.getMapper(EmployeesMapper.class);

    @Mapping(source = "name", target = "fullName")
    @Mapping(source = "employeeEmail", target = "email")
    EmployeesDto toDTO(Employees employees);

    @Mapping(source = "fullName", target = "name")
    @Mapping(source = "email", target = "employeeEmail")
    Employees toEntity(EmployeesDto employeesDTO);

    List<EmployeesDto> toDTOList(List<Employees> employeesList);

}
