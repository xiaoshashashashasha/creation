import request from "@/utils/request";

export const creationTagList = (params) =>{
    return request.get('/tag/list',{
        params
    })
}

export const creationTagAdd = (TagData) =>{

    return request.post('/tag/add', TagData)
}

export const creationTagDel = (tag_id) =>{
    return request.delete('/tag/del',{
        params: {
            tag_id
        }
    })
}


export const creationClassList = (params) =>{
    return request.get('/class/list',{
        params
    })
}

export const creationClassAdd = (ClassData) =>{

    return request.post('/class/add', ClassData)
}

export const creationClassDel = (class_id) =>{
    return request.delete('/class/del',{
        params: {
            class_id
        }
    })
}

export const creationListExamine = (params) =>{
    return request.get('/creation/listToExamine',{
        params
    })
}

export const creationGetTags = (creation_id) =>{
    return request.get('/creation/getTagsByCId',{
        params: {
            creation_id
        }
    })
}

export const creationConnectTag = (creation_id,tag_id) =>{
    const params = new URLSearchParams()
    params.append('creation_id', creation_id)
    params.append('tag_id', tag_id)

    return request.post('/creation/connectTag',params,{
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
}

export const creationCancelConnectTag = (creation_id,tag_id) =>{
    return request.delete('/creation/cancelConnect',{
        params: {
            creation_id,
            tag_id
        }
    })
}

export const creationInfo = (creation_id) =>{
    return request.get('/creation/creationInfo',{
        params: {
            creation_id
        }
    })
}

export const creationExamine = (creation_id,examine,review_comments) =>{
    const params = new URLSearchParams()
    params.append('creation_id', creation_id)
    params.append('examine', examine)
    params.append('review_comments', review_comments)

    return request.patch('/creation/examine',params,{
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
}

