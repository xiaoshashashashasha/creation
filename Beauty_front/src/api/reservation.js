import request from "@/utils/request";

export const addReservation = (params) => {
    return request.post('/reservation/add', params);
}

export const deleteReservation = (reservation_id) => {
    return request.delete(`/reservation/del`,{
        params:{
            reservation_id
        }
    });
}

export const myReservationList = (params) =>{
    return request.get('/reservation/myList',{
        params
    })
}

export const evaluate = (reservation_id, point, comment) =>{
    return request.patch(`/reservation/evaluate`, null,{
        params:{
            reservation_id,
            point,
            comment
        }
    })
}

export const offlineCommentList = (params) =>{
    return request.get('/reservation/commentList',{
        params
    })
}

export const offlineReservationList = (params) =>{
    return request.get('/reservation/offlineList',{
        params
    })
}

export const updateReservation = (reservation_id, status) => {
    return request.patch(`/reservation/updateStatus`, null, {
        params: {
            reservation_id,
            status
        }
    })
}


