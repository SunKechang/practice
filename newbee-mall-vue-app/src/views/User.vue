<template>
  <div class="user-box">

    <!-- 公用头-->
    <TopNavigator>
    </TopNavigator>

    <el-container>
      <!--侧边-->
      <el-aside style="height: 220px; padding-left: 100px" width="300px">

          <h3 style="text-align:center">设置</h3>
          <el-menu default-active="1" @open="handleOpen" @close="handleClose" @select="handSelect">
            <el-menu-item index="1">
              <span slot="title">个人信息</span>
            </el-menu-item>
            <el-menu-item index="2">
              <span slot="title">账户安全</span>
            </el-menu-item>
            <el-menu-item index="Address" >
              <span slot="title">收货地址</span>
            </el-menu-item>
          </el-menu>

      </el-aside>
      <!--main-->
      <el-main>
        <div v-if="this.index==='1'">
          <el-form :model="myInfoForm" label-width="100px">
            <el-form-item label="账号">
              <span>{{myInfoForm.loginName}}</span>
            </el-form-item>
            <el-form-item label="昵称" prop="nickName">
              <el-input v-model="myInfoForm.nickName" style="width: 200px"></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="sex">
              <template>
                <el-radio v-model="myInfoForm.sex" label="man">男</el-radio>
                <el-radio v-model="myInfoForm.sex" label="woman">女</el-radio>
                <el-radio v-model="myInfoForm.sex" label="none">保密</el-radio>
              </template>
            </el-form-item>
            <el-form-item label="联系电话">
              <el-input v-model="myInfoForm.telephone" style="width: 200px"></el-input>
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="myInfoForm.mail" style="width: 200px"></el-input>
            </el-form-item>
            <el-form-item label="个人介绍">
              <el-input type="textarea" v-model="myInfoForm.introduceSign" style="width: 300px"></el-input>
            </el-form-item>
            <el-form-item >
              <el-button type="primary" @click="changeInfo">提交</el-button>
            </el-form-item>
          </el-form>
        </div>
        <!-- 账号安全 -->
        <div v-else-if="this.index==='2'" style="margin-top: 30px;margin-left: 20px">

          <!-- 密码 -->
          <el-divider content-position="left">密码修改</el-divider>
          <div style="margin-top: 30px;width: 300px">
            <el-form ref="pwdForm" :model="pwdForm" label-width="80px" :rules="changePwdRules" status-icon>
              <el-form-item label="注册电话">
                <el-input v-model="pwdForm.telephone" disabled=""></el-input>
              </el-form-item>
              <el-form-item autocomplete="off">
                <el-button type="primary" @click="getCode">获取验证码</el-button>
              </el-form-item>
              <el-form-item label="验证码" prop="code" autocomplete="off">
                <el-input v-model="pwdForm.code" placeholder="请输入验证码"></el-input>
              </el-form-item>
              <el-form-item label="新密码" prop="newPwd" autocomplete="off" >
                <el-input type="password" v-model="pwdForm.newPwd" placeholder="请输入新密码" show-password></el-input>
              </el-form-item>
              <el-form-item label="确认" prop="newPwd2" autocomplete="off" >
                <el-input type="password" v-model="pwdForm.newPwd2" placeholder="再次输入新密码" show-password></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="changePwd">提交</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>

<!--        <div v-else>-->
<!--          -->
<!--        </div>-->

      </el-main>
    </el-container>

  </div>
</template>

<script>
import {EditUserInfo, getUserInfo, logout} from '../service/user'
import TopNavigator from '@/components/TopNavigator'
import axios from "../utils/axios";
import {setLocal} from "../common/js/utils";
import {getAddressList} from "../service/address";
export default {
  components: {
    TopNavigator
  },
  data() {
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.pwdForm.newPwd2 !== '') {
          this.$refs.pwdForm.validateField('newPwd2');
        }
        callback();
      }
    };
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.pwdForm.newPwd) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      index:'1',
      user:{},
      code:'',
      myInfoForm: {
        nickName: '',
        // 账号
        loginName: '',
        introduceSign: '',
        sex: '',
        telephone:'',
        mail:'',
      },
      pwdForm:{
        telephone:'',
        code:'',
        newPwd:'',
        newPwd2:''
      },
      changePwdRules:{
        // telephone: [
        //   { required: true, message: "请输入手机号", trigger: "blur" },
        //   { pattern:/^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/, message: "请输入合法手机号", trigger: "blur" }
        // ],
        newPwd:[{validator:validatePass,trigger:'blur'}],
        newPwd2:[{validator:validatePass2,trigger:'blur'}]
      },
    }
  },
  // 获取用户信息
  async mounted() {
    const { data } = await getUserInfo()
    this.myInfoForm = data;
    this.pwdForm.telephone=this.myInfoForm.telephone;
  },
  methods: {

    async changeInfo(){
      const params = {
        introduceSign: this.myInfoForm.introduceSign,
        nickName: this.myInfoForm.nickName,
        // passwordMd5: this.$md5(this.password)
        sex: this.myInfoForm.sex,
        telephone: this.myInfoForm.telephone,
        mail: this.myInfoForm.mail
      }
      const { data } = await EditUserInfo(params)
      console.log(data)
      this.$message({
        message: '修改成功!',
        type: 'success'
      });
      location.reload();
    },
    async changePwd(){
      if(this.pwdForm.code!==this.code){
        this.$message({
          message:'验证码错误',
          type: 'error'
        });
      }else{
        let pwd=this.$md5(this.pwdForm.newPwd)
        const { data } =await axios.post('/users/mall/changePwd',pwd);
        // console.log(data);
        if(data==="success"){
          this.$message({
            message:'修改成功,请重新登录!',
            type:'success'
          });
          const { resultCode } = await logout()
          if (resultCode === 200) {
              setLocal('token', '')
            await this.$router.push('/login');
          }
        }else{
          this.$message({
            message:'密码必须为新密码!',
            type:'error'
          });
        }
      }
    },
    async getCode(){
      const {data}=await axios.post('/users/mall/code',this.pwdForm.telephone)
      // console.log(data)
      this.code=data;
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    handSelect(key, keyPath){
      console.log(key,keyPath)
      this.index=key
      if(key==='Address'){
        this.$router.push('/Address');
      }
    }
  }
}
</script>

<style lang="less" scoped>

</style>
