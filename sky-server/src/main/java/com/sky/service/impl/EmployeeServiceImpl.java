package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;
import com.sky.context.CurrentHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

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
                .createTime(LocalDateTime.now())                //设置创建时间
                .updateTime(LocalDateTime.now())                //设置更新时间
                .createUser(currentHolder)                      //设置创建该员工信息的账户
                .updateUser(currentHolder)                      //设置最后一次操作账户
                .build();

        employeeMapper.addEmployee(employee);

    }


    /**
     * 获取当前操作账户ID
     *
     * @return 返回操作账户ID
     */
    private Long getCurrentHolder() {
        return CurrentHolder.getCurrentHolder();
    }

}
