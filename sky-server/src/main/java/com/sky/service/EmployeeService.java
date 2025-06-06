package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void addEmployee(EmployeeDTO employeeDTO);

    PageResult getEmployeesByPage(EmployeePageQueryDTO employeePageQueryDTO);

    void updateEmployeeStatus(Long id, Integer status);

    Employee getEmployeesById(Long id);

    void updateEmployee(EmployeeDTO employeeDTO);
}
