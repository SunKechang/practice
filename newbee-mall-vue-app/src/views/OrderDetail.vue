

<template>
  <div class="order-detail-box">
    <s-header :name="'订单详情'" @callback="close"></s-header>
    <el-descriptions title="订单信息" style="margin-top: 100px">
      <el-descriptions-item label="订单状态">{{ detail.orderStatusString }}</el-descriptions-item>
      <el-descriptions-item label="订单编号">{{ detail.orderNo }}</el-descriptions-item>
      <el-descriptions-item label="下单时间">{{ detail.createTime }}</el-descriptions-item>
      <el-descriptions-item label="订单金额">¥ {{ detail.totalPrice }}</el-descriptions-item>
    </el-descriptions>
    <div class="order-status">
      <el-button type="info" v-if="[1,2,3].includes(detail.orderStatus)" style="margin-bottom: 10px" color="#1baeae" block @click="handleConfirmOrder(detail.orderNo)">确认收货</el-button>
      <el-button type="info" v-if="detail.orderStatus == 0" style="margin-bottom: 10px" color="#1baeae" block @click="showPayFn">去支付</el-button>
      <el-button v-if="!(detail.orderStatus < 0 || detail.orderStatus == 4)" block @click="cancelOrder(detail.orderNo)" type="danger">取消订单</el-button>
    </div>
    <el-table
      :data="detail.newBeeMallOrderItemVOS"
      style="width: 100%">
      <el-table-column
        label="缩略图">
        <template slot-scope="scope">
          <div>
            <el-image
              style="width: 100px; height: 100px"
              :src="prefix(scope.row.goodsCoverImg)"
              :fit="fit"></el-image>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="goodsName"
        label="商品">
      </el-table-column>
      <el-table-column
        prop="goodsCount"
        label="商品数量"
        width="180">
      </el-table-column>
    </el-table>
    <!-- <van-card
      v-for="item in detail.newBeeMallOrderItemVOS"
      :key="item.goodsId"
      style="background: #fff"
      :num="item.goodsCount"
      :price="item.sellingPrice"
      desc="全场包邮"
      :title="item.goodsName"
      :thumb="prefix(item.goodsCoverImg)"
    /> -->
    <van-popup
      v-model="showPay"
      position="bottom"
      :style="{ height: '24%' }"
    >
      <div :style="{ width: '90%', margin: '0 auto', padding: '20px 0' }">
        <van-button :style="{ marginBottom: '10px' }" color="#1989fa" block @click="payOrder(detail.orderNo, 1)">支付宝支付</van-button>
        <van-button color="#4fc08d" block @click="payOrder(detail.orderNo, 2)">微信支付</van-button>
      </div>
    </van-popup>
  </div>
</template>

<script>
import sHeader from '@/components/SimpleHeader'
import { getOrderDetail, cancelOrder, confirmOrder, payOrder } from '../service/order'
import { Dialog, Toast } from 'vant'
export default {
  components: {
    sHeader
  },
  data() {
    return {
      detail: {},
      showPay: false
    }
  },
  mounted() {
    this.init()

  },
  methods: {
    async init() {
      Toast.loading({
        message: '加载中...',
        forbidClick: true
      });
      const { id } = this.$route.query
      const { data } = await getOrderDetail(id)
      this.detail = data
      Toast.clear()
    },
    cancelOrder(id) {
      Dialog.confirm({
        title: '确认取消订单？',
      }).then(() => {
        cancelOrder(id).then(res => {
          if (res.resultCode == 200) {
            Toast('删除成功')
            this.init()
          }
        })
      }).catch(() => {
        // on cancel
      });
    },
    handleConfirmOrder(id) {
      Dialog.confirm({
        title: '是否确认收货？',
      }).then(() => {
        confirmOrder(id).then(res => {
          if (res.resultCode == 200) {
            Toast('确认成功')
            this.init()
          }
        })
      }).catch(() => {
        // on cancel
      });
    },
    showPayFn() {
      this.showPay = true
    },
    async payOrder(id, type) {
      Toast.loading
      await payOrder({ orderNo: id, payType: type })
      this.showPay = false
      this.init()
    },
    close() {
      Dialog.close()
    }
  }
}
</script>

<style lang="less" scoped>
  .order-detail-box {
    background: #f7f7f7;
    .order-status {
      background: #fff;
      margin-top: 44px;
      padding: 20px;
      font-size: 15px;
      .status-item {
        margin-bottom: 10px;
        label {
          color: #999;
        }
        span {

        }
      }
    }
    .order-price {
      background: #fff;
      margin: 20px 0;
      padding: 20px;
      font-size: 15px;
      .price-item {
        margin-bottom: 10px;
        label {
          color: #999;
        }
        span {

        }
      }
    }
    .van-card {
      margin-top: 0;
    }
    .van-card__content {
      display: flex;
      flex-direction: column;
      justify-content: center;
    }
  }
</style>
