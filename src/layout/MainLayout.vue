<template>
  <div class="header">
    <h1 class="title">培训管理系统</h1>
    <div class="btn">
      <el-button :icon="EditPen" @click="editPassword">修改密码</el-button>
      <el-button :icon="SwitchButton" @click="logout">退出登录</el-button>
    </div>
  </div>
  <div class="container">
    <div class="menu">
      <AsideMenu />
    </div>
    <div>
      <RouterView />
    </div>
  </div>

  <el-dialog
      v-model="editPasswordVisible"
      width="500"
      align-center
  >
    <TopBar title="修改密码" />
    <el-form
        :model="form"
        :rules="rules"
        status-icon
        class="form_container"
        label-width="80px"
    >
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" show-password placeholder="请输入密码"/>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" show-password placeholder="请确认密码"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="editPasswordVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { RouterView, RouterLink } from "vue-router";
import { EditPen, SwitchButton } from "@element-plus/icons-vue"
import AsideMenu from "@/components/aside/AsideMenu.vue";
import TopBar from "@/components/topBar/TopBar.vue";
import {ref, reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";

const editPasswordVisible = ref(false)

const editPassword = () => {
  editPasswordVisible.value = true
}

const form = reactive({
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'));
  } else if (value !== form.newPassword) {
    callback(new Error('两次输入密码不一致!'));
  } else {
    callback();
  }
}

const rules = {
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: 'blur' }],
}

const submitForm = () => {
  console.log(form);
  const token = localStorage.getItem('token');
  console.log(token);
  request.put("employee/updatePassword", {// 请求体参数
    newPassword: form.newPassword  // 请求体参数
  },{
    headers: {
      'Authorization': `${token}`, // 设置Authorization请求头
      'Content-Type': 'application/json'  // 设置Content-Type（axios默认会设置，显式声明更清晰）
    }
  }).then(res => {
    console.log(res);
    if (res.code === '200') {
      form.newPassword = ''
      form.confirmPassword = ''
      editPasswordVisible.value = false
      ElMessage.success(res.msg)
    } else {
      editPasswordVisible.value = false
      ElMessage.error("修改失败")
    }
  })
}

const router = useRouter()

const logout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}

</script>

<style scoped>
.header{
  width: 100%;
  height: 50px;
  background-color: #474c5b;
  display: flex;
  justify-content: space-between;
  align-items: center; /* 垂直居中 */

  .title{
    letter-spacing: 2px;
    margin-left: 20px;
    color: #fff;
  }

  .btn{
    margin-right: 10px;

    button{
      height: 100%;
      font-size: 20px;
    }
  }
}

.container{
  display: flex;

  .menu{
    width: 16rem;
    height: 55rem;
    background-color: #f7f7f7;

  }
}

.form_container{
  margin-top: 1.5rem;
  margin-left: 2rem;
}
</style>

