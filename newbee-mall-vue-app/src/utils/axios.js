import axios from 'axios'
import { Toast } from 'vant'
import router from '../router'
import config from '../../backend-config'
const { mode } = config

const devURL = mode === 'test' ? 'http://backend-api-01.newbee.ltd/api/v1' : 'http://localhost:29110'
const buildURL = '' // 部署时的baseURL

axios.defaults.baseURL = process.env.NODE_ENV == 'development' ?  devURL : buildURL
axios.defaults.withCredentials = true
axios.defaults.headers['X-Requested-With'] = 'XMLHttpRequest'
axios.defaults.headers['token'] = localStorage.getItem('token') || ''
axios.defaults.headers.post['Content-Type'] = 'application/json'

axios.interceptors.response.use(res => {
  if (typeof res.data !== 'object') {
    Toast.fail('服务端异常！')
    return Promise.reject(res)
  }
  if (res.data.resultCode != 200) {
    if (res.data.message) Toast.fail(res.data.message)
    if (res.data.resultCode == 416) {
      router.push({ path: '/login' })
    }
    return Promise.reject(res.data)
  }

  return res.data
})

export default axios
