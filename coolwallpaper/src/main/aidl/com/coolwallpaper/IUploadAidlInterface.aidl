// IUploadAidlInterface.aidl
package com.coolwallpaper;

// Declare any non-default types here with import statements

interface IUploadAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    /**
    *  获取正在上传的图片的数量
    */
    int getUploadingCount();

    /**
    *   获取正在上传的图片的本地路径
    */
    String[] getUploadingFilePaths();

}
