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