package com.example.demo;

import java.util.ServiceLoader;

/**
 * @author baopc@tuya.com
 * @date 2020/6/29
 */
public class StaticMain {
    public static void main(String[] args){
        Class<IShout> cls = IShout.class;
        ServiceLoader<IShout> serviceLoader = ServiceLoader.load(cls);
        for (IShout item:serviceLoader){
            item.shout();
        }
        System.out.println("finish");
    }
}
