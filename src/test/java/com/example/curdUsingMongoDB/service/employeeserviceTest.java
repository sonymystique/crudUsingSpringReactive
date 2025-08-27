package com.example.curdUsingMongoDB.service;

import com.example.curdUsingMongoDB.dto.EmployeesDto;
import com.example.curdUsingMongoDB.entity.Employees;
import com.example.curdUsingMongoDB.exceptions.DomainNotFoundException;
import com.example.curdUsingMongoDB.exceptions.EmployeeNotFoundException;
import com.example.curdUsingMongoDB.exceptions.InvalidInputException;
import com.example.curdUsingMongoDB.mappers.EmployeesMapper;
import com.example.curdUsingMongoDB.repository.EmployeeRepository;
import com.example.curdUsingMongoDB.response.Response;
import com.example.curdUsingMongoDB.service.impl.EmployeesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import static com.example.curdUsingMongoDB.service.impl.EmployeesServiceImpl.domain;
import static org.mockito.Mockito.when;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.ArgumentMatchers.any;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
public class employeeserviceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeesMapper employeesMapper;

    @InjectMocks
    private EmployeesServiceImpl employeesService;

    private Employees employee;
    private EmployeesDto employeeDto;
    private List<Employees> employeesList;
    private List<EmployeesDto> employeeDTOList;

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
    }
//positive testcase for normal getrequest
    @Test
    public void getAllDetailsTest() {
        Mockito.when(employeeRepository.findAll()).thenReturn(Flux.just(employee));
        Mockito.when(employeesMapper.toDTO(ArgumentMatchers.any(Employees.class))).thenReturn(employeeDto);

        StepVerifier.create(employeesService.getAllDetails())
                .expectNext(employeeDto)
                .expectComplete()
                .verify();

        verify(employeeRepository).findAll();
        verify(employeesMapper).toDTO(employee);
    }
//-ve test for normal getrequest
    @Test public void getAllDetailsTest_Exception(){
        when(employeeRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(employeesService.getAllDetails())
                .expectError(EmployeeNotFoundException.class)
                .verify();

    }

    //+ve test for filtered get request
    @Test
    public void getFilteredDetailsTest() {
        Mockito.when(employeeRepository.findByEmployeeEmailContaining(domain)).thenReturn(Flux.just(employee));
        Mockito.when(employeesMapper.toDTO(ArgumentMatchers.any(Employees.class))).thenReturn(employeeDto);

        StepVerifier.create(employeesService.getFilteredDetails())
                .expectNext(employeeDto)
                .expectComplete()
                .verify();

        verify(employeeRepository).findByEmployeeEmailContaining(domain);
        verify(employeesMapper).toDTO(employee);
    }

    @Test public void getFilteredDetailsTest_Exception(){
        when(employeeRepository.findByEmployeeEmailContaining(domain)).thenReturn(Flux.empty());

        StepVerifier.create(employeesService.getFilteredDetails())
                .expectError(DomainNotFoundException.class)
                .verify();

    }

    @Test
    public void createEmployeeTest() {
        Mockito.when(employeesMapper.toEntity(any(EmployeesDto.class))).thenReturn(employee);
        Mockito.when(employeeRepository.save(any(Employees.class))).thenReturn(Mono.just(employee));
        Mockito.when(employeesMapper.toDTO(any(Employees.class))).thenReturn(employeeDto);

        StepVerifier.create(employeesService.createEmployee(employeeDto))
                .expectNext(employeeDto)
                .expectComplete()
                .verify();

        verify(employeesMapper).toEntity(employeeDto);
        verify(employeeRepository).save(employee);
        verify(employeesMapper).toDTO(employee);
    }

    @Test
    void testCreateEmployee_InvalidInputException() {

        StepVerifier.create(employeesService.createEmployee(null))
                .expectError(InvalidInputException.class)
                .verify();

        Mockito.verifyNoInteractions(employeeRepository);
        Mockito.verifyNoInteractions(employeesMapper);
    }
//    for next exceptions in post +2

    @Test
    void updateEmployeesTest(){
        Mockito.when(employeeRepository.findById("1")).thenReturn(Mono.just(employee));
        Mockito.when(employeeRepository.save(any(Employees.class))).thenReturn(Mono.just(employee));
        Mockito.when(employeesMapper.toDTO(any(Employees.class))).thenReturn(employeeDto);

        Mono<EmployeesDto> result = employeesService.updateEmployees("1",employeeDto);

        StepVerifier.create(result)
                .expectNext(employeeDto)
                .verifyComplete();

        verify(employeeRepository).findById("1");
        verify(employeeRepository).save(any(Employees.class));
        verify(employeesMapper).toDTO(any(Employees.class));

    }

    @Test
    void updateEmployees_employeeNotFound() {
        when(employeeRepository.findById("nonExistentId")).thenReturn(Mono.empty());

        Mono<EmployeesDto> result = employeesService.updateEmployees("nonExistentId", employeeDto);

        StepVerifier.create(result)
                .expectError(EmployeeNotFoundException.class)
                .verify();

        verify(employeeRepository).findById("nonExistentId");
        verify(employeeRepository, Mockito.never()).save(any(Employees.class));
        verify(employeesMapper, Mockito.never()).toDTO(any(Employees.class));
    }

    @Test
    void DeleteEmployeesTest(){
        Mockito.when(employeeRepository.findById("1")).thenReturn(Mono.just(employee));
        Mockito.when(employeeRepository.deleteById("1")).thenReturn((Mono.empty()));

        Mono<Response> result = employeesService.deleteEmployees("1");

        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    return  response.getMessage().equals("the item has been deleted!")&&
                            response.getStatus().equals(String.valueOf(HttpStatus.OK.value()));
                })
                .verifyComplete();

        verify(employeeRepository).findById("1");
        verify(employeeRepository).deleteById("1");


    }
    @Test
    void deleteEmployees_success() {
        when(employeeRepository.findById("1")).thenReturn(Mono.just(employee));
        when(employeeRepository.deleteById("1")).thenReturn(Mono.empty());

        Mono<Response> result = employeesService.deleteEmployees("1");

        StepVerifier.create(result)
                .expectNextMatches(response ->
                    "the item has been deleted!".equals(response.getMessage()) &&
                            String.valueOf(HttpStatus.OK.value()).equals(response.getStatus()) &&
                            response.getTimestamp() != null
                    )
                .verifyComplete();

        verify(employeeRepository).findById("1");
        verify(employeeRepository).deleteById("1");
    }

    @Test
    public void getAllDetailsPageTest() {
        Pageable pageable = PageRequest.of(0, 5);
        long totalCount = employeesList.stream().count();
        Mockito.when(employeeRepository.findAllBy(pageable)).thenReturn(Flux.fromIterable(employeesList));
        Mockito.when(employeeRepository.count()).thenReturn(Mono.just(totalCount));

        Mockito.when(employeesMapper.toDTO(Mockito.any(Employees.class)))
                        .thenAnswer(invocation->{
                            Employees emp = invocation.getArgument(0);
                            return  new EmployeesDto(emp.getName(),emp.getEmployeeEmail(), emp.getCreatedTimeStamp(),emp.getLastModifiedTimeStamp());
                        });
        StepVerifier.create(employeesService.getAllDetailsPage(pageable))
                        .expectNextMatches(page->{
                            return page.getContent().size()==employeeDTOList.size() &&
                                    page.getContent().equals(employeeDTOList)&&
                                    page.getTotalElements()==totalCount &&
                                    page.getNumber()==pageable.getPageNumber();
                        })
                                .verifyComplete();

        Mockito.verify(employeeRepository).findAllBy(pageable);
        Mockito.verify(employeeRepository).count();
        Mockito.verify(employeesMapper, Mockito.times(employeesList.size())).toDTO(Mockito.any(Employees.class));
    }




}
