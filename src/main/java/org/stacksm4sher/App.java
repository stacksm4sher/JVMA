package org.stacksm4sher;

import org.stacksm4sher.jvma.BytecodeUtils;
import org.stacksm4sher.jvma.ClassInfo;
import org.stacksm4sher.jvma.ConstantPoolInfo;
import org.stacksm4sher.jvma.MethodInfo;
import org.stacksm4sher.jvma.ParsedBytecode;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        ParsedBytecode parsedBytecode = BytecodeUtils.parseBytecodeFromClass(App.class);

        ClassInfo classInfo = parsedBytecode.getClassInfo();
        ConstantPoolInfo constantPool = parsedBytecode.getConstantPool();
        MethodInfo methodsInfo = parsedBytecode.getMethodsInfo();

        methodsInfo.getMethod(0);

        

        Thread.currentThread().getClass().getClassLoader();

        int a = 1;
    }

    private void m(String haha, int a, int b) {
        System.out.println(haha);
        System.out.println(a + b);
    }

    // object asm


    private interface I {

    }

    private static class IImpl implements I {

    }
}
