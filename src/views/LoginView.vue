<template>
  <div class="main">
    <div class="image">
    </div>
    <div class="login_container">
      <div class="login_box">
        <h1 class="title">培训管理系统</h1>
        <el-form :model="data.form" ref="formRef" :rules="data.rules" label-width="65px" status-icon>
          <el-form-item label="用户名" prop="username">
            <el-input
                v-model="data.form.username"
                placeholder="请输入员工用户名"
                :prefix-icon="User"
                size="large"
                style="height: 50px; line-height: 50px;"
            />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
                v-model="data.form.password"
                type="password"
                show-password
                placeholder="请输入密码"
                :prefix-icon="Lock"
                size="large"
                style="height: 50px; line-height: 50px;"
            />
          </el-form-item>
        </el-form>
        <div class="btn">
          <el-button type="primary" @click="login">登录</el-button>
          <el-button type="info" >取消</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import {User, Lock} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  form: {
    username: '',
    password: ''
  },
  rules: {
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ]
  }
})

const formRef = ref()

const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('login', data.form).then(res => {
        console.log(res)
        if (res.code === "200") {
          localStorage.setItem('token', res.token)
          console.log(res)
          ElMessage.success(res.msg)
          setTimeout(() => {
            router.push('/manageSys/department')
          }, 500)

        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  });
}
</script>

<style scoped>
.main{
  display: flex;
  width: 100%;
  height: 100%;

  .image {
    width: 60rem;
    height: 58rem;
    overflow: hidden;
    background-image: url(@/assets/images/login.png);
    background-repeat: no-repeat;
    background-size: cover;
  }

  .login_container {
    margin: auto 0;

    .login_box {
      width: 35rem;
      height: 23rem;
      border: 1px solid #000;
      padding: 35px;

      .title {
        font-size: 38px;
        font-weight: normal;
        margin-bottom: 25px;
      }

      .btn {
        display: flex;
        justify-content: space-evenly;
        margin-top: 30px;

        .el-button {
          width: 120px;
          height: 40px;

        }
      }
    }

  }
}

.el-form-item {
  margin-bottom: 25px; /* 调节垂直间距 */
}

.el-form-item {
  display: grid;
  grid-template-columns: 55px 1fr; /* label宽度 + 输入框自适应 */
  align-items: center; /* 垂直居中 */
}

</style>