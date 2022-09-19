package asm;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AsmTest {

    public static void main(String[] args) throws IOException {
        // 读取字节码
        ClassReader classReader = new ClassReader("asm.Test");
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        // 字节码增强
        AsmOpt classVisitor = new AsmOpt(classWriter);
        classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);
        byte[] data = classWriter.toByteArray();
        // 输出字节码到class文件
        File f = new File("/Users/liuyong/Downloads/work/agent-template/src/test/java/asm/Test.class");
        FileOutputStream fout = new FileOutputStream(f);
        fout.write(data);
        fout.close();
    }
}
