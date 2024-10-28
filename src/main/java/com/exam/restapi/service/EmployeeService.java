package com.exam.restapi.service;

import com.exam.restapi.model.Employee;
import com.exam.restapi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Locale;
import java.util.List;

@Service
@Validated
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MessageSource messageSource;


    @Transactional
    public String addEmployee(Employee employee, Locale locale) {
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new IllegalArgumentException(messageSource.getMessage("email.already.in.use", null, locale));
        }
        employeeRepository.save(employee);
        return messageSource.getMessage("employee.added", null, locale);
    }


    @Transactional(readOnly = true)
    public Employee findEmployeeById(Long id, Locale locale) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        messageSource.getMessage("employee.not.found", null, locale)));
    }


    @Transactional(readOnly = true)
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional
    public String updateEmployee(Long id, Employee updatedEmployee, Locale locale) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        messageSource.getMessage("employee.not.found", null, locale)));

        // Vérifie si l'email est déjà utilisé par un autre employé
        if (employeeRepository.findByEmail(updatedEmployee.getEmail())
                .filter(e -> !e.getId().equals(id))
                .isPresent()) {
            throw new IllegalArgumentException(messageSource.getMessage("email.already.in.use", null, locale));
        }

        employee.setName(updatedEmployee.getName());
        employee.setEmail(updatedEmployee.getEmail());
        employeeRepository.save(employee);
        return messageSource.getMessage("employee.updated", null, locale);
    }

    // Supprimer un employé par ID
    @Transactional
    public String deleteEmployee(Long id, Locale locale) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        messageSource.getMessage("employee.not.found", null, locale)));

        employeeRepository.delete(employee);
        return messageSource.getMessage("employee.deleted", null, locale);
    }
}

