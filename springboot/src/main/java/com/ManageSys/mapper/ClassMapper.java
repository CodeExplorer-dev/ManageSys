package com.ManageSys.mapper;

import com.ManageSys.entity.Class;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ClassMapper {
    // 查询所有班级信息
    List<Class> selectAll(Class classes);

    // 根据id查询班级信息
    @Select("select * from `class` where id = #{id}")
    Class selectById(Integer id);

    // 添加班级信息
    void insert(Class classes);

    // 修改班级信息
    void updateById(Class classes);

    // 根据id删除班级信息
    @Delete("delete from `class` where id = #{id}")
    void deleteById(Integer id);
}
