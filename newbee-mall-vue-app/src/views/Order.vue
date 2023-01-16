

<template>
  <div class="order-box">
    <div style="width: 100%;">
      <TopNavigator>
      </TopNavigator>
    </div>
    <!-- <s-header :name="'我的订单'" :back="'/user'"></s-header> -->
    <el-select @change="onChangeTabElement" v-model="cateTab" placeholder="请选择">
      <el-option value="" label="全部">全部</el-option>
      <el-option value="0" label="待付款">待付款</el-option>
      <el-option value="1" label="待确认">待确认</el-option>
      <el-option value="2" label="待发货">待发货</el-option>
      <el-option value="3" label="已发货">已发货</el-option>
      <el-option value="4" label="交易完成">交易完成</el-option>
    </el-select>
    <el-table
      :data="list"
      style="width: 100%">
      <el-table-column
        prop="newBeeMallOrderItemVOS[0].goodsName"
        label="商品">
      </el-table-column>
      <el-table-column
        prop="total"
        label="商品数量"
        width="180">
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="购买时间"
        width="360">
      </el-table-column>
      <el-table-column
        label="操作"
        width="360">
        <template slot-scope="scope">
          <el-button
            @click.native.prevent="goTo(scope.row.orderNo)"
            type="text"
            size="small">
            详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="5"
      layout="prev, pager, next"
      :total="totalCount">
    </el-pagination>
  </div>
</template>

<script>
import sHeader from '@/components/SimpleHeader'
import { getOrderList } from '../service/order'
import { prefix } from '@/common/js/utils'
import TopNavigator from '../components/TopNavigator.vue'

export default {
  data() {
    return {
      status: '',
      loading: false,
      finished: false,
      refreshing: false,
      list: [],
      page: 1,
      cateTab: '',
      totalCount: 0,
      currentPage: 1,
    }
  },
  components: {
    // sHeader,
    TopNavigator
  },
  async mounted() {
    // this.loadData()
  },
  created() {
    this.onLoad()
  },
  methods: {
    async loadData() {
      const { data, data: { list } } = await getOrderList({ pageNumber: this.page, status: this.status })
      this.list = list
      for(let i=0;i<this.list.length;i++) {
        this.list[i].goods = this.list[i].newBeeMallOrderItemVOS
        let total = 0
        for(let j=0;j<this.list[i].goods.length;j++) {
          total += this.list[i].goods[j].goodsCount
        }
        this.list[i].total = total
      }
      this.totalCount = data.totalCount
      console.log(this.totalPage)
      this.loading = false;
      if (this.page >= data.totalPage) this.finished = true
    },
    onChangeTabElement(key) {
      console.log(key)
      this.status = key
      this.onRefresh()
    },
    goTo(id) {
      this.$router.push({ path: `order-detail?id=${id}` })
    },
    goBack() {
      this.$router.go(-1)
    },
    onLoad() {
      if (!this.refreshing && this.page < this.totalPage) {
        this.page = this.page + 1
      }
      if (this.refreshing) {
        this.list = [];
        this.refreshing = false;
      }
      this.loadData()
    },
    onRefresh() {
      this.refreshing = true
      this.finished = false
      this.loading = true
      this.page = 1
      this.onLoad()
    },
    handleCurrentChange(pageNum) {
      this.page = pageNum
      this.loadData()
    }
  }
}
</script>

<style lang="less" scoped>
  .order-box {
    width: 100%;
  }
</style>
