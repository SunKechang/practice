

<template>
   <div class="product_list">
     <TopNavigator>
     </TopNavigator>
     <div class="search_and_cart">
       <div class="wide">
         <div class="logo" style="float: left"><img style="width: 100%" src="https://img10.360buyimg.com/img/jfs/t1/50166/9/22920/11310/63a1993cE3800216e/105f3d456f0905bb.jpg"></div>
         <el-input v-model="input" style="width: 546px;margin-top: 48px;margin-left: 82px;" placeholder="请输入内容"  class="input-with-select">
           <el-button @click="searchGoods" style="background-color: #f58c0c;color: white;" slot="append"  icon="el-icon-search">搜索</el-button>
         </el-input>
         <el-button @click="goTo" style="background-color: #ED6C3E;color: white;" slot="append"  icon="el-icon-goods">购物车</el-button>
         <div class="logo" style="float: right"><img style="width: 100%" src="https://img13.360buyimg.com/babel/jfs/t1/192532/18/31574/14301/63aeb38dF5c55c212/38245c8dbdee113c.jpg.avif"></div>
       </div>
     </div>
     <div class="contents">
       <div class="wide">
         <div class="content">
           <div class="wide" style="text-align:center;height: 731px;background-color:#fff;">
             <el-tabs stretch="true" v-model="activeName" @tab-click="changeTab">
               <el-tab-pane default-value label="推荐" name=""></el-tab-pane>
               <el-tab-pane label="新品" name="new"></el-tab-pane>
               <el-tab-pane label="价格" name="price"></el-tab-pane>
             </el-tabs>
             <div class="content" v-for="(item, index) in productList" :key="index" @click="goToDetail(item)">
               <el-image style="padding-top: 5px;height: 185px;width:217px" fit="fill" :src="prefix(item.goodsCoverImg)"></el-image>
               <div class="content_name" ><h3>{{item.goodsName}}</h3></div>
               <div class="content_describe"><h3>{{item.goodsIntro}}</h3></div>
               <div class="content_price"><span class="price_span">¥{{item.sellingPrice}}</span></div>
             </div>
             <div style="clear: both"></div>
             <el-pagination
               hide-on-single-page
               style="float: right"
               background
               layout="prev, pager, next"
               :page-count="totalPage" @current-change="change" :current-page="page">
             </el-pagination>
           </div>
         </div>
       </div>
     </div>
   </div>
</template>

<script>
import { getQueryString } from '@/common/js/utils'
import { search } from '../service/good'
import { Toast } from 'vant'
import TopNavigator from "@/components/TopNavigator";
export default {
  components: { TopNavigator },
  data() {
    return {
      input:"",
      activeName:'',
      keyword: this.$route.query.keyword || '',
      searchBtn: false,
      seclectActive: false,
      refreshing: false,
      list: [],
      loading: false,
      finished: false,
      productList: [],
      totalPage: 0,
      page: 1,
      orderBy: ''
    }
  },
  mounted() {
    window.addEventListener('scroll', this.pageScroll)
    setTimeout(() => {
        this.isLoading = false
    }, 500)
    this.init()
  },
  methods: {
    async init() {
      const { categoryId, from } = this.$route.query
      if (!categoryId && !this.keyword) {
        // Toast.fail('请输入关键词')
        this.finished = true
        this.loading = false;
        return
      }
      console.log(this.page)
      const { data, data: { list } } = await search({ pageNumber: this.page, goodsCategoryId: categoryId, keyword: this.keyword, orderBy: this.orderBy })
      this.productList = list
      this.totalPage = data.totalPage
      this.loading = false;
      if (this.page >= data.totalPage) this.finished = true
    },
    goBack() {
      this.$router.go(-1)
    },
    goToDetail(item) {
      this.$router.push({ path: `product`,query:{id:item.goodsId} })
    },
    goTo()
    {
      this.$router.push({ path: '/cart' })
    },
    searchGoods() {
      this.keyword=this.input;
      this.init();
    },
    pageScroll() {
      let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
      scrollTop > 50 ? this.seclectActive = true : this.seclectActive = false
    },
    change(page){
      this.page=page
        this.init()
    },
    onLoad() {
      if (!this.refreshing && this.page < this.totalPage) {
        this.page = this.page + 1
      }
      if (this.refreshing) {
        this.productList = [];
        this.refreshing = false;
      }
      this.init()
    },
    onRefresh() {
      this.refreshing = true
      this.finished = false
      this.loading = true
      this.page = 1
      this.onLoad()
    },
    changeTab() {
      this.orderBy = this.activeName
      this.onRefresh()
    }
  }
}
</script>

<style lang="less" scoped>
body{
  min-width: 1190px;
}
.product_list{
  overflow: scroll;
}
.product_list{
  background-color: #f4f4f4;
}
.wide{
  margin: auto;
  width: 1190px;
}
.search_and_cart{
  background: #fff;
  border-bottom: 1px solid #ddd;
  margin-bottom: 10px;
}
.search_and_cart .wide{
  position: relative;
  z-index: 11;
  height: 140px;
}
.logo{
  overflow: hidden;
  display: flex;
  align-items: center;
  margin: 10px 0px 0px 0px;
  width: 190px;
  height: 120px;
}
.contents{
  background-color: #f4f4f4;

}
.contents .wide:after{
  position: relative;
  z-index: 11;
  padding-top: 10px;
  content: ".";
  display: block;
  clear: both;
  visibility: hidden;
}
.content{
  float: left;
  height: 310px;
  width: 217px;
  margin-right: 10px;
  margin-left: 10px;
  margin-bottom: 10px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}
.content_name{
  width: 160px;margin-left: auto;margin-right: auto;margin-top: 13px;
}
.content_name h3{
  font-size: 15px;font-weight: 400;color: #333;-webkit-transition: color .2s ease;transition: color .2s ease;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;
}
.content_describe{
  width: 188px;margin-left: auto;margin-right: auto;margin-top: 13px;
}
.content_describe h3{
  font-size: 12px;font-weight: 400;color: #333;-webkit-transition: color .2s ease;transition: color .2s ease;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;
}
.content_price{
  position: relative;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  line-height: 24px;
  overflow: hidden;
  text-align: center;
  width: 160px;
  height: 24px;
  margin-left: auto;
  margin-right: auto;
  margin-top: 7px;
}
.price_span{
  height: 100%;
  width: 66px;
  background: #fff;
  text-align: center;
  color: #e1251b;
  font-size: 14px;
  font-weight: 700;
  line-height: 22px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  vertical-align: top;
}
</style>
