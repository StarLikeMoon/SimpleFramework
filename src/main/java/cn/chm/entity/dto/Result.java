package cn.chm.entity.dto;

/**
 * 用来进行sevice层和controller层的数据传输的封装类
 * @param <T>
 */
public class Result<T> {

    // 本次请求结果的状态码，200表示成功
    private int code;
    // 本次请求结果的详情
    private String msg;
    // 本次请求返回的结果集
    private T data;

}
