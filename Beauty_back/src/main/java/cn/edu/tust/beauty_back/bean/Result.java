package cn.edu.tust.beauty_back.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    private int code;   //状态码，0-成功，1-失败
    private String msg;
    private T data;

    //带数据成功响应
    public static <E> Result<E> success(E data) {
        return new Result<>(0,"操作成功",data);
    }

    //不带数据成功响应
    public static Result success() {
        return new Result(0,"操作成功",null);
    }

    //失败响应
    public static Result error(String msg) {
        return new Result(1,msg,null);
    }
}
