<template>
  <div class="login">
    <div v-if="type === 'login'" class="login-body login">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="ruleForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="ruleForm.password" autocomplete="off"  show-password></el-input>
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
        <el-form-item label="手机号" prop="username">
          <el-input v-model="regForm.username"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <el-input v-model="regForm.code"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="regForm.password" autocomplete="off" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="text" @click="toLogin()">已有登录账号</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button type="primary" @click="getCode">获取验证码</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { login, register, getUserInfo } from '../service/user'
import { setLocal, getLocal } from '@/common/js/utils'
import axios from "../utils/axios";
export default {
  data() {
    return {
      type: 'login',
      code:'',
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
        code:'',
      },
      rules:
        {
          username: [
            { required:true,message:'请输入手机号',trigger: 'blur'},
            { pattern:/^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/, message: "请输入合法手机号", trigger: "blur" }
          ],
          code:[{
            required:true,message:'请输入验证码', trigger: 'blur'
          }],
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
        // token
        console.log(data)
        if(resultCode==='500'){
          this.$message({
            message: '账号或密码错误,请重新输入',
            type: 'error'
          });
        }
        else{
          setLocal('token', data)
          await this.$router.push('/hom')
          // window.location.href = '/'
        }
      } else {
        if(this.code===this.regForm.code){
          const { data } = await register({
            "loginName": this.regForm.username,
            "password": this.regForm.password
          })
          this.$message({
            message: '注册成功',
            type: 'success'
          });
          this.type = 'login'
          // console.log(data)
        }else{
          this.$message({
            message: '验证码输入错误',
            type: 'error'
          });
        }
      }
    },
    async getCode(){
      let phone=this.regForm.username
      const {data}=await axios.post('/users/mall/code',phone)
      this.$message({
        message: '验证码已发送',
        type: 'success'
      });
      this.code=data;
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
