package com.ManageSys.service;

import com.ManageSys.common.JwtUtils;
import com.ManageSys.entity.Employee;
import com.ManageSys.exception.CustomException;
import com.ManageSys.mapper.EmployeeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    //增加员工
    public void add(Employee employee) {
        employeeMapper.insert(employee);
    }

    // 修改员工
    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    // 删除员工
    public void deleteById(Integer id) {
        employeeMapper.deleteById(id);
    }

    // 查询全部员工
    public List<Employee> selectAll(Employee employee) {
        List<Employee> list = employeeMapper.selectAll(employee);
        return list;
    }

    // 根据id查询员工
    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }

    public PageInfo<Employee> selectPage(Employee employee, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Employee> list = employeeMapper.selectAll(employee);
        return PageInfo.of(list);
    }


    public String login(Employee employee) {
        String username = employee.getUsername();
        Employee emp = employeeMapper.selectByUsername(username);
        if (emp == null) { // 用户不存在
            throw new CustomException("500", "用户不存在");
        }
        String password = employee.getPassword();
        if(!emp.getPassword().equals(password)){ // 密码错误
            throw new CustomException("500", "密码错误");
        }
        // 将员工信息封装到 JWT 中
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", emp.getId());
        claims.put("username", emp.getUsername());
        claims.put("name", emp.getName());
        claims.put("gender", emp.getGender());
        claims.put("image", emp.getImage());
        claims.put("job", emp.getJob());
        claims.put("entryDate", emp.getEntryDate());
        claims.put("deptId", emp.getDeptId());
        claims.put("createTime", emp.getCreateTime());

        return JwtUtils.createJwt(claims);

    }

    public void updatePassword(String token, String newPassword) {
        // 解析JWT
        Claims claims = JwtUtils.parseJWT(token);
        if (claims == null) {
            throw new CustomException("500", "令牌已过期或无效，请重新登录");
        }

        // 从Claims中获取用户id
        Integer id = (Integer) claims.get("id");
        Employee employee = employeeMapper.selectById(id);
        if (employee == null) {
            throw new CustomException("500", "用户不存在");
        }
        System.out.println(id);
        System.out.println(newPassword);
        employeeMapper.updatePassword(id, newPassword);
    }
}
