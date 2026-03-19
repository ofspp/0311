<template>
<div>
   <el-form
    class="login-container"
    :model="loginForm"
    :rules="rules"   
    label-position="left"
    label-width="0px"
    v-loading="loading"
  >
    <h3 class="login_title">系统登录</h3>

    <!-- 账号输入 -->
    <el-form-item prop="username">
      <el-input
        v-model="loginForm.username"
        type="text"
        autocomplete="off"
        placeholder="账号"
      ></el-input>
    </el-form-item>

    <!-- 密码输入 -->
    <el-form-item prop="password">
      <el-input
        v-model="loginForm.password"
        type="password"
        autocomplete="off"
        placeholder="密码"
        @keyup.enter="submitClick"
      ></el-input>
    </el-form-item>

    <!-- 记住密码 -->
    <el-checkbox v-model="checked" label-position="left" class="login_remember">
      记住密码
    </el-checkbox>

    <!-- 登录按钮 -->
    <el-form-item style="width: 100%">
      <el-button type="primary" @click.prevent="submitClick" style="width: 100%">
        登录
      </el-button>
    </el-form-item>
  </el-form>
  <el-divider></el-divider>
  <el-descriptions title="登录说明（建议分辨率：1920*1080）" style="margin-left:100px;width:60%" :column="2" border>
    <el-descriptions-item :labelStyle="{'width':'120px'}">
      <template #label>
        <i class="el-icon-user"></i>
        超级用户名
      </template>
      root （<span style="color:#901010">注意：可以查看所有功能，但是不要进行业务操作</span>）
    </el-descriptions-item>
    <el-descriptions-item :labelStyle="{'width':'120px'}">
      <template #label >
        <i class="el-icon-mobile-phone"></i>
        密码
      </template>
      123
    </el-descriptions-item>
    <el-descriptions-item :labelStyle="{'width':'120px'}">
      <template #label>
        <i class="el-icon-user"></i>
        挂号用户名
      </template>
      挂号
    </el-descriptions-item>
    <el-descriptions-item :labelStyle="{'width':'120px'}">
      <template #label >
        <i class="el-icon-mobile-phone"></i>
        密码
      </template>
      123
    </el-descriptions-item>  
    <el-descriptions-item :labelStyle="{'width':'120px'}">
      <template #label>
        <i class="el-icon-user"></i>
        医生用户名
      </template>      
      扁鹊（范无病、华佗、葛洪等） <span style="color:#901010">注意：需要根据登录医生查看患者</span>      
    </el-descriptions-item>
    <el-descriptions-item :labelStyle="{'width':'120px'}">
      <template #label >
        <i class="el-icon-mobile-phone"></i>
        密码
      </template>
      123
    </el-descriptions-item>
    <el-descriptions-item :labelStyle="{'width':'120px'}">
      <template #label>
        <i class="el-icon-user"></i>
        检查用户名
      </template>
      检查
    </el-descriptions-item>
    <el-descriptions-item :labelStyle="{'width':'120px'}">
      <template #label >
        <i class="el-icon-mobile-phone"></i>
        密码
      </template>
      123
    </el-descriptions-item>
        <el-descriptions-item :labelStyle="{'width':'120px'}">
      <template #label>
        <i class="el-icon-user"></i>
        检验用户名
      </template>
      检验
    </el-descriptions-item>
    <el-descriptions-item :labelStyle="{'width':'120px'}">
      <template #label >
        <i class="el-icon-mobile-phone"></i>
        密码
      </template>
      123
    </el-descriptions-item>
        <el-descriptions-item :labelStyle="{'width':'120px'}">
      <template #label>
        <i class="el-icon-user"></i>
        处置用户名
      </template>
      处置
    </el-descriptions-item>
    <el-descriptions-item :labelStyle="{'width':'120px'}">
      <template #label >
        <i class="el-icon-mobile-phone"></i>
        密码
      </template>
      123
    </el-descriptions-item>
  </el-descriptions>
</div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue"
import { useRouter } from "vue-router"
import { ElMessage } from "element-plus"   // 如果用element-plus 消息提示
import request from "../util/util"

const router = useRouter()

const loginForm = reactive({
  username: "",
  password: "",
})

const checked = ref(false) // 是否记住密码

// 记住密码
const rememberPassword = () => {
  let userInfo = localStorage.getItem("remember_password")
  if (userInfo) {
    let obj = JSON.parse(userInfo)
    loginForm.username = obj.username
    loginForm.password = obj.password
    checked.value = true
  }
}

// 登录提交
const submitClick = async () => {
  if (!loginForm.username || !loginForm.password) {
    ElMessage({
      type: "info",
      message: "用户名和密码不能为空!",
    })
    return
  }

  try {
    // 用axios请求，不需要手动拼接 url
    const resp = await request.get("/login", {
      params: {
        username: loginForm.username,
        password: loginForm.password,
      },
    })
    let userInfo = resp.data    
    if (!userInfo || userInfo.length === 0) {
      ElMessage({
        message: "用户名或密码不存在或错误!",
        type: "error",
      })
    } else {
      sessionStorage.setItem("Flag", "isLogin")
      sessionStorage.setItem("loginUser", JSON.stringify(userInfo[0]))
      ElMessage("欢迎 " + userInfo[0].realname + " 登录成功!")

      if (checked.value) {
        localStorage.setItem("remember_password", JSON.stringify(loginForm))
      } else {
        localStorage.removeItem("remember_password")
      }

      router.replace({ path: "/home" })
    }
  } catch (e) {
    console.error("请求出错，详细信息：", e) // 打印错误对象
    ElMessage({
      type: "error",
      message: "请求出错，请检查后端接口",
    })
  }
}

onMounted(() => {
  rememberPassword()
})
</script>


<style scope>
.login-container {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 100px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.login_title {
  margin: 0px auto 40px auto;
  text-align: center;
  color: #505458;
}

.login_remember {
  margin: 0px 0px 35px 0px;
  text-align: left;
}
</style>
