<template>
  <div class="login">
    <!--    <s-header :name="ruleForm.type === 'login' ? '登录' : '注册'" :back="'/home'"></s-header>-->
    <div v-if="type === 'login'" class="login-body login">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="ruleForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="text" @click="toRegister()">注册</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">登录</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div v-else class="login-body register">
      <el-form :model="regForm" :rules="rules" ref="regRef" label-width="100px" class="demo-ruleForm">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="regForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="regForm.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="text" @click="toLogin()">已有登录账号</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { login, register, getUserInfo } from '../service/user'
import { setLocal, getLocal } from '@/common/js/utils'
export default {
  data() {
    return {
      type: 'login',
      ruleForm: {
        username: '',
        password: '',
        username1: '',
        password1: '',
        verify: false
      },
      regForm:{
        username: '',
        password: '',
      },
      rules:
        {
          username: [
            { required:true,message:'请输入用户名',trigger: 'blur'}
          ],
          password: [
            { required:true,message:'请输入密码', trigger: 'blur'}
          ]
        }
    }
  },

  methods: {
    toLogin() {
      this.type = 'login'
    },
    toRegister(){
      this.type='register'
    },
    async onSubmit(values) {
      if (this.type === 'login') {
        const { data, resultCode } = await login({
          "loginName": this.ruleForm.username,
          "passwordMd5": this.$md5(this.ruleForm.password)
        })
        setLocal('token', data)
        window.location.href = '/'
      } else {
        const { data } = await register({
          "loginName": this.regForm.username,
          "password": this.regForm.password
        })
        this.$message({
          message: '注册成功',
          type: 'success'
        });
        this.type = 'login'
      }
    },
    success(obj) {
      this.verify = true
      obj.refresh()
    },
    error(obj) {
      this.verify = false
      obj.refresh()
    }
  },
}
</script>

<style lang="less">
.login {
  margin: auto;
  width: 400px;
  height: 200px;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  .login {
    .link-register {
      font-size: 14px;
      margin-bottom: 20px;
      color: #1989fa;
      display: inline-block;
    }
  }
  .register {
    .link-login {
      font-size: 14px;
      margin-bottom: 20px;
      color: #1989fa;
      display: inline-block;
    }
  }
}
</style>
