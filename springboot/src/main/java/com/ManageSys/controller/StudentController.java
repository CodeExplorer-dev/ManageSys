package com.ManageSys.controller;

import com.ManageSys.common.Result;
import com.ManageSys.entity.Student;
import com.ManageSys.service.StudentService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentService studentService;

    /**
     * 添加学员
     */
    @PostMapping("/add")
    public Result add(@RequestBody Student student) {
        studentService.add(student);
        return Result.success("添加学员成功");
    }

    /**
     * 修改学员信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody Student student) {
        studentService.update(student);
        return Result.success("更新学员成功");
    }

    /**
     * 违纪扣分
     */
    @PutMapping("/point/{id}")
    public Result point(@PathVariable Integer id, @RequestBody Integer point) {
        studentService.point(id, point);
        return Result.success("操作成功");
    }

    /**
     * 删除学员
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        studentService.deleteById(id);
        return Result.success("删除学员成功");
    }

    /**
     * 批量删除学员
     */
    @DeleteMapping("/deleteMore")
    public Result deleteMore(@RequestBody List<Integer> ids) {
        studentService.deleteMore(ids);
        return Result.success("批量删除成功");
    }

    /**
     * 查询所有学员
     */
    @GetMapping("/selectAll")
    public Result selectAll(Student student) {
        List<Student> students = studentService.selectAll(student);
        return Result.success("查询所有学员成功", students);
    }

    /**
     * 根据id查询学员
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Student student = studentService.selectById(id);
        return Result.success("查询成功", student);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Student student,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize) {
        PageInfo<Student> pageInfo = studentService.selectPage(student, pageNum, pageSize);
        return Result.success("查询成功", pageInfo);
    }
}
