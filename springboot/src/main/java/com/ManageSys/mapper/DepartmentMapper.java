package com.ManageSys.mapper;

import com.ManageSys.entity.Class;
import com.ManageSys.entity.Department;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


import java.util.List;

public interface DepartmentMapper {
    // 查询
    List<Department> selectAll(Department department);

    // 添加
    void insert(String name);

    // 修改
    void updateById(String name, Integer id);

    // 删除
    void deleteById(Integer id);

}
