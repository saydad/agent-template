package asm;

public class Test {

    public void say() {
        // 插入输出 start
        System.out.println("doing~~");
        // 插入输出 end
    }

    public static void main(String[] args) {
        new Test().say();
    }
}
