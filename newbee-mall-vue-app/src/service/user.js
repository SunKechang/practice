import axios from '../utils/axios'
import config from '../../backend-config'
const { mode } = config

export function getUserInfo() {
  return axios.get(mode === 'test' ? '/user/info' : '/users/mall/detail');
}

export function EditUserInfo(params) {
  return axios.put(mode === 'test' ? '/user/info' : '/users/mall/update', params);
}

export function login(params) {
  return axios.post(mode === 'test' ? '/user/login' : '/users/mall/login', params);
}

export function logout() {
  return axios.post(mode === 'test' ? '/user/logout' : '/users/mall/logout')
}

export function register(params) {
  return axios.post(mode === 'test' ? '/user/register' : '/users/mall/register', params);
}

