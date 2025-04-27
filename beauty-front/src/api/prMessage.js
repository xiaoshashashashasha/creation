import request from "@/utils/request";

export const sendMessage = (params)=>{
    return request.post("/prMessage/send",params);
}

export const getHistory = (params) => {
    return request.get("/prMessage/history",{
        params
    });
}

export const getUnReadCount = () => {
    return request.get("/prMessage/tUnReadCount")
}

export const setMessageRead = (target_id)=>{
    return request.post("/prMessage/setAllRead",null,{
        params:{
            target_id
        }
    })
}

export const getChatList = () => {
    return request.get("/prMessage/getChatList");
};