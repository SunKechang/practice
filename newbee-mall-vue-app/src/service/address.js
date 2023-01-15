import axios from '../utils/axios'
import config from '../../backend-config'

const { mode } = config
const prefix = mode === 'test' ? '' : '/mall'

export function addAddress(params) {
  return axios.post(`${prefix}/address`, params);
}

export function EditAddress(params) {
  return axios.put(`${prefix}/address`, params);
}

export function DeleteAddress(id) {
  return axios.delete(`${prefix}/address/${id}`);
}

export function getDefaultAddress() {
  return axios.get(`${prefix}/address/default`);
}

export function getAddressList() {
  return axios.get(`${prefix}/address`, { pageNumber: 1, pageSize: 1000 })
}

export function getAddressDetail(id) {
  return axios.get(`${prefix}/address/${id}`)
}


