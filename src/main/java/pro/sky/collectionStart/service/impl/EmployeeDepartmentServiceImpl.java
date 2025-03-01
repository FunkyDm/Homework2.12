package pro.sky.collectionStart.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.collectionStart.exceptions.EmployeeWrongDepartmentNumberException;
import pro.sky.collectionStart.model.Employee;
import pro.sky.collectionStart.service.EmployeeDepartmentService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeDepartmentServiceImpl implements EmployeeDepartmentService {
    private final EmployeeServiceImpl employeeServiceImpl;

    public EmployeeDepartmentServiceImpl(EmployeeServiceImpl employeeServiceImpl) {
        this.employeeServiceImpl = employeeServiceImpl;
    }

    @Override
    public Map<Integer, List<Employee>> getEmployees() {
        return employeeServiceImpl.printAllEmployees()
                .stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }

    @Override
    public List<Employee> getEmployeesByDep(int departmentId) {
        checkDepartmentId(departmentId);
        return employeeServiceImpl.printAllEmployees()
                .stream()
                .filter(e -> (e.getDepartment() == departmentId))
                .sorted(Comparator.comparing(Employee::getDepartment))
                .collect(Collectors.toList());
    }

    @Override
    public double getEmployeeDepMaxSalary(int departmentId) {
        checkDepartmentId(departmentId);
        return employeeServiceImpl.printAllEmployees()
                .stream()
                .filter(e -> e.getDepartment() == departmentId)
                .max(Comparator.comparing(Employee::getSalary))
                .map(Employee::getSalary)
                .orElse(0.0);
    }

    @Override
    public double getEmployeeDepMinSalary(int departmentId) {
        checkDepartmentId(departmentId);
        return employeeServiceImpl.printAllEmployees()
                .stream()
                .filter(e -> e.getDepartment() == departmentId)
                .min(Comparator.comparing(Employee::getSalary))
                .map(Employee::getSalary)
                .orElse(0.0);
    }

    @Override
    public double getEmployeeDepSalarySum(int departmentId) {
        checkDepartmentId(departmentId);
        return employeeServiceImpl.printAllEmployees()
                .stream()
                .filter(e -> e.getDepartment() == departmentId)
                .mapToDouble(Employee::getSalary)
                .sum();
    }

    private void checkDepartmentId(int departmentId) {
        Optional.of(departmentId)
                .filter(id -> id >= 1 && id <= 5)
                .orElseThrow(() -> new EmployeeWrongDepartmentNumberException("Установлен неправильный номер отдела."));
    }

}
