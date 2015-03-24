package com.bigfat.androidliteormtest.model;

import com.litesuits.orm.db.annotation.Default;
import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.Table;

/**
 * Created by yueban on 15/3/24.
 */
@Table("t_user")
public class User extends BaseModel{
    @NotNull
    private String name;

    @NotNull
    private String age;

    //性别0：男 1：女 2：未知
    @Default("2")
    private int gender;

    public User(String name, String age, int gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", gender=" + gender +
                '}';
    }
}
