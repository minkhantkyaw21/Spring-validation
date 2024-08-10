package com.min.validation.controller;

import com.min.validation.entity.Employee;
import com.min.validation.error.EmployeeNotFoundException;
import com.min.validation.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
public class HomeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    public static String uploadDirectory=System.getProperty("user.dir")+"/src/main/resources/images";

    @GetMapping("/")
    public String home(){
        return "Welcome to Global Exception Handling!!";
    }
    @PostMapping("/saveEmployee")
    public String save(@ModelAttribute Employee employee, @RequestParam("image")MultipartFile file)throws IOException {
        String originalFilename = file.getOriginalFilename();
        Path fileNmaeAndPath= Paths.get(uploadDirectory,originalFilename);
        Files.write((java.nio.file.Path) fileNmaeAndPath,file.getBytes());
        employee.setProfileImage(originalFilename);
        employeeRepository.save(employee);
        return "Employee saved successfully!";
    }
    @GetMapping("/getEmployee/{eid}")
    public Employee getData(@PathVariable("eid") int eid) throws EmployeeNotFoundException {
        Optional<Employee> employeeData= employeeRepository.findById(eid);
        if(!employeeData.isPresent())
            throw new EmployeeNotFoundException("Employee is not available for this eid.");
        Employee employee=employeeData.get();
        return  employee ;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleException(MethodArgumentNotValidException ex){
        return "Please check the input field again: Some fields are missing";
    }
}
