import request from "@/utils/request";

export const offlineRequestListExa = (params) => {
    return request.get('/offline/requestListExa',{
        params
    })
}
export const offlineExamine = (request_id,examine,review_comments) => {
    const params = new URLSearchParams()
    params.append('request_id', request_id)
    params.append('examine', examine)
    params.append('review_comments', review_comments)

    return request.patch('/offline/examine',params,{
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
}

export const offlineInfo = (offline_id) =>{
    return request.get('/offline/offlineInfo',{
        params: {
            offline_id
        }
    })
}

export const offlineList = (params) =>{
    return request.get('/offline/offlineList',{
        params
    })
}

export const offlineRequestList = (params) =>{
    return request.get('/offline/requestList',{
        params
    })
}

export const addRequest = (params)=>{
    return request.post('/offline/addRequest',params)
}

export const myOfflineList = (params) =>{
    return request.get('/offline/myOfflineList',{
        params
    })
}

export const improveOffline = (params) =>{
    return request.patch('/offline/improveOffline', params)
}

export const delOffline = (offline_id) =>{
    return request.delete('/offline/delOffline',{
        params: {
            offline_id
        }
    })
}

export const memberList = (offline_id) =>{
    return request.get('/offline/memberList',{
        params:{
            offline_id
        }
    })
}

export const addMember = (params) =>{
    return request.post('/offline/addMember',params)
}

export const delMember = (member_id) =>{
    return request.delete('/offline/delMember',{
        params: {
            member_id
        }
    })
}