package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @param employeeDTO 对前端发送的json格式数据的封装
     * @return
     */
    @PostMapping("")
    public Result<String> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO);
        employeeService.addEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询员工
     *
     * @param employeePageQueryDTO 分页查询数据封装
     */
    @GetMapping("/page")
    public Result<PageResult> getEmployeesByPage(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询：{}", employeePageQueryDTO);
        return Result.success(employeeService.getEmployeesByPage(employeePageQueryDTO));
    }

    /**
     * 启用、禁用员工账号
     * @param id 被修改员工对应ID
     * @param status 员工状态数值
     */
    @PostMapping("/status/{status}")
    public Result<String> updateEmployeeStatus(@RequestParam("id") Long id, @PathVariable("status") Integer status) {
        log.info("修改员工启用状态，员工id={}，状态更改为status={}", id, status);
        employeeService.updateEmployeeStatus(id,status);
        return Result.success();
    }

    /**
     * 根据ID查询员工信息
     * @param id 员工ID
     */
    @GetMapping("/{id}")
    public Result<Employee> getEmployeeById(@PathVariable("id") Long id) {
        log.info("根据id查询员工信息，id={}",id);
        return Result.success(employeeService.getEmployeesById(id));
    }
}
