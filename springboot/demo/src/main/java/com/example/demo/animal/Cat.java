package com.example.demo.animal;

import com.example.demo.IShout;

/**
 * @author baopc@tuya.com
 * @date 2020/6/29
 */
public class Cat implements IShout {
    @Override
    public void shout(){
        System.out.println("cat");
    }
}
