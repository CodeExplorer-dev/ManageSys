<template>
  <el-aside>
    <el-menu
        default-active="1"
        class="aside_menu"
        router
        active-text-color="red"
        text-color="#000"
    >
      <el-sub-menu index="1">
        <template #title>
          <span class="title">班级学员管理</span>
        </template>
        <el-menu-item index="/class" class="sub_title">班级管理</el-menu-item>
        <el-menu-item index="/student" class="sub_title">学员管理</el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="2">
        <template #title>
          <span class="title">系统信息管理</span>
        </template>
        <el-menu-item index="/department" class="sub_title">部门管理</el-menu-item>
        <el-menu-item index="/employee" class="sub_title">员工管理</el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="3">
        <template #title>
          <span class="title">数据统计管理</span>
        </template>
        <el-menu-item index="/profile" class="sub_title">员工信息统计</el-menu-item>
      </el-sub-menu>
    </el-menu>
  </el-aside>
</template>

<script setup>

</script>

<style scoped>
.aside_menu{
  width: 16rem;
  background-color: #f7f7f7;

  .title{
    font-size: 18px;
    font-weight: bold;
    &:hover{
      color: red;
    }
  }

  .sub_title{
    font-size: 16px;
    &:hover{
      color: red;
    }
  }
}
</style>