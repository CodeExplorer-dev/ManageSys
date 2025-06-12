package com.ManageSys.mapper;

import com.ManageSys.entity.Employee;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmployeeMapper {
    // 查询全部
    List<Employee> selectAll(Employee employee);

    // 根据id查询
    @Select("select * from `employee` where id = #{id}")
    Employee selectById(Integer id);

    // 添加
    void insert(Employee employee);

    // 修改
    void updateById(Employee employee);

    // 修改密码
    @Select("update `employee` set password = #{newPassword} where id = #{id}")
    void updatePassword(Integer id, String newPassword);

    // 删除
    @Delete("delete from `employee` where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from `employee` where username = #{username}")
    Employee selectByUsername(String username);
}
