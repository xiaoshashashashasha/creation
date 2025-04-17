import request from "@/utils/request";

export const walletInfo = ()=>{
    return request.get('/wallet/walletInfo',{
        params:{

        }
    })
}