<template>
  <div class="top_container">
    <div class="short_cut">
      <div class="wide">
        <el-menu style="float: right;" router="true" active-text-color="#f58c0c" background-color="#e3e4e5"  class="el-menu-demo" mode="horizontal" >
          <el-menu-item  index="hom">首页</el-menu-item>
          <el-menu-item  index="user">我的</el-menu-item>
          <el-menu-item  index="cart">购物车</el-menu-item>
          <el-submenu index="3">
            <template slot="title" index="order">订单管理</template>
            <el-menu-item index="order">全部</el-menu-item>
            <el-menu-item index="2-2">待付款</el-menu-item>
            <el-menu-item index="2-3">待发货</el-menu-item>
            <el-menu-item index="2-3">已完成</el-menu-item>
          </el-submenu>
        </el-menu>
      </div>
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'TopNavigator'
}
</script>

<style scoped>
.top_container{
  width: auto;
}
.short_cut{
  border-bottom: 1px solid #ddd;
  background-color: #e3e4e5;
}
.short_cut .wide{
  height: 30px;
  line-height: 30px;
  color: #999;
}
.short_cut .wide .el-menu{
  height: 30px;
  line-height: 30px;
}
.short_cut .wide .el-menu .el-menu-item {
  height: 30px;
  line-height: 30px;
}
.short_cut .wide .el-menu .el-submenu{
  height: 30px;
  line-height: 30px;
}
.short_cut .wide .el-menu .el-submenu /deep/ .el-submenu__title {
  height: 30px;
  line-height: 30px;
}
.navigator_li{
  height: 30px;
}
.wide{
  margin: auto;
  width: 1190px;
}
</style>
