package com.coolwallpaper.bean;

import java.util.Map;

/**
 * QQ登录获取到的用户信息
 * Created by fuchao on 2016/5/27.
 */
public class QQUserInfo implements IUserInfo {

    private String openId;//QQ系统的用户id
    private String province; //省份
    private String gender;//性别
    private String screenName;//姓名
    private String profileImgeUrl;//头像
    private String city;//城市

    public QQUserInfo() {

    }

    //map是qq登录成功后获取的用户信息
    public QQUserInfo(Map<String, String> map) {
        this.openId = map.get("openid");
        this.province = map.get("province");
        this.gender = map.get("gender");
        this.screenName = map.get("screen_name");
        this.profileImgeUrl = map.get("profile_image_url");
        this.city = map.get("city");
    }

    @Override
    public String getAccount() {
        return openId;
    }

    @Override
    public String getName() {
        return screenName;
    }

    @Override
    public String getImg() {
        return profileImgeUrl;
    }

    @Override
    public int getSex() {
        if ("男".equals(gender)) {
            return SEX_MAN;
        } else {
            return SEX_GIRL;
        }
    }

    @Override
    public int getUserType() {
        return USER_TYPE_QQ;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImgeUrl() {
        return profileImgeUrl;
    }

    public void setProfileImgeUrl(String profileImgeUrl) {
        this.profileImgeUrl = profileImgeUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
