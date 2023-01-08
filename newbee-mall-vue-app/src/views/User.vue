<template>
  <div class="user-box">
    <!--    <s-header :name="'我的'"></s-header>-->
    <!-- 公用头-->
    <TopNavigator>
    </TopNavigator>
    <!--    <div class="user-info">-->
    <!--      <div class="info">-->
    <!--        <img src="../assets/takina.jpg"/>-->
    <!--        <div class="user-desc">-->
    <!--          <span>昵称：{{ user.nickName }}</span>-->
    <!--          <span>登录名：{{ user.loginName }}</span>-->
    <!--          <span class="name">个性签名：{{ user.introduceSign }}</span>-->
    <!--        </div>-->
    <!--      </div>-->
    <!--    </div>-->
    <!--    <ul class="user-list">-->
    <!--      <li @click="goTo('order')">-->
    <!--        <span>我的订单</span>-->
    <!--        <van-icon name="arrow" />-->
    <!--      </li>-->
    <!--      <li @click="goTo('setting')">-->
    <!--        <span>账号管理</span>-->
    <!--        <van-icon name="arrow" />-->
    <!--      </li>-->
    <!--      <li @click="goTo('address?from=mine')">-->
    <!--        <span>地址管理</span>-->
    <!--        <van-icon name="arrow" />-->
    <!--      </li>-->
    <!--      <li @click="goTo('about')">-->
    <!--        <span>关于我们</span>-->
    <!--        <van-icon name="arrow" />-->
    <!--      </li>-->
    <!--    </ul>-->
    <!--    <nav-bar></nav-bar>-->


    <el-container>
      <!--侧边-->
      <el-aside style="height: 220px; padding-left: 100px" width="300px">

          <h3 style="text-align:center">设置</h3>
          <el-menu default-active="1" @open="handleOpen" @close="handleClose">
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
        <el-form :model="myInfoForm" :rules="myInfoRules" label-width="100px">
          <el-form-item label="昵称" prop="nickName">
            <el-input v-model="myInfoForm.nickName" style="width: 200px"></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="sex">
            <template>
              <el-radio v-model="myInfoForm.sex" label="1">男</el-radio>
              <el-radio v-model="myInfoForm.sex" label="2">女</el-radio>
              <el-radio v-model="myInfoForm.sex" label="3">保密</el-radio>
            </template>
          </el-form-item>

        </el-form>
      </el-main>
    </el-container>

  </div>
</template>

<script>
import sHeader from '@/components/SimpleHeader'
import { getUserInfo } from '../service/user'
import TopNavigator from '@/components/TopNavigator'
export default {
  components: {
    // sHeader
    TopNavigator
  },
  data() {
    return {
      user: {},
      myInfoForm: {
        nickname:'',
        sex:'',
        bir: ''
      },
    }
  },
  // 获取用户信息
  async mounted() {
    const { data } = await getUserInfo()
    this.user = data
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    goTo(r) {
      this.$router.push({ path: r })
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
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
