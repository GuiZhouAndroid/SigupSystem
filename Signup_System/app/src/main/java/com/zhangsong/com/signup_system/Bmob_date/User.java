package com.zhangsong.com.signup_system.Bmob_date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {
    private String sex;//性别
    private String signature;//个性签名
    private String age;//年龄
    private BmobFile Icon;//头像

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public BmobFile getIcon() {
        return Icon;
    }

    public void setIcon(BmobFile icon) {
        Icon = icon;
    }

    @Override
    public String toString() {
        return "User{" +
                "sex='" + sex + '\'' +
                ", signature='" + signature + '\'' +
                ", age='" + age + '\'' +
                ", Icon=" + Icon +
                '}';
    }
}
