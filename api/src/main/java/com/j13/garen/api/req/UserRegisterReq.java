package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class UserRegisterReq {
//    String mobile, String password, String nickName, Integer isMachine, FileItem file

    @Parameter(desc="注册用手机号")
    private String mobile;
    @Parameter(desc="密码（已加密）")
    private String password;
//    @Parameter(desc="昵称")
//    private String nickName;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public String getNickName() {
//        return nickName;
//    }
//
//    public void setNickName(String nickName) {
//        this.nickName = nickName;
//    }



}
