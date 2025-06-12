package com.ManageSys.service;

import com.ManageSys.entity.Student;
import com.ManageSys.mapper.StudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    @Resource
    private StudentMapper studentMapper;

    // 添加学员
    public void add(Student student) {
        studentMapper.insert(student);
    }

    // 修改学员
    public void update(Student student) {
        studentMapper.updateById(student);
    }

    // 违纪扣分
    public void point(Integer id, Integer point) {
        studentMapper.point(id, point);
    }

    // 删除学员
    public void deleteById(Integer id) {
        studentMapper.deleteById(id);
    }

    // 批量删除
    public void deleteMore(List<Integer> ids) {
        for(Integer id : ids){
            this.deleteById(id);
        }
    }

    // 查询全部学员
    public List<Student> selectAll(Student student) {
        return studentMapper.selectAll(student);
    }

    // 根据id查询学员
    public Student selectById(Integer id) {
        return studentMapper.selectById(id);
    }

    public PageInfo<Student> selectPage(Student student, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Student> list = studentMapper.selectAll(student);
        return PageInfo.of(list);
    }



}
