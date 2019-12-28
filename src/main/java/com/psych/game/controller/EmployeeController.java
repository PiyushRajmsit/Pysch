package com.psych.game.controller;

import com.psych.game.model.Employee;
import com.psych.game.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/dev")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employee")
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employee/{id}")
    public Employee getEmployeeById(@Valid @PathVariable(value = "id")Long id) throws Exception{
        return employeeRepository.findById(id).orElseThrow(Exception::new);
    }

}
