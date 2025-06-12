package com.ManageSys.controller;

import com.ManageSys.common.Result;
import com.ManageSys.entity.Department;
import com.ManageSys.service.DepartmentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depts")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    /**
     * 查询部门所有数据
     */
    @GetMapping("/selectAll")
    public Result selectAll(Department department) {
        List<Department> list = departmentService.selectAll(department);
        return Result.success("查询所有部门成功", list);
    }

    /**
     * 添加部门
     */
    @PostMapping("/add")
    public Result add(@RequestBody Department department) {
        departmentService.add(department.getName());
        return Result.success("添加部门成功");
    }

    /**
     * 修改部门
     */
    @PutMapping("/update/{id}/{name}")
    public Result update(@PathVariable String name, @PathVariable Integer id) {
        departmentService.updateById(name, id);
        return Result.success("更新部门成功");
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        departmentService.deleteById(id);
        return Result.success("删除部门成功");
    }
}

