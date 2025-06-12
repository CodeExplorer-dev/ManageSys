package com.ManageSys.mapper;

import com.ManageSys.entity.Student;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper {
    // 查询所有学员信息
    List<Student> selectAll(Student student);

    // 根据id查询学员信息
    @Select("select * from `student` where id = #{id}")
    Student selectById(Integer id);

    // 添加学员信息
    void insert(Student student);

    // 修改学员信息
    void updateById(Student student);

    // 根据id修改学员违纪扣分
    void point(Integer id, Integer point);

    // 根据id删除学员
    @Select("delete from `student` where id = #{id}")
    void deleteById(Integer id);


}
