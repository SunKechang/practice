import axios from '../utils/axios'
import config from '../../backend-config'
const { mode } = config

export function getDetail(id) {
  return axios.get(mode === 'test' ? `/goods/detail/${id}` : `/goods/mall/detail/${id}`);
}

export function getCategory() {
  return axios.get(mode === 'test' ? '/categories' : '/categories/mall/listAll');
}

export function search(params) {
  return axios.get(mode === 'test' ? '/search' : '/goods/mall/search', { params });
}

