package com.ManageSys.controller;

import com.ManageSys.common.Result;
import com.ManageSys.entity.Class;
import com.ManageSys.service.ClassService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/class")
public class ClassController {

    @Resource
    private ClassService classService;

    /**
     * 添加班级
     */
    @PostMapping("/add")
    public Result add(@RequestBody Class classes) {
        classService.add(classes);
        return Result.success("添加班级成功");
    }

    /**
     * 更新班级
     */
    @PutMapping("/update")
    public Result update(@RequestBody Class classes) {
        classService.update(classes);
        return Result.success("更新班级成功");
    }

    /**
     * 删除班级
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        classService.deleteById(id);
        return Result.success("删除班级成功");
    }

    /**
     * 查询所有班级
     */
    @GetMapping("/selectAll")
    public Result selectAll(Class classes) {
        List<Class> list = classService.selectAll(classes);
        return Result.success("查询所有班级成功", list);
    }

    /**
     * 根据id查询班级
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Class classes = classService.selectById(id);
        return Result.success("查询成功", classes);
    }

    /**
     * 分页查询班级
     */
    @GetMapping("/selectPage")
    public Result selectPage(Class classes,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize) {
        PageInfo<Class> pageInfo = classService.selectPage(classes, pageNum, pageSize);
        return Result.success("分页查询成功", pageInfo);
    }

}
