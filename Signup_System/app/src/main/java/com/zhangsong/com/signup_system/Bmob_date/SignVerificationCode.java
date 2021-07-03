package com.zhangsong.com.signup_system.Bmob_date;

import cn.bmob.v3.BmobObject;

public class SignVerificationCode extends BmobObject {
    private String VerificationCode;//报考验证码

    public String getVerificationCode() {
        return VerificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        VerificationCode = verificationCode;
    }

    @Override
    public String toString() {
        return "SignVerificationCode{" +
                "VerificationCode='" + VerificationCode + '\'' +
                '}';
    }
}
