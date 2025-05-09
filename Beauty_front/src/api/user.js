import request from '@/utils/request'

export const userRegisterService = (registerData) => {
    const params = new URLSearchParams()
    for (const key in registerData) {
        params.append(key, registerData[key])
    }

    return request.post('/user/register', params, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
}

export const userLoginService = (loginData) => {
    const params = new URLSearchParams()
    for (const key in loginData) {
        params.append(key, loginData[key])
    }

    return request.post('/user/login', params, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
}

export const userListService = (params) => {
    return request.get('/user/list', {
        params
    })
}

export const userChangeRoleService = (user_id,role) => {
    const params = new URLSearchParams()
    params.append('user_id', user_id)
    params.append('role', role)

    return request.patch('/user/changeRole', params,{
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
}

export const userCheckRoleService = () => {
    return request.get('/user/roleCheck',{
        params: {}
    })
}

export const userLogoutService = () => {
    return request.get('/user/logout')
}

export const userOtherInfoService = (user_id) => {
    return request.get('/user/otherInfo',{
        params:{
            user_id
        }
    })
}

export const userFollowInfoService = (followed_id) => {
    return request.get('/user/followInfo',{
        params:{
            followed_id
        }
    })
}

export const userFollowService = (followed_id) => {
    return request.post('/user/follow', null, {
        params: {
            followed_id
        }
    })
}


export const userCancelFollowService = (followed_id) => {
    return request.delete('/user/cancelFollow',{
        params:{
            followed_id
        }
    })
}

export const userInfoService = () => {
    return request.get('/user/myInfo',{
        params:{}
    })
}

export const listFollowedService = ()=>{
    return request.get('/user/listFollowed',{
        params:{}
    })
}

export const listFollowerService = ()=>{
    return request.get('/user/listFollower',{
        params:{}
    })
}

export const updateUserPicService = (avatarUrl) => {
    return request.patch('/user/updateAvatar', null, {
        params: {
            avatarUrl
        }
    })
}

export const updatePasswordService = (oldPwd, newPwd, rePwd) => {
    return request.patch('/user/updatePwd', {
        old_pwd: oldPwd,
        new_pwd: newPwd,
        re_pwd: rePwd
    })
}
