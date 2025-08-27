package com.example.curdUsingMongoDB.controller;

import com.example.curdUsingMongoDB.dto.EmployeesDto;
import com.example.curdUsingMongoDB.entity.Employees;
import com.example.curdUsingMongoDB.response.Response;
import com.example.curdUsingMongoDB.service.EmployeesService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import security.TestSecurityConfig;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private EmployeesService employeesService;

    private Employees employee;
    private EmployeesDto employeeDto;
    private List<Employees> employeesList;
    private List<EmployeesDto> employeeDTOList;
    private Response response;

    @BeforeEach
    void setUp() {
        employee = new Employees("2", "name2", "name2@mycompany.com", LocalDateTime.now(), LocalDateTime.now());
        employeeDto = new EmployeesDto("name2", "name2@mycompany.com", LocalDateTime.now(), LocalDateTime.now());

        employeesList = List.of(new Employees("0", "name1", "name1@mycompany.com",LocalDateTime.now(),LocalDateTime.now()),
                new Employees("1", "name2", "name2@mycompany.com",LocalDateTime.now(),LocalDateTime.now()),
                new Employees("2", "name3", "name3@mycompany.com",LocalDateTime.now(),LocalDateTime.now()),
                new Employees("3", "name4", "name4@mycompany.com",LocalDateTime.now(),LocalDateTime.now()),
                new Employees("4", "name5", "name5@mycompany.com",LocalDateTime.now(),LocalDateTime.now()),
                new Employees("5", "name6", "name6@mycompany.com",LocalDateTime.now(),LocalDateTime.now())
        );

        employeeDTOList = List.of(new EmployeesDto("name1", "name1@mycompany.com",LocalDateTime.now(),LocalDateTime.now()),
                new EmployeesDto("name2", "name2@mycompany.com",LocalDateTime.now(),LocalDateTime.now()),
                new EmployeesDto( "name3", "name3@mycompany.com",LocalDateTime.now(),LocalDateTime.now()),
                new EmployeesDto( "name4", "name4@mycompany.com",LocalDateTime.now(),LocalDateTime.now()),
                new EmployeesDto( "name5", "name5@mycompany.com",LocalDateTime.now(),LocalDateTime.now()),
                new EmployeesDto( "name6", "name6@mycompany.com",LocalDateTime.now(),LocalDateTime.now())
        );

        response = new Response("Employee successfully deleted",LocalDateTime.now(), String.valueOf(HttpStatus.OK.value()));
    }

    @Test
    void getByFilter(){
        given(employeesService.getFilteredDetails()).willReturn(Flux.fromIterable(employeeDTOList));

        webTestClient.get().uri("/ems/employees/filterByDomain")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeesDto.class)
                .hasSize(employeesList.size())
                .contains(employeeDto);

        Mockito.verify(employeesService, Mockito.times(1))
                .getFilteredDetails();
    }

    @Test
    void getAllEmployeesTest(){
        given(employeesService.getAllDetails()).willReturn(Flux.fromIterable(employeeDTOList));

        webTestClient.get().uri("/ems/employees")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeesDto.class)
                .hasSize(employeesList.size())
                .contains(employeeDto);

        Mockito.verify(employeesService, Mockito.times(1))
                .getAllDetails();

        Mockito.verify(employeesService, Mockito.times(2))
                .getAllDetails();

    }

    @Test
    void getAllEmployeesPageTest(){

        Pageable pageable = PageRequest.of(0, 5);
        Page<EmployeesDto> mockPage = new PageImpl<>(employeeDTOList,pageable,employeeDTOList.size());

        Mockito.when(employeesService.getAllDetailsPage(pageable)).thenReturn(Mono.just(mockPage));
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ems/employees")
                        .queryParam("offset", 0)
                        .queryParam("page", 5)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeesDto.class)

        ;

    }
@Test
void addEmployeesTest(){
    given(employeesService.createEmployee(any(EmployeesDto.class))).willReturn(Mono.just(employeeDto));

    webTestClient.post().uri("/ems/employee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(employeeDto),EmployeesDto.class)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody(EmployeesDto.class)
                    .value(responseDto -> {
                        assertEquals(employeeDto.getFullName(), responseDto.getFullName());
                        assertEquals(employeeDto.getEmail(), responseDto.getEmail());
                        assertEquals(employeeDto.getCreatedTimeStamp(), responseDto.getCreatedTimeStamp());
                        assertEquals(employeeDto.getLastModifiedTimeStamp(), responseDto.getLastModifiedTimeStamp());
                    });

    Mockito.verify(employeesService, Mockito.times(1))
            .createEmployee(any(EmployeesDto.class));

}

    @Test
    void updateEmployeesTest(){
        given(employeesService.updateEmployees(anyString(),any(EmployeesDto.class))).willReturn(Mono.just(employeeDto));

        webTestClient.put().uri("/ems/employee/1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto),EmployeesDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(EmployeesDto.class)
                .value(responseDto -> {
                    assertEquals(employeeDto.getFullName(), responseDto.getFullName());
                    assertEquals(employeeDto.getEmail(), responseDto.getEmail());
                    assertEquals(employeeDto.getCreatedTimeStamp(), responseDto.getCreatedTimeStamp());
                    assertEquals(employeeDto.getLastModifiedTimeStamp(), responseDto.getLastModifiedTimeStamp());
                });

        Mockito.verify(employeesService, Mockito.times(1))
                .updateEmployees(anyString(),any(EmployeesDto.class));

    }

    @Test
    void deleteEmployeesTest(){
        given(employeesService.deleteEmployees(anyString())).willReturn(Mono.just(response));

        webTestClient.delete().uri("/ems/employee/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Response.class)
                .value(responseMsg -> {
                    assertEquals(response.getMessage(),responseMsg.getMessage());

                });

        Mockito.verify(employeesService, Mockito.times(1))
                .deleteEmployees("1");

    }



    }


