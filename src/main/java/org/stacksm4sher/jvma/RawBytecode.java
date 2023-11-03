package org.stacksm4sher.jvma;

import lombok.Getter;

@Getter
public class RawBytecode {

    private int byte_code_ptr = 0;

    private final byte[] magic_u4 = new byte[4];
    private final byte[] minor_version_u2 = new byte[2];
    private final byte[] major_version_u2 = new byte[2];
    private final byte[] constant_pool_count = new byte[2];
    private byte[] cp_info = new byte[0];
    private final byte[] access_flags = new byte[2];
    private final byte[] this_class = new byte[2];
    private final byte[] super_class = new byte[2];
    private final byte[] interfaces_count = new byte[2];
    private byte[] interfaces = new byte[0];
    private final byte[] fields_count = new byte[2];
    private byte[] fields = new byte[0];
    private final byte[] methods_count = new byte[2];
    private byte[] methods = new byte[0];
    private final byte[] attributes_count = new byte[2];
    private byte[] attributes = new byte[0];

    private final byte[] bytecode;

    public RawBytecode(byte[] bytecode) {
        this.bytecode = bytecode;

        // 4 insturctions
        this.setMagic_u4();
        this.setMinor_version_u2();
        this.setMajor_version_u2();
        this.setConstant_pool_count();
        this.setCp_info();
        this.setAccess_flags();
        this.setThis_class();
        this.setSuper_class();
        this.setInterfaces_count();
        this.setInterfaces();
        this.setFields_count();
        this.setFields();
        this.setMethods_count();
        this.setMethods();
        this.setAttributes_count();
        this.setAttributes();
    }

    private void setMagic_u4() {
        System.arraycopy(bytecode, 0, magic_u4, 0, magic_u4.length);
        byte_code_ptr += magic_u4.length;
    }

    private void setMinor_version_u2() {
        System.arraycopy(bytecode, byte_code_ptr, minor_version_u2, 0, minor_version_u2.length);
        byte_code_ptr += minor_version_u2.length;
    }

    private void setMajor_version_u2() {
        System.arraycopy(bytecode, byte_code_ptr, major_version_u2, 0, major_version_u2.length);
        byte_code_ptr += major_version_u2.length;
    }

    private void setConstant_pool_count() {
        System.arraycopy(bytecode, byte_code_ptr, constant_pool_count, 0, constant_pool_count.length);
        byte_code_ptr += constant_pool_count.length;
    }

    private void setCp_info() {
        // 0000 0001 0111 1110 - awaited
        // 0000 0001 0111 1110 - current
        // 0000 0001 0111 0111 - awaited by jvm
        short constant_pool_size = (short) (((0xff & constant_pool_count[0]) << 8) | (0xff & constant_pool_count[1]));
        for (int i = 0; i < constant_pool_size; ++i) {
            byte tag = bytecode[byte_code_ptr];
            if (tag == 16 || tag == 7 || tag == 8 || tag == 19 || tag == 20) {
                byte[] buffer = new byte[3];
                System.arraycopy(bytecode, byte_code_ptr, buffer, 0, buffer.length);
                cp_info = copyArray(cp_info, buffer);
                byte_code_ptr += buffer.length;
            }
            if (tag == 3 || tag == 4 || tag == 9 || tag == 10 || tag == 11 || tag == 12 || tag == 17 || tag == 18) {
                byte[] buffer = new byte[5];
                System.arraycopy(bytecode, byte_code_ptr, buffer, 0, buffer.length);
                cp_info = copyArray(cp_info, buffer);
                byte_code_ptr += buffer.length;
            }
            if (tag == 5 || tag == 6) {
                byte[] buffer = new byte[9];
                System.arraycopy(bytecode, byte_code_ptr, buffer, 0, buffer.length);
                cp_info = copyArray(cp_info, buffer);
                byte_code_ptr += buffer.length;
                i++;
            }
            if (tag == 1) {
                // 1010 1100
                short length = (short) (((0xff & bytecode[byte_code_ptr + 1]) << 8) | (0xff & bytecode[byte_code_ptr + 2]));
                byte[] buffer = new byte[3 + length];
                System.arraycopy(bytecode, byte_code_ptr, buffer, 0, buffer.length);
                cp_info = copyArray(cp_info, buffer);
                byte_code_ptr += buffer.length;
            }
            if (tag == 15) {
                byte[] buffer = new byte[4];
                System.arraycopy(bytecode, byte_code_ptr, buffer, 0, buffer.length);
                cp_info = copyArray(cp_info, buffer);
                byte_code_ptr += buffer.length;
            }
        }
    }

    private void setAccess_flags() {
        System.arraycopy(bytecode, byte_code_ptr, access_flags, 0, access_flags.length);
        byte_code_ptr += access_flags.length;
    }

    private void setThis_class() {
        System.arraycopy(bytecode, byte_code_ptr, this_class, 0, this_class.length);
        byte_code_ptr += this_class.length;
    }

    private void setSuper_class() {
        System.arraycopy(bytecode, byte_code_ptr, super_class, 0, super_class.length);
        byte_code_ptr += super_class.length;
    }

    private void setInterfaces_count() {
        System.arraycopy(bytecode, byte_code_ptr, interfaces_count, 0, interfaces_count.length);
        byte_code_ptr += interfaces_count.length;
    }

    private void setInterfaces() {
        int byteArraySize = (((0xff & interfaces_count[0]) << 8) | (0xff & interfaces_count[1])) * 2;
        interfaces = new byte[byteArraySize];
        System.arraycopy(bytecode, byte_code_ptr, interfaces, 0, interfaces.length);
        byte_code_ptr += interfaces.length;
    }

    private void setFields_count() {
        System.arraycopy(bytecode, byte_code_ptr, fields_count, 0, fields_count.length);
        byte_code_ptr += fields_count.length;
    }

    private void setFields() {
        short field_info_size = (short) (((0xff & fields_count[0]) << 8) | (0xff & fields_count[1]));
        for (short i = 0; i < field_info_size; ++i) {
            byte[] field_access_flags = new byte[2];
            byte[] field_name_index = new byte[2];
            byte[] field_descriptor_index = new byte[2];
            byte[] field_attributes_count = new byte[2];
            byte[] field_attributes = new byte[0];

            System.arraycopy(bytecode, byte_code_ptr, field_access_flags, 0, field_access_flags.length);
            byte_code_ptr += field_access_flags.length;
            System.arraycopy(bytecode, byte_code_ptr, field_name_index, 0, field_name_index.length);
            byte_code_ptr += field_access_flags.length;
            System.arraycopy(bytecode, byte_code_ptr, field_descriptor_index, 0, field_descriptor_index.length);
            byte_code_ptr += field_descriptor_index.length;
            System.arraycopy(bytecode, byte_code_ptr, field_attributes_count, 0, field_attributes_count.length);
            byte_code_ptr += field_attributes_count.length;

            short field_attributes_size = (short) (((0xff & field_attributes_count[0]) << 8) | (0xff & field_attributes_count[1]));
            for (short j = 0; j < field_attributes_size; ++j) {
                byte[] attribute_name_index = new byte[2];
                byte[] attribute_length = new byte[4];
                byte[] attribute_info;

                System.arraycopy(bytecode, byte_code_ptr, attribute_name_index, 0, attribute_name_index.length);
                byte_code_ptr += attribute_name_index.length;
                System.arraycopy(bytecode, byte_code_ptr, attribute_length, 0, attribute_length.length);
                byte_code_ptr += attribute_length.length;

                attribute_info = new byte[(((
                        (0xff & attribute_length[0]) << 24)
                        | (0xff & attribute_length[1]) << 16)
                        | (0xff & attribute_length[2]) << 8)
                        | (0xff & attribute_length[3])
                        ];
                System.arraycopy(bytecode, byte_code_ptr, attribute_info, 0, attribute_info.length);
                byte_code_ptr += attribute_info.length;

                byte[] buffer = new byte[attribute_name_index.length + attribute_length.length + attribute_info.length];
                int buffer_ptr = 0;

                System.arraycopy(attribute_name_index, 0, buffer, buffer_ptr, attribute_name_index.length);
                buffer_ptr += attribute_name_index.length;
                System.arraycopy(attribute_length, 0, buffer, buffer_ptr, attribute_length.length);
                buffer_ptr += attribute_length.length;
                System.arraycopy(attribute_info, 0, buffer, buffer_ptr, attribute_info.length);

                field_attributes = copyArray(field_attributes, buffer);
            }

            byte[] buffer = new byte[field_access_flags.length
                    + field_name_index.length
                    + field_descriptor_index.length
                    + field_attributes_count.length
                    + field_attributes.length];
            int buffer_ptr = 0;

            System.arraycopy(field_access_flags, 0, buffer, buffer_ptr, field_access_flags.length);
            buffer_ptr += field_access_flags.length;
            System.arraycopy(field_name_index, 0, buffer, buffer_ptr, field_name_index.length);
            buffer_ptr += field_name_index.length;
            System.arraycopy(field_descriptor_index, 0, buffer, buffer_ptr, field_descriptor_index.length);
            buffer_ptr += field_descriptor_index.length;
            System.arraycopy(field_attributes_count, 0, buffer, buffer_ptr, field_attributes_count.length);
            buffer_ptr += field_attributes_count.length;
            System.arraycopy(field_attributes, 0, buffer, buffer_ptr, field_attributes.length);
            fields = copyArray(fields, buffer);
        }
    }

    private void setMethods_count() {
        System.arraycopy(bytecode, byte_code_ptr, methods_count, 0, methods_count.length);
        byte_code_ptr += methods_count.length;
    }

    private void setMethods() {
        short method_info_size = (short) (((0xff & methods_count[0]) << 8) | (0xff & methods_count[1]));
        for (short i = 0; i < method_info_size; ++i) {
            byte[] method_access_flags = new byte[2];
            byte[] method_name_index = new byte[2];
            byte[] method_descriptor_index = new byte[2];
            byte[] method_attributes_count = new byte[2];
            byte[] method_attributes = new byte[0];

            System.arraycopy(bytecode, byte_code_ptr, method_access_flags, 0, method_access_flags.length);
            byte_code_ptr += method_access_flags.length;
            System.arraycopy(bytecode, byte_code_ptr, method_name_index, 0, method_name_index.length);
            byte_code_ptr += method_access_flags.length;
            System.arraycopy(bytecode, byte_code_ptr, method_descriptor_index, 0, method_descriptor_index.length);
            byte_code_ptr += method_descriptor_index.length;
            System.arraycopy(bytecode, byte_code_ptr, method_attributes_count, 0, method_attributes_count.length);
            byte_code_ptr += method_attributes_count.length;

            short method_attributes_size = (short) (((0xff & method_attributes_count[0]) << 8) | (0xff & method_attributes_count[1]));
            for (short j = 0; j < method_attributes_size; ++j) {
                byte[] attribute_name_index = new byte[2];
                byte[] attribute_length = new byte[4];
                byte[] attribute_info;

                System.arraycopy(bytecode, byte_code_ptr, attribute_name_index, 0, attribute_name_index.length);
                byte_code_ptr += attribute_name_index.length;
                System.arraycopy(bytecode, byte_code_ptr, attribute_length, 0, attribute_length.length);
                byte_code_ptr += attribute_length.length;

                attribute_info = new byte[(int) ((((
                        (0xff & attribute_length[0]) << 24)
                        | (0xff & attribute_length[1]) << 16)
                        | (0xff & attribute_length[2]) << 8)
                        | (0xff & attribute_length[3])
                )];
                System.arraycopy(bytecode, byte_code_ptr, attribute_info, 0, attribute_info.length);
                byte_code_ptr += attribute_info.length;

                byte[] buffer = new byte[attribute_name_index.length + attribute_length.length + attribute_info.length];
                int buffer_ptr = 0;

                System.arraycopy(attribute_name_index, 0, buffer, buffer_ptr, attribute_name_index.length);
                buffer_ptr += attribute_name_index.length;
                System.arraycopy(attribute_length, 0, buffer, buffer_ptr, attribute_length.length);
                buffer_ptr += attribute_length.length;
                System.arraycopy(attribute_info, 0, buffer, buffer_ptr, attribute_info.length);

                method_attributes = copyArray(method_attributes, buffer);
            }

            byte[] buffer = new byte[method_access_flags.length
                    + method_name_index.length
                    + method_descriptor_index.length
                    + method_attributes_count.length
                    + method_attributes.length];
            int buffer_ptr = 0;

            System.arraycopy(method_access_flags, 0, buffer, buffer_ptr, method_access_flags.length);
            buffer_ptr += method_access_flags.length;
            System.arraycopy(method_name_index, 0, buffer, buffer_ptr, method_name_index.length);
            buffer_ptr += method_name_index.length;
            System.arraycopy(method_descriptor_index, 0, buffer, buffer_ptr, method_descriptor_index.length);
            buffer_ptr += method_descriptor_index.length;
            System.arraycopy(method_attributes_count, 0, buffer, buffer_ptr, method_attributes_count.length);
            buffer_ptr += method_attributes_count.length;
            System.arraycopy(method_attributes, 0, buffer, buffer_ptr, method_attributes.length);
            methods = copyArray(methods, buffer);
        }
    }

    private void setAttributes_count() {
        System.arraycopy(bytecode, byte_code_ptr, attributes_count, 0, attributes_count.length);
        byte_code_ptr += attributes_count.length;
    }

    private void setAttributes() {
        short attributes_size = (short) (((0xff & attributes_count[0]) << 8) | (0xff & attributes_count[1]));
        for (short j = 0; j < attributes_size; ++j) {
            byte[] attribute_name_index = new byte[2];
            byte[] attribute_length = new byte[4];
            byte[] attribute_info;

            System.arraycopy(bytecode, byte_code_ptr, attribute_name_index, 0, attribute_name_index.length);
            byte_code_ptr += attribute_name_index.length;
            System.arraycopy(bytecode, byte_code_ptr, attribute_length, 0, attribute_length.length);
            byte_code_ptr += attribute_length.length;

            attribute_info = new byte[(((
                    (0xff & attribute_length[0]) << 24)
                    | (0xff & attribute_length[1]) << 16)
                    | (0xff & attribute_length[2]) << 8)
                    | (0xff & attribute_length[3])
                    ];
            System.arraycopy(bytecode, byte_code_ptr, attribute_info, 0, attribute_info.length);
            byte_code_ptr += attribute_info.length;

            byte[] buffer = new byte[attribute_name_index.length + attribute_length.length + attribute_info.length];
            int buffer_ptr = 0;

            System.arraycopy(attribute_name_index, 0, buffer, buffer_ptr, attribute_name_index.length);
            buffer_ptr += attribute_name_index.length;
            System.arraycopy(attribute_length, 0, buffer, buffer_ptr, attribute_length.length);
            buffer_ptr += attribute_length.length;
            System.arraycopy(attribute_info, 0, buffer, buffer_ptr, attribute_info.length);

            attributes = copyArray(attributes, buffer);
        }
    }

    private static byte[] copyArray(byte[] to, byte[] from) {
        byte[] old_to = to;
        to = new byte[old_to.length + from.length];

        System.arraycopy(old_to, 0, to, 0, old_to.length);
        System.arraycopy(from, 0, to, old_to.length, from.length);

        return to;
    }
}
