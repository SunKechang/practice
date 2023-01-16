<template>
  <div class="address-box">
    <s-header :name="'地址管理'" :back="'/user'"></s-header>
    <div class="address-item">
      <el-table
      :data="list"
      style="width: 100%">
        <el-table-column
          prop="name"
          label="收货人"
          width="180">
        </el-table-column>
        <el-table-column
          prop="tel"
          label="手机号"
          width="180">
        </el-table-column>
        <el-table-column
          prop="address"
          label="收货地址">
        </el-table-column>
        <el-table-column
          label="地址类型">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isDefault === true" type="success">默认地址</el-tag>
          </template>
          
        </el-table-column>
        <el-table-column
          label="操作">
          <template slot-scope="scope">
            <el-button @click="select(scope.row)" type="success">选择</el-button>
          </template>
          
        </el-table-column>
        
      </el-table>
      <el-button @click="onAdd" type="primary">添加地址</el-button>
    </div>
  </div>
</template>

<script>
import { Toast } from 'vant';
import sHeader from '@/components/SimpleHeader'
import { getAddressList } from '../service/address'
export default {
  data() {
    return {
      chosenAddressId: '1',
      list: [],
      from: this.$route.query.from,
    }
  },
  async mounted() {
    const { data } = await getAddressList()
    this.list = data.map(item => {
      return {
        id: item.addressId,
        name: item.userName,
        tel: item.userPhone,
        address: `${item.provinceName} ${item.cityName} ${item.regionName} ${item.detailAddress}`,
        isDefault: !!item.defaultFlag
      }
    })
  },
  methods: {
    onAdd() {
      this.$router.push({ path: `address-edit?type=add&from=${this.from}` })
    },
    onEdit(item, index) {
      this.$router.push({ path: `address-edit?type=edit&addressId=${item.id}&from=${this.from}` })
    },
    select(item, index) {
      this.$router.push({ path: `create-order?addressId=${item.id}&from=${this.from}` })
    }
  }
}
</script>

<style lang="less">
  @import '../common/style/mixin';
  .address-box {
    .van-radio__icon {
      display: none;
    }
    .address-item {
      margin-top: 44px;
      .van-button {
        background: @primary;
        border-color: @primary;
      }
    }
  }
</style>
