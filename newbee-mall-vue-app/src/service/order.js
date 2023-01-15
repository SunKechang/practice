import axios from '../utils/axios'
import config from '../../backend-config'
const { mode } = config

const prefix = mode === 'test' ? '' : '/orders/mall'

export function createOrder(params) {
  return axios.post(`${prefix}/saveOrder`, params);
}

export function getOrderList(params) {
  return axios.get(`${prefix}/order`, { params });
}

export function getOrderDetail(id) {
  return axios.get(`${prefix}/order/${id}`);
}

export function cancelOrder(id) {
  return axios.put(`${prefix}/order/${id}/cancel`);
}

export function confirmOrder(id) {
  return axios.put(`${prefix}/order/${id}/finish`)
}

export function payOrder(params) {
  return axios.get(`${prefix}/paySuccess`, { params })
}




