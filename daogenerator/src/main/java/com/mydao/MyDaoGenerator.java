package com.mydao;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.DaoGenerator;

/**
 * 创建greenDao的Generator
 * Created by fuchao 24/11/2015
 */
public class MyDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.coolwallpaper.model");
        addNote(schema);
        new DaoGenerator().generateAll(schema,"coolwallpaper/src/main/java-gen");
    }

    public static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
    }
}
