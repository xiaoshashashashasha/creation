import request from "@/utils/request"

export const hairstyleAdd = (HairstyleData) =>{
    return request.post("/hairstyle/add", HairstyleData)
}

export const hairstyleUpdate = (HairstyleData) =>{
    return request.put("/hairstyle/update", HairstyleData)
}

export const hairstyleDel = (hairstyle_id) =>{
    return request.delete('/hairstyle/del',{
        params: {
            hairstyle_id
        }
    })
}

export const hairstyleList = (params) =>{
    return request.get('/hairstyle/list',{
        params
    })
}

export const hairstyleInfo = (hairstyle_id) =>{
    return request.get('/hairstyle/hairStyleInfo',{
        params: {
            hairstyle_id
        }
    })
}