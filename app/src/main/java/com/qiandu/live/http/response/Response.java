package com.qiandu.live.http.response;


import com.qiandu.live.http.IDontObfuscate;
/**
 * @description: 基础返回数据
 *
 * @author: Andruby
 * @time: 2016/11/2 18:07
 */
public class Response<T>  extends IDontObfuscate {

	public int code;
	public String msg;
	public T data;
	@Override
	public String toString() {
		return "Response [code=" + code + ", msg=" + msg + ", data=" + data
				+ "]";
	}

}

