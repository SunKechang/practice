<template>
  <div class="top_container">
    <div class="short_cut">
      <div class="wide">
        <el-menu style="float: right;" active-text-color="#f58c0c" background-color="#e3e4e5" :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <el-menu-item  index="1">首页</el-menu-item>
          <el-menu-item  index="2">处理中心</el-menu-item>
          <el-submenu index="3">
            <template slot="title">我的工作台</template>
            <el-menu-item index="2-1">选项1</el-menu-item>
            <el-menu-item index="2-2">选项2</el-menu-item>
            <el-menu-item index="2-3">选项3</el-menu-item>
            <el-submenu index="2-4">
              <template slot="title">选项4</template>
              <el-menu-item index="2-4-1">选项1</el-menu-item>
              <el-menu-item index="2-4-2">选项2</el-menu-item>
              <el-menu-item index="2-4-3">选项3</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-menu-item  index="4" disabled>消息中心</el-menu-item>
          <el-menu-item  index="5"><a href="https://www.ele.me" target="_blank">订单管理</a></el-menu-item>
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
