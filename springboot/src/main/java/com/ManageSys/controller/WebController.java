package com.ManageSys.controller;

import com.ManageSys.common.Result;
import com.ManageSys.entity.Employee;
import com.ManageSys.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Resource
    private EmployeeService employeeService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Employee employee) {
        String token = employeeService.login(employee);
        return Result.loginSuccess("登录成功", token);
    }
}
