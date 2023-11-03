package org.stacksm4sher.jvma;

import static java.util.Objects.isNull;

public class ParsedBytecode {

    private ClassInfo classInfo;
    private ConstantPoolInfo constantPoolInfo;
    private InterfaceInfo interfaceInfo;
    private FieldInfo fieldInfo;
    private MethodInfo methodInfo;
    private final RawBytecode rawBytecode;

    public ParsedBytecode(RawBytecode rawBytecode) {
        this.rawBytecode = rawBytecode;
    }

    public ClassInfo getClassInfo() {
        if (isNull(classInfo)) {
            this.classInfo = new ClassInfo(rawBytecode);
            return this.classInfo;
        }
        return this.classInfo;
    }

    public ConstantPoolInfo getConstantPool() {
        if (isNull(constantPoolInfo)) {
            this.constantPoolInfo = new ConstantPoolInfo(rawBytecode);
            return this.constantPoolInfo;
        }
        return this.constantPoolInfo;
    }

    public MethodInfo getMethodsInfo() {
        if (isNull(methodInfo)) {
            ConstantPoolInfo constantPool = this.getConstantPool();
            this.methodInfo = new MethodInfo(rawBytecode, constantPool);
            return this.methodInfo;
        }
        return this.methodInfo;
    }
}
