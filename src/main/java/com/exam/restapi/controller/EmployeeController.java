package com.exam.restapi.controller;

import com.exam.restapi.model.Employee;
import com.exam.restapi.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MessageSource messageSource;

    // Ajouter un employé
    @PostMapping
    public ResponseEntity<String> addEmployee(@Valid @RequestBody Employee employee, Locale locale) {
        String message = employeeService.addEmployee(employee, locale);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    // Récupérer un employé par ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id, Locale locale) {
        Employee employee = employeeService.findEmployeeById(id, locale);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    // Récupérer tous les employés
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.findAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    // Mettre à jour un employé
    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee updatedEmployee, Locale locale) {
        String message = employeeService.updateEmployee(id, updatedEmployee, locale);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // Supprimer un employé
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id, Locale locale) {
        String message = employeeService.deleteEmployee(id, locale);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // Message d'erreur personnalisé en cas de mauvaise requête
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex, Locale locale) {
        String errorMessage = messageSource.getMessage("error.bad.request", null, locale);
        return new ResponseEntity<>(errorMessage + ": " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

