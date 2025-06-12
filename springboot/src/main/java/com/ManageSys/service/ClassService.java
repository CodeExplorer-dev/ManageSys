package com.ManageSys.service;

import com.ManageSys.entity.Class;
import com.ManageSys.mapper.ClassMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {
    @Resource
    private ClassMapper classMapper;

    // 添加班级
    public void add(Class classes) {
        classMapper.insert(classes);
    }

    // 修改班级
    public void update(Class classes) {
        classMapper.updateById(classes);
    }

    // 删除班级
    public void deleteById(Integer id) {
        classMapper.deleteById(id);
    }

    // 查询全部班级
    public List<Class> selectAll(Class classes) {
        return classMapper.selectAll(classes);
    }

    // 根据id查询班级
    public Class selectById(Integer id) {
        return classMapper.selectById(id);
    }

    public PageInfo<Class> selectPage(Class classes, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Class> list = classMapper.selectAll(classes);
        return PageInfo.of(list);
    }
}
