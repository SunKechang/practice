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
            <el-menu-item index="3">
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
            <el-form ref="pwdRef" :model="pwdForm" label-width="80px">
              <el-form-item label="旧密码" prop="oldPwd">
                <el-input v-model="pwdForm.oldPwd" placeholder="请输入旧密码" show-password=true></el-input>
              </el-form-item>
              <el-form-item label="注册电话" prop="telephone">
                <el-input v-model="pwdForm.telephone" placeholder="请输入注册电话"></el-input>
              </el-form-item>
              <el-form-item >
                <el-button type="primary" @click="getCode">获取验证码</el-button>
              </el-form-item>
              <el-form-item label="验证码" prop="code">
                <el-input v-model="pwdForm.code" placeholder="请输入验证码"></el-input>
              </el-form-item>
              <el-form-item label="新密码" prop="newPwd">
                <el-input v-model="pwdForm.newPwd" placeholder="请输入新密码" show-password=true></el-input>
              </el-form-item>
              <el-form-item label="确认" prop="newPwd2">
                <el-input v-model="pwdForm.newPwd2" placeholder="再次输入新密码" show-password=true></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="changePwd">提交</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>

      </el-main>
    </el-container>

  </div>
</template>

<script>
import {EditUserInfo, getUserInfo} from '../service/user'
import TopNavigator from '@/components/TopNavigator'
import axios from "../utils/axios";
export default {
  components: {
    TopNavigator
  },
  data() {
    return {
      index:'1',
      user:{},
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
        oldPwd:'',
        telephone:'',
        code:'',
        newPwd:'',
        newPwd2:''
      }
    }
  },
  // 获取用户信息
  async mounted() {
    const { data } = await getUserInfo()
    this.myInfoForm = data;
    // console.log(data)
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
    changePwd(){

    },
    getCode(){
      const {data1}=axios.post('/users/mall/code',this.pwdForm.telephone);
      console.log(data1);
    },
    // goTo(r) {
    //   this.$router.push({ path: r })
    // },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    handSelect(key, keyPath){
      console.log(key,keyPath)
      this.index=key
    }
  }
}
</script>

<style lang="less" scoped>
@import '../common/style/mixin';



.user-box {
  .user-info {
    width: 94%;
    margin: 10px;
    height: 115px;
    background: linear-gradient(90deg, @primary, #51c7c7);
    box-shadow: 0 2px 5px #269090;
    border-radius: 6px;
    margin-top: 50px;

    .info {
      position: relative;
      display: flex;
      width: 100%;
      height: 100%;
      padding: 25px 20px;
      .boxSizing();

      img {
        .wh(60px, 60px);
        border-radius: 50%;
        margin-top: 4px;
      }

      .user-desc {
        display: flex;
        flex-direction: column;
        margin-left: 10px;
        line-height: 20px;
        font-size: 14px;
        color: #fff;

        span {
          color: #fff;
          font-size: 14px;
          padding: 2px 0;
        }
      }

      .account-setting {
        position: absolute;
        top: 10px;
        right: 20px;
        font-size: 13px;
        color: #fff;

        .van-icon-setting-o {
          font-size: 16px;
          vertical-align: -3px;
          margin-right: 4px;
        }
      }
    }
  }

  .user-list {
    padding: 0 20px;
    margin-top: 20px;

    li {
      height: 40px;
      line-height: 40px;
      border-bottom: 1px solid #e9e9e9;
      display: flex;
      justify-content: space-between;
      font-size: 14px;

      .van-icon-arrow {
        margin-top: 13px;
      }
    }
  }
}
</style>
