package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);


    /**
     * 新增员工
     *
     * @param employee 封装完成的员工信息对象
     */
    void addEmployee(Employee employee);

    /**
     * 分页查询员工信息
     *
     * @param name 员工姓名
     * @return
     */
    List<Employee> getEmployeesByPage(@Param("name") String name);

    /**
     * 根据ID查询员工信息
     * @param id 员工ID
     */
    @Select("select id, name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user from employee where id = #{id}")
    Employee getEmployeesById(@Param("id") Long id);

    /**
     * 根据ID修改员工信息
     * @param employee 员工信息封装
     */
    void updateEmployee(Employee employee);
}
