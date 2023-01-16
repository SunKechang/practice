

<template>
  <div class="create-order">
    <el-page-header title="" content="生成订单"></el-page-header>
    
    <div class="address-wrap">
      <el-descriptions title="用户信息">
        <el-descriptions-item label="用户名">{{ address.userName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ address.userPhone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址">{{ address.provinceName }} {{ address.cityName }} {{ address.regionName }} {{ address.detailAddress }}</el-descriptions-item>
      </el-descriptions>
    </div>
    <el-button @click="goTo" type="primary">选择地址</el-button>
    <div class="good">
      <el-table
      :data="cartList"
      style="width: 100%">
        <el-table-column
          prop="goodsName"
          label="商品名称"
          width="180">
        </el-table-column>
        <el-table-column
          prop="sellingPrice"
          label="单价"
          width="180">
        </el-table-column>
        <el-table-column
          prop="goodsCount"
          label="数量">
        </el-table-column>
      </el-table>
    </div>

    <div class="pay-wrap">
      <div class="price">
        <span>商品金额</span>
        <span>¥{{ total }}</span>
      </div>
      <el-button @click="createOrder" type="primary" style="width: 80%;margin-left: 10%;">生成订单</el-button>
    </div>
    <van-popup
      closeable
      :close-on-click-overlay="false"
      v-model="showPay"
      position="bottom"
      :style="{ height: '30%' }"
      @close="close"
    >
      <div :style="{ width: '90%', margin: '0 auto', padding: '50px 0' }">
        <van-button :style="{ marginBottom: '10px' }" color="#1989fa" block @click="payOrder(1)">支付宝支付</van-button>
        <van-button color="#4fc08d" block @click="payOrder(2)">微信支付</van-button>
      </div>
    </van-popup>
  </div>
</template>

<script>
import sHeader from '@/components/SimpleHeader'
import { getCart, getByCartItemIds } from '../service/cart'
import { getDefaultAddress, getAddressDetail } from '../service/address'
import { createOrder, payOrder } from '../service/order'
import { setLocal, getLocal } from '@/common/js/utils'
import { Toast } from 'vant'
export default {
  data() {
    return {
      cartList: [],
      address: {},
      showPay: false,
      orderNo: '',
      cartItemIds: []
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    async init() {
      Toast.loading({ message: '加载中...', forbidClick: true });
      const { addressId, cartItemIds } = this.$route.query
      const _cartItemIds = cartItemIds ? JSON.parse(cartItemIds) : JSON.parse(getLocal('cartItemIds'))
      setLocal('cartItemIds', JSON.stringify(_cartItemIds))
      const { data: list } = await getByCartItemIds({ cartItemIds: _cartItemIds.join(',') })
      const { data: address } = addressId ? await getAddressDetail(addressId) : await getDefaultAddress()
      if (!address) {
        this.$router.push({ path: 'address' })
        return
      }
      this.cartList = list
      this.address = address
      Toast.clear()
    },
    goTo() {
      this.$router.push({ path: `address?cartItemIds=${JSON.stringify(this.cartItemIds)}` })
    },
    deleteLocal() {
      setLocal('cartItemIds', '')
    },
    async createOrder() {
      const params = {
        addressId: this.address.addressId,
        cartItemIds: this.cartList.map(item => item.cartItemId)
      }
      const { data, resultCode } = await createOrder(params)
      setLocal('cartItemIds', '')
      this.orderNo = data
      this.showPay = true
    },
    close() {
      this.$router.push({ path: 'order' })
    },
    async payOrder(type) {
      Toast.loading
      await payOrder({ orderNo: this.orderNo, payType: type })
      this.$router.push({ path: 'order' })
    }
  },
  computed: {
    total: function() {
      let sum = 0
      this.cartList.forEach(item => {
        sum += item.goodsCount * item.sellingPrice
      })
      return sum
    }
  }
}
</script>

<style lang="less" scoped>
  @import '../common/style/mixin';
  .create-order {
    background: #f9f9f9;
    .address-wrap {
      margin-bottom: 20px;
      background: #fff;
      position: relative;
      margin-top: 44px;
      font-size: 14px;
      padding: 15px;
      color: #222333;
      .name, .address {
        margin: 10px 0;
      }
      .arrow {
        position: absolute;
        right: 10px;
        top: 50%;
        transform: translateY(-50%);
        font-size: 20px;
      }
      &::before {
        position: absolute;
        right: 0;
        bottom: 0;
        left: 0;
        height: 2px;
        background: -webkit-repeating-linear-gradient(135deg, #ff6c6c 0, #ff6c6c 20%, transparent 0, transparent 25%, #1989fa 0, #1989fa 45%, transparent 0, transparent 50%);
        background: repeating-linear-gradient(-45deg, #ff6c6c 0, #ff6c6c 20%, transparent 0, transparent 25%, #1989fa 0, #1989fa 45%, transparent 0, transparent 50%);
        background-size: 80px;
        content: '';
      }
    }
    .good {
      margin-bottom: 120px;
    }
    .good-item {
      padding: 10px;
      background: #fff;
      display: flex;
      .good-img {
        img {
          .wh(100px, 100px)
        }
      }
      .good-desc {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        flex: 1;
        padding: 20px;
        .good-title {
          display: flex;
          justify-content: space-between;
        }
        .good-btn {
          display: flex;
          justify-content: space-between;
          .price {
            font-size: 16px;
            color: red;
            line-height: 28px;
          }
          .van-icon-delete {
            font-size: 20px;
            margin-top: 4px;
          }
        }
      }
    }
    .pay-wrap {
      position: fixed;
      bottom: 0;
      left: 0;
      width: 100%;
      background: #fff;
      padding: 10px 0;
      padding-bottom: 50px;
      border-top: 1px solid #e9e9e9;
      >div {
        display: flex;
        justify-content: space-between;
        padding: 0 5%;
        margin: 10px 0;
        font-size: 14px;
        span:nth-child(2) {
          color: red;
          font-size: 18px;
        }
      }
      .pay-btn {
        position: fixed;
        bottom: 7px;
        right: 0;
        left: 0;
        width: 90%;
        margin: 0 auto;
      }
    }
  }
</style>
