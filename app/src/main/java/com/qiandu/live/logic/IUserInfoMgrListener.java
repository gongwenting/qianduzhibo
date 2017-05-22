package com.qiandu.live.logic;


/**
 * @description:  用户资料管理类与服务器通信的结果回调，包括查询资料的结果，修改资料的结果等。
 *
 * @author: Andruby
 * @time: 2016/11/4 14:12
 */
public interface IUserInfoMgrListener {

    /**
     * 用户信息查询结果
     *
     * @param error 查询结果, 0表示成功，非0表示失败
     * @param errorMsg 查询失败的错误信息
     */
    void OnQueryUserInfo(int error, String errorMsg);

    /**
     * 用户信息设置结果
     *
     * @param error  设置结果,0表示成功，非0表示失败
     * @param errorMsg 设置失败的错误信息
     */
    void OnSetUserInfo(int error, String errorMsg);

}
