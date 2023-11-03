package org.stacksm4sher.jvma;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

public class ConstantPoolInfo {

    private int length;
    private final List<Constant> constantList;
    private final RawBytecode rawBytecode;

    public ConstantPoolInfo(RawBytecode rawBytecode) {
        this.length = 0;
        this.constantList = new ArrayList<>();
        this.rawBytecode = rawBytecode;
        this.parse();
    }

    public Constant getConstant(int index) {
        return this.constantList.get(index - 1);
    }

    public List<Constant> getConstantList() {
        return this.constantList;
    }

    public int getLength() {
        return this.length;
    }

    private void parse() {
        byte[] constant_pool_count = rawBytecode.getConstant_pool_count();
        byte[] cp_info = rawBytecode.getCp_info();

        int byte_code_ptr = 0;

        short cp_count = (short) (((0xff & constant_pool_count[0]) << 8) | (0xff & constant_pool_count[1]));
        this.length = cp_count - 1;
        for (short i = 0; i < this.length; ++i) {
            byte tag = cp_info[byte_code_ptr];
            byte[] buffer;
            if (tag == 7) {
                buffer = new byte[3];
                System.arraycopy(cp_info, byte_code_ptr, buffer, 0, buffer.length);
                byte_code_ptr += buffer.length;
                int ctag = (0xff & buffer[0]);
                int name_index = ((0xff & buffer[1]) << 8) | (0xff & buffer[2]);
                CONSTANT_Class constant_class = new CONSTANT_Class(ctag, name_index);
                Constant constant = new Constant(constant_class);
                constantList.add(constant);
            }
            if (tag == 9 || tag == 10 || tag == 11) {
                buffer = new byte[5];
                System.arraycopy(cp_info, byte_code_ptr, buffer, 0, buffer.length);
                byte_code_ptr += buffer.length;
                int ctag = (0xff & buffer[0]);
                int class_index = ((0xff & buffer[1]) << 8) | (0xff & buffer[2]);
                int name_and_type_index = ((0xff & buffer[3]) << 8) | (0xff & buffer[4]);
                if (tag == 9) {
                    CONSTANT_Fieldref constant_fieldref = new CONSTANT_Fieldref(ctag, class_index, name_and_type_index);
                    Constant constant = new Constant(constant_fieldref);
                    constantList.add(constant);
                }
                if (tag == 10) {
                    CONSTANT_Methodref constant_methodref = new CONSTANT_Methodref(ctag, class_index, name_and_type_index);
                    Constant constant = new Constant(constant_methodref);
                    constantList.add(constant);
                }
                if (tag == 11) {
                    CONSTANT_InterfaceMethodref constant_interfaceMethodref = new CONSTANT_InterfaceMethodref(ctag, class_index, name_and_type_index);
                    Constant constant = new Constant(constant_interfaceMethodref);
                    constantList.add(constant);
                }
            }
            if (tag == 8) {
                buffer = new byte[3];
                System.arraycopy(cp_info, byte_code_ptr, buffer, 0, buffer.length);
                byte_code_ptr += buffer.length;
                int ctag = (0xff & buffer[0]);
                int string_index = ((0xff & buffer[1]) << 8) | (0xff & buffer[2]);
                CONSTANT_String constant_class = new CONSTANT_String(ctag, string_index);
                Constant constant = new Constant(constant_class);
                constantList.add(constant);
            }
            if (tag == 3 || tag == 4) {
                buffer = new byte[5];
                System.arraycopy(cp_info, byte_code_ptr, buffer, 0, buffer.length);
                byte_code_ptr += buffer.length;
                int ctag = (0xff & buffer[0]);
                if (tag == 3) {
                    int value = ((((0xff & buffer[1]) << 24)
                            | ((0xff & buffer[2]) << 16))
                            | ((0xff & buffer[3]) << 8))
                            | (0xff & buffer[4]);
                    CONSTANT_Integer constant_class = new CONSTANT_Integer(ctag, value);
                    Constant constant = new Constant(constant_class);
                    constantList.add(constant);
                }
                if (tag == 4) {
                    float value = ((((0xff & buffer[1]) << 24)
                            | ((0xff & buffer[2]) << 16))
                            | ((0xff & buffer[3]) << 8))
                            | (0xff & buffer[4]);
                    CONSTANT_Float constant_class = new CONSTANT_Float(ctag, value);
                    Constant constant = new Constant(constant_class);
                    constantList.add(constant);
                }
            }
            if (tag == 5 || tag == 6) {
                i++;
                buffer = new byte[9];
                System.arraycopy(cp_info, byte_code_ptr, buffer, 0, buffer.length);
                byte_code_ptr += buffer.length;
                int ctag = (0xff & buffer[0]);
                if (tag == 5) {
                    long value = ((((((((long) (0xff & buffer[1]) << 56)
                            | ((long) (0xff & buffer[2]) << 48))
                            | ((long) (0xff & buffer[3]) << 40))
                            | ((long) (0xff & buffer[4]) << 32))
                            | ((long) (0xff & buffer[5]) << 24))
                            | ((long) (0xff & buffer[6]) << 16))
                            | ((long) (0xff & buffer[7]) << 8))
                            | (long) (0xff & buffer[8]);
                    CONSTANT_Long constant_long = new CONSTANT_Long(ctag, value);
                    Constant constant = new Constant(constant_long);
                    constantList.add(constant);
                }
                if (tag == 6) {
                    double value = ((((((((long) (0xff & buffer[1]) << 56)
                            | ((long) (0xff & buffer[2]) << 48))
                            | ((long) (0xff & buffer[3]) << 40))
                            | ((long) (0xff & buffer[4]) << 32))
                            | ((long) (0xff & buffer[5]) << 24))
                            | ((long) (0xff & buffer[6]) << 16))
                            | ((long) (0xff & buffer[7]) << 8))
                            | (long) (0xff & buffer[8]);
                    CONSTANT_Double constant_double = new CONSTANT_Double(ctag, value);
                    Constant constant = new Constant(constant_double);
                    constantList.add(constant);
                }
                constantList.add(new Constant(new CONSTANT_longNumericContinued()));
            }
            if (tag == 12) {
                buffer = new byte[5];
                System.arraycopy(cp_info, byte_code_ptr, buffer, 0, buffer.length);
                byte_code_ptr += buffer.length;
                int ctag = (0xff & buffer[0]);
                int name_index = ((0xff & buffer[1]) << 8) | (0xff & buffer[2]);
                int descriptor_index = ((0xff & buffer[3]) << 8) | (0xff & buffer[4]);
                CONSTANT_NameAndType constant_nameAndType = new CONSTANT_NameAndType(ctag, name_index, descriptor_index);
                Constant constant = new Constant(constant_nameAndType);
                constantList.add(constant);
            }
            if (tag == 1) {
                int ctag = cp_info[byte_code_ptr];
                int length = ((0xff & cp_info[byte_code_ptr + 1]) << 8) | (0xff & cp_info[byte_code_ptr + 2]);
                byte_code_ptr += 3;
                buffer = new byte[length];
                System.arraycopy(cp_info, byte_code_ptr, buffer, 0, buffer.length);
                byte_code_ptr += buffer.length;

                char[] cbuffer = new char[buffer.length];
                for (int j = 0; j < buffer.length; ++j) {
                    cbuffer[j] = (char) buffer[j];
                }
                String value = String.valueOf(cbuffer);
                CONSTANT_Utf8 constant_utf8 = new CONSTANT_Utf8(ctag, length, value);
                Constant constant = new Constant(constant_utf8);
                constantList.add(constant);
            }
            if (tag == 15) {
                buffer = new byte[4];
                System.arraycopy(cp_info, byte_code_ptr, buffer, 0, buffer.length);
                byte_code_ptr += buffer.length;
                int ctag = (0xff & buffer[0]);
                int reference_kind = (0xff & buffer[1]);
                int reference_index = ((0xff & buffer[2]) << 8) | (0xff & buffer[3]);
                CONSTANT_MethodHandle constant_methodHandle = new CONSTANT_MethodHandle(ctag, reference_kind, reference_index);
                Constant constant = new Constant(constant_methodHandle);
                constantList.add(constant);
            }
            if (tag == 16) {
                buffer = new byte[3];
                System.arraycopy(cp_info, byte_code_ptr, buffer, 0, buffer.length);
                byte_code_ptr += buffer.length;
                int ctag = (0xff & buffer[0]);
                int descriptor_index = ((0xff & buffer[1]) << 8) | (0xff & buffer[2]);
                CONSTANT_MethodType constant_methodType = new CONSTANT_MethodType(ctag, descriptor_index);
                Constant constant = new Constant(constant_methodType);
                constantList.add(constant);
            }
            if (tag == 17 || tag == 18) {
                buffer = new byte[5];
                System.arraycopy(cp_info, byte_code_ptr, buffer, 0, buffer.length);
                byte_code_ptr += buffer.length;
                int ctag = (0xff & buffer[0]);
                int bootstrap_method_attr_index = ((0xff & buffer[1]) << 8) | (0xff & buffer[2]);
                int name_and_type_index = ((0xff & buffer[3]) << 8) | (0xff & buffer[4]);
                if (tag == 17) {
                    CONSTANT_Dynamic constant_dynamic = new CONSTANT_Dynamic(ctag, bootstrap_method_attr_index, name_and_type_index);
                    Constant constant = new Constant(constant_dynamic);
                    constantList.add(constant);
                }
                if (tag == 18) {
                    CONSTANT_InvokeDynamic constant_invokeDynamic = new CONSTANT_InvokeDynamic(ctag, bootstrap_method_attr_index, name_and_type_index);
                    Constant constant = new Constant(constant_invokeDynamic);
                    constantList.add(constant);
                }
            }
            if (tag == 19 || tag == 20) {
                buffer = new byte[3];
                System.arraycopy(cp_info, byte_code_ptr, buffer, 0, buffer.length);
                byte_code_ptr += buffer.length;
                int ctag = (0xff & buffer[0]);
                int name_index = ((0xff & buffer[1]) << 8) | (0xff & buffer[2]);
                if (tag == 19) {
                    CONSTANT_Module constant_module = new CONSTANT_Module(ctag, name_index);
                    Constant constant = new Constant(constant_module);
                    constantList.add(constant);
                }
                if (tag == 20) {
                    CONSTANT_Package constant_package = new CONSTANT_Package(ctag, name_index);
                    Constant constant = new Constant(constant_package);
                    constantList.add(constant);
                }
            }

        }
    }

    @Getter
    public static class Constant {
        private final Class<?> clazz;
        private final Object value;

        public Constant(Object value) {
            this.clazz = value.getClass();
            this.value = value;
        }

    }

    public static class CONSTANT_longNumericContinued {
    }

    /**
     * tag 7
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_Class {
        private final int tag;
        private final int name_index;
    }

    /**
     * tag 9
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_Fieldref {
        private final int tag;
        private final int class_index;
        private final int name_and_type_index;
    }

    /**
     * tag 10
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_Methodref {
        private final int tag;
        private final int class_index;
        private final int name_and_type_index;
    }

    /**
     * tag 11
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_InterfaceMethodref {

        private final int tag;
        private final int class_index;
        private final int name_and_type_index;

    }

    /**
     * tag 8
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_String {
        private final int tag;
        private final int string_index;
    }

    /**
     * tag 3
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_Integer {
        private final int tag;
        private final int value;
    }

    /**
     * tag 4
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_Float {
        private final int tag;
        private final float value;
    }

    /**
     * tag 5
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_Long {
        private final int tag;
        private final long value;
    }

    /**
     * tag 6
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_Double {
        private final int tag;
        private final double value;
    }

    /**
     * tag 12
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_NameAndType {
        private final int tag;
        private final int name_index;
        private final int descriptor_index;
    }

    /**
     * tag 1
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_Utf8 {
        private final int tag;
        private final int length;
        private final String value;
    }

    /**
     * tag 15
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_MethodHandle {
        private final int tag;
        private final int reference_kind;
        private final int reference_index;
    }

    /**
     * tag 16
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_MethodType {
        private final int tag;
        private final int descriptor_index;
    }

    /**
     * tag 17
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_Dynamic {
        private final int tag;
        private final int bootstrap_method_attr_index;
        private final int name_and_type_index;
    }

    /**
     * tag 18
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_InvokeDynamic {
        private final int tag;
        private final int bootstrap_method_attr_index;
        private final int name_and_type_index;
    }

    /**
     * tag 19
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_Module {
        private final int tag;
        private final int name_index;
    }

    /**
     * tag 20
     */
    @Getter
    @RequiredArgsConstructor
    public static class CONSTANT_Package {
        private final int tag;
        private final int name_index;
    }

}
