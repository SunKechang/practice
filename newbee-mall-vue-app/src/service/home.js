import axios from '../utils/axios'
import config from '../../backend-config'
const { mode } = config

export function getHome(params) {
  // debugger
  return axios.get(mode ==='test' ? '/index-infos' : '/mall/index/recommondInfos');
}

