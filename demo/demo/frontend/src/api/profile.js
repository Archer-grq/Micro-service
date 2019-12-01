import request from '@/utils/request'

export function info(username) {
  return request({
    url: '/profile/info/'+username,
    method: 'get'
  })
}

//一定要传 data 只有data 才会以json方式放松(做这个的自己要求的)
export function update(data) {
  return request({
    url: '/profile/update',
    method: 'post',
    data
  })
}

/**
 * 更新头像
 * @param data
 */
export function modifyIcon(data) {
  return request({
    url: '/profile/modify/icon',
    method: 'post',
    data
  })
}
