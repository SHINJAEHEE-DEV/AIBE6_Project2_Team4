package org.project.standard.util;

public class Util {
    // 공통으로 쓸 수 있게 App이나 별도 Util 클래스에 넣어두세요.
    public static void d(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void delay(int ms){
        try {
            Thread.sleep(ms);//시간만큼 쓰레드 sleep
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();//
        }
    }
}
