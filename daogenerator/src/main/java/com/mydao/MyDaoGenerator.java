package com.mydao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * 创建greenDao的Generator
 * Created by fuchao 24/11/2015
 */
public class MyDaoGenerator {

    public static final String TB_PICTURE = "tb_picture";
    public static final String TB_PARAM = "tb_param";
    public static final String TB_PARAM_PICTURE = "tb_param_picture";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.coolwallpaper.model");
        //创建数据库表
        addNote(schema);
        new DaoGenerator().generateAll(schema, "coolwallpaper/src/main/java");
    }

    public static void addNote(Schema schema) {
        //添加图片表
        Entity picture = schema.addEntity("Picture");
        picture.setTableName(TB_PICTURE);//设置数据库表的名称
        picture.implementsSerializable();//实现序列化接口
        picture.addIdProperty().primaryKey().autoincrement();//id是主键
        picture.addStringProperty("thumbUrl");//缩略图url
        picture.addStringProperty("downloadUrl").unique();//原图url,原图不能重复
        picture.addStringProperty("fromUrl");//图片来源
        picture.addIntProperty("width");//原图的宽度
        picture.addIntProperty("height");//原图的高度
        picture.addStringProperty("desc");//图片描述
        //添加网络访问参数表.theme title1 title2三个字段唯一确定了一个param
        Entity param = schema.addEntity("Param");
        param.setTableName(TB_PARAM);
        param.implementsSerializable();//实现序列化接口
        param.addIdProperty().primaryKey().autoincrement();//主键
        param.addStringProperty("title1");//注意：没有办法设置多个主键，所以插入时要注意判断是否重复
        param.addStringProperty("title2");
        //添加依赖关系,param和picture是1:N的关系
        Property paramId = picture.addLongProperty("paramId").notNull().getProperty();
        //首先添加param对picture的一对多关系
        param.addToMany(picture, paramId);
        //接着添加picture对param的1对1关系
        picture.addToOne(param, paramId);
    }

}
