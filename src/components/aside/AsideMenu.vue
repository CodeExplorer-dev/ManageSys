<template>
  <el-aside>
    <el-menu
        :default-active="activeMenu" 
        class="aside_menu"
        router
        active-text-color="red"
        text-color="#000"
    >
      <el-sub-menu index="1">
        <template #title>
          <span class="title">班级学员管理</span>
        </template>
        <el-menu-item index="/manageSys/class" class="sub_title">班级管理</el-menu-item>
        <el-menu-item index="/manageSys/student" class="sub_title">学员管理</el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="2">
        <template #title>
          <span class="title">系统信息管理</span>
        </template>
        <el-menu-item index="/manageSys/department" class="sub_title">部门管理</el-menu-item>
        <el-menu-item index="/manageSys/employee" class="sub_title">员工管理</el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="3">
        <template #title>
          <span class="title">数据统计管理</span>
        </template>
        <el-menu-item index="/manageSys/profile" class="sub_title">员工信息统计</el-menu-item>
      </el-sub-menu>
    </el-menu>
  </el-aside>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const activeMenu = computed(() => {
  // 如果当前路径是 /manageSys，默认高亮班级管理
  return route.path === '/manageSys' ? '/manageSys/department' : route.path
})
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