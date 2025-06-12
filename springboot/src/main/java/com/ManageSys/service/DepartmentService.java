package com.ManageSys.service;

import com.ManageSys.entity.Department;
import com.ManageSys.mapper.DepartmentMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DepartmentService {
    @Resource
    private DepartmentMapper departmentMapper;

    // 查询所有部门信息
    public List<Department> selectAll(Department department) {
        List<Department> list = departmentMapper.selectAll(department);
        return list;
    }

    // 添加部门
    public void add(String name) {
        departmentMapper.insert(name);
    }

    // 修改部门
    public void updateById(String name, Integer id) {
        departmentMapper.updateById(name, id);
    }

    // 删除部门
    public void deleteById(Integer id) {
        departmentMapper.deleteById(id);
    }
}

