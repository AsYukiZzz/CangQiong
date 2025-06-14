package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.context.CurrentHolderInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //数据库存储MD5加密后的密码，故现将密码用MD5加密再对比
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO 对前端请求json数据的封装
     */
    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        //获取当前操作账户ID
        Long currentHolder = getCurrentHolder();

        //对密码进行加密
        String password = DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes());

        Employee employee = Employee.builder()
                .name(employeeDTO.getName())                    //设置名字
                .username(employeeDTO.getUsername())            //设置用户名
                .password(password)                             //设置默认密码
                .phone(employeeDTO.getPhone())                  //设置手机号码
                .status(StatusConstant.ENABLE)                  //设置用户状态
                .sex(employeeDTO.getSex())                      //设置性别
                .idNumber(employeeDTO.getIdNumber())            //设置身份证号
                .build();

        employeeMapper.addEmployee(employee);

    }

    /**
     * 分页查询员工信息
     *
     * @param employeePageQueryDTO 分页查询数据封装对象
     */

    @Override
    public PageResult getEmployeesByPage(EmployeePageQueryDTO employeePageQueryDTO) {
        int page = employeePageQueryDTO.getPage();
        int pageSize = employeePageQueryDTO.getPageSize();

        //执行分页查询并返回结果
        PageHelper.startPage(
                page == 0 ? 1 : page,
                pageSize == 0 ? 10 : pageSize
        );

        // TODO 后续应使用PageInfo直接封装（修改resultType）
        List<Employee> empList = employeeMapper.getEmployeesByPage(employeePageQueryDTO.getName());

        //将PageHelper返回的集合强制转换为Page对象（Page继承List），以获取记录数与员工集合
        PageInfo<Employee> pageInfo = new PageInfo<>(empList);

        //返回员工集合
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 修改员工启用状态
     *
     * @param id     员工ID
     * @param status 员工状态
     */
    @Override
    public void updateEmployeeStatus(Long id, Integer status) {
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();

        employeeMapper.updateEmployee(employee);
    }

    /**
     * 根据ID查询员工信息
     *
     * @param id 员工ID
     */
    @Override
    public Employee getEmployeesById(Long id) {
        return employeeMapper.getEmployeesById(id);
    }

    /**
     * 根据ID修改员工信息
     *
     * @param employeeDTO
     */
    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = Employee.builder()
                .build();

        //拷贝属性
        BeanUtils.copyProperties(employeeDTO, employee);

        employeeMapper.updateEmployee(employee);
    }


    /**
     * 获取当前操作账户ID
     *
     * @return 返回操作账户ID
     */
    private Long getCurrentHolder() {
        return CurrentHolderInfo.getCurrentHolder();
    }

}
