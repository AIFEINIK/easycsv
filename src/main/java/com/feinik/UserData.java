package com.feinik;

import com.feinik.annotation.CsvProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Feinik
 * @discription
 * @date 2019/7/10
 * @since 1.0.0
 */
@Data
public class UserData implements Serializable {
    @CsvProperty(value = "姓名", index = 0)
    private String userName;
    @CsvProperty(value = "地址", index = 2)
    private String address;
    @CsvProperty(value = "年龄", index = 1, format = "{0}岁")
    private Integer age;

    public UserData() {
    }

    public UserData(String userName, Integer age, String address) {
        this.userName = userName;
        this.age = age;
        this.address = address;
    }
}
