package org.stacksm4sher.jvma;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.stacksm4sher.jvma.ConstantPoolInfo.CONSTANT_Utf8;

public class MethodInfo {

    private int length = -1;
    private final List<Method> methodsList;
    private final RawBytecode rawBytecode;
    private final ConstantPoolInfo constantPoolInfo;

    public MethodInfo(RawBytecode rawBytecode, ConstantPoolInfo constantPoolInfo) {
        this.methodsList = new ArrayList<>();
        this.rawBytecode = rawBytecode;
        this.constantPoolInfo = constantPoolInfo;
        this.parse();
    }

    public List<Method> getMethodsList() {
        return this.methodsList;
    }

    public Method getMethod(int index) {
        return this.methodsList.get(index);
    }

    public int getLength() {
        if (this.length == -1) {
            this.length = this.methodsList.size();
            return this.length;
        }
        return this.length;
    }

    private void parse() {
        byte[] methodsCount = rawBytecode.getMethods_count();
        byte[] methods = rawBytecode.getMethods();

        int byte_code_ptr = 0;

        short methods_count = (short) (((0xff & methodsCount[0]) << 8) | (0xff & methodsCount[1]));
        for (short i = 0; i < methods_count; ++i) {
            int access_flag = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
            byte_code_ptr += 2;
            int name_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
            byte_code_ptr += 2;
            int descriptor_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
            byte_code_ptr += 2;
            int attributes_count = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
            byte_code_ptr += 2;
            Attribute[] attributes_method = new Attribute[attributes_count];

            for (int x = 0; x < attributes_count; ++x) {
                int attribute_name_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                byte_code_ptr += 2;
                int attribute_length = ((0xff & methods[byte_code_ptr]) << 24)
                        | ((0xff & methods[byte_code_ptr + 1]) << 16)
                        | ((0xff & methods[byte_code_ptr + 2]) << 8)
                        | (0xff & methods[byte_code_ptr + 3]);
                byte_code_ptr += 4;

                CONSTANT_Utf8 constant_utf8 = (CONSTANT_Utf8) constantPoolInfo.getConstant(attribute_name_index).getValue();
                String value = constant_utf8.getValue();

                if (value.equals("Code")) {
                    int max_stack_code = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                    byte_code_ptr += 2;
                    int max_locals_code = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                    byte_code_ptr += 2;
                    int code_length_code = ((0xff & methods[byte_code_ptr]) << 24)
                            | ((0xff & methods[byte_code_ptr + 1]) << 16)
                            | ((0xff & methods[byte_code_ptr + 2]) << 8)
                            | (0xff & methods[byte_code_ptr + 3]);
                    byte_code_ptr += 4;
                    byte[] code = new byte[code_length_code];
                    System.arraycopy(methods, byte_code_ptr, code, 0, code.length);
                    byte_code_ptr += code.length;
                    int exception_table_length = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                    byte_code_ptr += 2;
                    Code.ExceptionTable[] exception_table = new Code.ExceptionTable[exception_table_length];
                    for (int j = 0; j < exception_table_length; ++j) {
                        int start_pc = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        int end_pc = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        int handler_pc = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        int catch_type = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        Code.ExceptionTable exceptionTable = new Code.ExceptionTable(start_pc, end_pc, handler_pc, catch_type);
                        exception_table[j] = exceptionTable;
                    }
                    int attributes_count_code = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                    byte_code_ptr += 2;
                    Attribute[] attributes = new Attribute[attributes_count_code];
                    for (int j = 0; j < attributes_count_code; ++j) {
                        int attribute_name_index_code = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        int attribute_length_code = ((0xff & methods[byte_code_ptr]) << 24)
                                | ((0xff & methods[byte_code_ptr + 1]) << 16)
                                | ((0xff & methods[byte_code_ptr + 2]) << 8)
                                | (0xff & methods[byte_code_ptr + 3]);
                        byte_code_ptr += 4;
                        CONSTANT_Utf8 constant_utf8_code = (CONSTANT_Utf8) constantPoolInfo.getConstant(attribute_name_index_code).getValue();
                        String value_code = constant_utf8_code.getValue();
                        if (value_code.equals("LineNumberTable")) {
                            int line_number_table_length = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                            byte_code_ptr += 2;
                            LineNumberTable.LineNumberTable$[] line_number_table = new LineNumberTable.LineNumberTable$[line_number_table_length];
                            for (int k = 0; k < line_number_table_length; ++k) {
                                int start_pc = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                byte_code_ptr += 2;
                                int line_number = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                byte_code_ptr += 2;
                                LineNumberTable.LineNumberTable$ lineNumberTable$ = new LineNumberTable.LineNumberTable$(start_pc, line_number);
                                line_number_table[k] = lineNumberTable$;
                            }
                            LineNumberTable lineNumberTable = new LineNumberTable(attribute_name_index_code, attribute_length_code, line_number_table_length, line_number_table);
                            attributes[j] = new Attribute(lineNumberTable);
                        }
                        if (value_code.equals("LocalVariableTable")) {
                            int local_variable_table_length = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                            byte_code_ptr += 2;
                            LocalVariableTable.LocalVariableTable$[] local_variable_table = new LocalVariableTable.LocalVariableTable$[local_variable_table_length];
                            for (int k = 0; k < local_variable_table_length; ++k) {
                                int start_pc_code = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                byte_code_ptr += 2;
                                int length_code = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                byte_code_ptr += 2;
                                int name_index_code = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                byte_code_ptr += 2;
                                int descriptor_index_code = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                byte_code_ptr += 2;
                                int index_code = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                byte_code_ptr += 2;
                                LocalVariableTable.LocalVariableTable$ localVariableTable$ = new LocalVariableTable.LocalVariableTable$(start_pc_code, length_code, name_index_code, descriptor_index_code, index_code);
                                local_variable_table[k] = localVariableTable$;
                            }
                            LocalVariableTable localVariableTable = new LocalVariableTable(attribute_name_index_code, attribute_length_code, local_variable_table_length, local_variable_table);
                            attributes[j] = new Attribute(localVariableTable);
                        }
                        if (value_code.equals("LocalVariableTypeTable")) {
                            int local_variable_type_table_length = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                            byte_code_ptr += 2;
                            LocalVariableTypeTable.LocalVariableTypeTable$[] local_variable_type_table = new LocalVariableTypeTable.LocalVariableTypeTable$[local_variable_type_table_length];
                            for (int k = 0; k < local_variable_type_table_length; ++k) {
                                int start_pc_code = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                byte_code_ptr += 2;
                                int length_code = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                byte_code_ptr += 2;
                                int name_index_code = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                byte_code_ptr += 2;
                                int signature_index_code = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                byte_code_ptr += 2;
                                int index_code = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                byte_code_ptr += 2;
                                LocalVariableTypeTable.LocalVariableTypeTable$ localVariableTable$ = new LocalVariableTypeTable.LocalVariableTypeTable$(start_pc_code, length_code, name_index_code, signature_index_code, index_code);
                                local_variable_type_table[k] = localVariableTable$;
                            }
                            LocalVariableTypeTable localVariableTypeTable = new LocalVariableTypeTable(attribute_name_index_code, attribute_length_code, local_variable_type_table_length, local_variable_type_table);
                            attributes[j] = new Attribute(localVariableTypeTable);
                        }
                        if (value_code.equals("StackMapTable")) {
                            int number_of_entries = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                            byte_code_ptr += 2;
                            StackMapTable[] stackMapTable = new StackMapTable[number_of_entries];
                            for (int k = 0; k < number_of_entries; ++k) {
                                int frameType = (0xff & methods[byte_code_ptr]);
                                ++byte_code_ptr;
                                if (frameType >= 0 && frameType < 64) {
                                    SameFrame sameFrame = new SameFrame(frameType);
                                    stackMapTable[k] = new StackMapTable(attribute_name_index_code, attribute_length_code, number_of_entries, new StackMapFrame(sameFrame));
                                }
                                if (frameType >= 64 && frameType < 128) {
                                    VerificationTypeInfo[] verificationTypeInfo = new VerificationTypeInfo[1];
                                    int tag = (0xff & methods[byte_code_ptr]);
                                    ++byte_code_ptr;
                                    int[] byte_code_ptr_ptr = new int[1];
                                    byte_code_ptr_ptr[0] = byte_code_ptr;
                                    determineVerificationTypeInfo(tag, verificationTypeInfo, methods, byte_code_ptr_ptr, 0);
                                    byte_code_ptr = byte_code_ptr_ptr[0];
                                    SameLocalsOneStackItemFrame sameLocalsOneStackItemFrame = new SameLocalsOneStackItemFrame(frameType, verificationTypeInfo);
                                    stackMapTable[k] = new StackMapTable(attribute_name_index_code, attribute_length_code, number_of_entries, new StackMapFrame(sameLocalsOneStackItemFrame));
                                }
                                if (frameType == 247) {
                                    int offset_delta = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                    byte_code_ptr += 2;
                                    VerificationTypeInfo[] verificationTypeInfo = new VerificationTypeInfo[1];
                                    int tag = (0xff & methods[byte_code_ptr]);
                                    ++byte_code_ptr;
                                    int[] byte_code_ptr_ptr = new int[1];
                                    byte_code_ptr_ptr[0] = byte_code_ptr;
                                    determineVerificationTypeInfo(tag, verificationTypeInfo, methods, byte_code_ptr_ptr, 0);
                                    byte_code_ptr = byte_code_ptr_ptr[0];
                                    SameLocalsOneStackItemFrameExtended sameLocalsOneStackItemFrameExtended = new SameLocalsOneStackItemFrameExtended(frameType, offset_delta, verificationTypeInfo);
                                    stackMapTable[k] = new StackMapTable(attribute_name_index_code, attribute_length_code, number_of_entries, new StackMapFrame(sameLocalsOneStackItemFrameExtended));
                                }
                                if (frameType >= 248 && frameType < 251) {
                                    int offset_delta = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                    byte_code_ptr += 2;
                                    ChopFrame chopFrame = new ChopFrame(frameType, offset_delta);
                                    stackMapTable[k] = new StackMapTable(attribute_name_index_code, attribute_length_code, number_of_entries, new StackMapFrame(chopFrame));
                                }
                                if (frameType == 251) {
                                    int offset_delta = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                    byte_code_ptr += 2;
                                    SameFrameExtended sameFrameExtended = new SameFrameExtended(frameType, offset_delta);
                                    stackMapTable[k] = new StackMapTable(attribute_name_index_code, attribute_length_code, number_of_entries, new StackMapFrame(sameFrameExtended));
                                }
                                if (frameType >= 252 && frameType < 255) {
                                    int offset_delta = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                    byte_code_ptr += 2;
                                    VerificationTypeInfo[] verificationTypeInfo = new VerificationTypeInfo[frameType - 251];
                                    for (int y = 0; y < verificationTypeInfo.length; ++y) {
                                        int tag = (0xff & methods[byte_code_ptr]);
                                        ++byte_code_ptr;
                                        int[] byte_code_ptr_ptr = new int[1];
                                        byte_code_ptr_ptr[0] = byte_code_ptr;
                                        determineVerificationTypeInfo(tag, verificationTypeInfo, methods, byte_code_ptr_ptr, y);
                                        byte_code_ptr = byte_code_ptr_ptr[0];
                                    }
                                    AppendFrame appendFrame = new AppendFrame(frameType, offset_delta, verificationTypeInfo);
                                    stackMapTable[k] = new StackMapTable(attribute_name_index_code, attribute_length_code, number_of_entries, new StackMapFrame(appendFrame));
                                }
                                if (frameType == 255) {
                                    int offset_delta = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                    byte_code_ptr += 2;
                                    int number_of_locals = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                    byte_code_ptr += 2;
                                    VerificationTypeInfo[] verificationTypeInfo = new VerificationTypeInfo[number_of_locals];
                                    for (int y = 0; y < verificationTypeInfo.length; ++y) {
                                        int tag = (0xff & methods[byte_code_ptr]);
                                        ++byte_code_ptr;
                                        int[] byte_code_ptr_ptr = new int[1];
                                        byte_code_ptr_ptr[0] = byte_code_ptr;
                                        determineVerificationTypeInfo(tag, verificationTypeInfo, methods, byte_code_ptr_ptr, y);
                                        byte_code_ptr = byte_code_ptr_ptr[0];
                                    }

                                    int number_of_stack_items = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                    byte_code_ptr += 2;
                                    VerificationTypeInfo[] verificationTypeInfo0 = new VerificationTypeInfo[number_of_stack_items];
                                    for (int y = 0; y < verificationTypeInfo0.length; ++y) {
                                        int tag = (0xff & methods[byte_code_ptr]);
                                        ++byte_code_ptr;
                                        int[] byte_code_ptr_ptr = new int[1];
                                        byte_code_ptr_ptr[0] = byte_code_ptr;
                                        determineVerificationTypeInfo(tag, verificationTypeInfo0, methods, byte_code_ptr_ptr, y);
                                        byte_code_ptr = byte_code_ptr_ptr[0];
                                    }
                                    FullFrame fullFrame = new FullFrame(frameType, offset_delta, number_of_locals, verificationTypeInfo, number_of_stack_items, verificationTypeInfo0);
                                    stackMapTable[k] = new StackMapTable(attribute_name_index_code, attribute_length_code, number_of_entries, new StackMapFrame(fullFrame));
                                }
                            }
                            attributes[j] = new Attribute(stackMapTable);
                        }
                    }
                    Code code_attribute = new Code(attribute_name_index, attribute_length, max_stack_code, max_locals_code, code_length_code, code, exception_table_length, exception_table, attributes_count_code, attributes);
                    attributes_method[x] = new Attribute(code_attribute);
                }
                if (value.equals("Exceptions")) {
                    int number_of_exceptions = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                    byte_code_ptr += 2;
                    int[] exception_index_table = new int[number_of_exceptions]; /*u2 array*/
                    for (int j = 0; j < number_of_exceptions; ++j) {
                        exception_index_table[j] = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                    }
                    ExceptionsTable exceptionsTable = new ExceptionsTable(attribute_name_index, attribute_length, number_of_exceptions, exception_index_table);
                    attributes_method[x] = new Attribute(exceptionsTable);
                }
                if (value.equals("RuntimeVisibleParameterAnnotations")) {
                    int num_parameters = (0xff & methods[byte_code_ptr]); /*u1*/
                    ++byte_code_ptr;
                    ParameterAnnotation[] parameterAnnotation = new ParameterAnnotation[num_parameters];
                    for (int j = 0; j < num_parameters; ++j) {
                        int num_annotation = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        Annotation[] annotations = new Annotation[num_annotation];
                        for (int k = 0; k < num_annotation; ++k) {
                            int type_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                            byte_code_ptr += 2;
                            int num_element_value_pairs = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                            byte_code_ptr += 2;
                            ElementValuePairs[] element_value_pairs = new ElementValuePairs[num_element_value_pairs];
                            for (int l = 0; l < num_element_value_pairs; ++l) {
                                int element_name_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                byte_code_ptr += 2;
                                int[] byte_code_ptr_ptr = new int[0];
                                byte_code_ptr_ptr[0] = byte_code_ptr;
                                ElementValue elementValue = recursiveElementValueParse(methods, byte_code_ptr_ptr);
                                byte_code_ptr = byte_code_ptr_ptr[0];
                                ElementValuePairs elementValuePairs = new ElementValuePairs(element_name_index, elementValue);
                                element_value_pairs[l] = elementValuePairs;
                            }
                            annotations[k] = new Annotation(type_index, num_element_value_pairs, element_value_pairs);
                        }
                        parameterAnnotation[j] = new ParameterAnnotation(num_annotation, annotations);
                    }
                    RuntimeVisibleParameterAnnotations runtimeVisibleParameterAnnotations = new RuntimeVisibleParameterAnnotations(attribute_name_index, attribute_length, num_parameters, parameterAnnotation);
                    attributes_method[x] = new Attribute(runtimeVisibleParameterAnnotations);
                }
                if (value.equals("RuntimeInvisibleParameterAnnotations")) {
                    int num_parameters = (0xff & methods[byte_code_ptr]); /*u1*/
                    ++byte_code_ptr;
                    ParameterAnnotation[] parameterAnnotation = new ParameterAnnotation[num_parameters];
                    for (int j = 0; j < num_parameters; ++j) {
                        int num_annotation = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        Annotation[] annotations = new Annotation[num_annotation];
                        for (int k = 0; k < num_annotation; ++k) {
                            int type_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                            byte_code_ptr += 2;
                            int num_element_value_pairs = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                            byte_code_ptr += 2;
                            ElementValuePairs[] element_value_pairs = new ElementValuePairs[num_element_value_pairs];
                            for (int l = 0; l < num_element_value_pairs; ++l) {
                                int element_name_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                                byte_code_ptr += 2;
                                int[] byte_code_ptr_ptr = new int[0];
                                byte_code_ptr_ptr[0] = byte_code_ptr;
                                ElementValue elementValue = recursiveElementValueParse(methods, byte_code_ptr_ptr);
                                byte_code_ptr = byte_code_ptr_ptr[0];
                                ElementValuePairs elementValuePairs = new ElementValuePairs(element_name_index, elementValue);
                                element_value_pairs[l] = elementValuePairs;
                            }
                            annotations[k] = new Annotation(type_index, num_element_value_pairs, element_value_pairs);
                        }
                        parameterAnnotation[j] = new ParameterAnnotation(num_annotation, annotations);
                    }
                    RuntimeInvisibleParameterAnnotations runtimeInvisibleParameterAnnotations = new RuntimeInvisibleParameterAnnotations(attribute_name_index, attribute_length, num_parameters, parameterAnnotation);
                    attributes_method[x] = new Attribute(runtimeInvisibleParameterAnnotations);
                }
                if (value.equals("AnnotationDefault")) {
                    int[] byte_code_ptr_ptr = new int[0];
                    byte_code_ptr_ptr[0] = byte_code_ptr;
                    ElementValue elementValue = recursiveElementValueParse(methods, byte_code_ptr_ptr);
                    byte_code_ptr = byte_code_ptr_ptr[0];
                    AnnotationDefault annotationDefault = new AnnotationDefault(attribute_name_index, attribute_length, elementValue);
                    attributes_method[x] = new Attribute(annotationDefault);
                }
                if (value.equals("MethodParameters")) {
                    int parameters_count = (0xff & methods[byte_code_ptr]);
                    ++byte_code_ptr;
                    MethodParameters.Parameters[] parameters = new MethodParameters.Parameters[parameters_count];
                    for (int j = 0; j < parameters_count; ++j) {
                        int name_index_mparam = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        int access_flag_mparam = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        MethodParameters.Parameters parameter = new MethodParameters.Parameters(name_index_mparam, access_flag_mparam);
                        parameters[j] = parameter;
                    }
                    MethodParameters methodParameters = new MethodParameters(attribute_name_index, attribute_length, parameters_count, parameters);
                    attributes_method[x] = new Attribute(methodParameters);
                }
                if (value.equals("Synthetic")) {
                    Synthetic synthetic = new Synthetic(attribute_name_index, attribute_length);
                    attributes_method[x] = new Attribute(synthetic);
                }
                if (value.equals("Deprecated")) {
                    Deprecated deprecated = new Deprecated(attribute_name_index, attribute_length);
                    attributes_method[x] = new Attribute(deprecated);
                }
                if (value.equals("Signature")) {
                    int signature_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                    byte_code_ptr += 2;
                    Signature signature = new Signature(attribute_name_index, attribute_length, signature_index);
                    attributes_method[x] = new Attribute(signature);
                }
                if (value.equals("RuntimeVisibleAnnotations")) {
                    int num_annotations = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                    byte_code_ptr += 2;
                    Annotation[] annotations = new Annotation[num_annotations];
                    for (int j = 0; j < num_annotations; ++j) {
                        int type_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        int num_element_value_pairs = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        ElementValuePairs[] element_value_pairs = new ElementValuePairs[num_element_value_pairs];
                        for (int k = 0; k < num_element_value_pairs; ++k) {
                            int element_name_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                            byte_code_ptr += 2;
                            int[] byte_code_ptr_ptr = new int[1];
                            byte_code_ptr_ptr[0] = byte_code_ptr;
                            ElementValue elementValue = recursiveElementValueParse(methods, byte_code_ptr_ptr);
                            byte_code_ptr = byte_code_ptr_ptr[0];
                            ElementValuePairs elementValuePairs = new ElementValuePairs(element_name_index, elementValue);
                            element_value_pairs[k] = elementValuePairs;
                        }
                        Annotation annotation = new Annotation(type_index, num_element_value_pairs, element_value_pairs);
                        annotations[j] = annotation;
                    }
                    RuntimeVisibleAnnotations runtimeVisibleAnnotations = new RuntimeVisibleAnnotations(attribute_name_index, attribute_length, num_annotations, annotations);
                    attributes_method[x] = new Attribute(runtimeVisibleAnnotations);
                }
                if (value.equals("RuntimeInvisibleAnnotations")) {
                    int num_annotations = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                    byte_code_ptr += 2;
                    Annotation[] annotations = new Annotation[num_annotations];
                    for (int j = 0; j < num_annotations; ++j) {
                        int type_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        int num_element_value_pairs = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        ElementValuePairs[] element_value_pairs = new ElementValuePairs[num_element_value_pairs];
                        for (int k = 0; k < num_element_value_pairs; ++k) {
                            int element_name_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                            byte_code_ptr += 2;
                            int[] byte_code_ptr_ptr = new int[1];
                            byte_code_ptr_ptr[0] = byte_code_ptr;
                            ElementValue elementValue = recursiveElementValueParse(methods, byte_code_ptr_ptr);
                            byte_code_ptr = byte_code_ptr_ptr[0];
                            ElementValuePairs elementValuePairs = new ElementValuePairs(element_name_index, elementValue);
                            element_value_pairs[k] = elementValuePairs;
                        }
                        Annotation annotation = new Annotation(type_index, num_element_value_pairs, element_value_pairs);
                        annotations[j] = annotation;
                    }
                    RuntimeInvisibleAnnotations runtimeInvisibleAnnotations = new RuntimeInvisibleAnnotations(attribute_name_index, attribute_length, num_annotations, annotations);
                    attributes_method[x] = new Attribute(runtimeInvisibleAnnotations);
                }
                if (value.equals("RuntimeVisibleTypeAnnotations")) {
                    int num_annotations = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                    byte_code_ptr += 2;
                    TypeAnnotation[] annotations = new TypeAnnotation[num_annotations];
                    for (int j = 0; j < num_annotations; ++j) {
                        int target_type = (0xff & methods[byte_code_ptr]);
                        ++byte_code_ptr;
                        TargetInfo target_info = null;
                        if (target_type == 0x01) {
                            int type_parameter_index = (0xff & methods[byte_code_ptr]);
                            ++byte_code_ptr;
                            TypeParameterTarget typeParameterTarget = new TypeParameterTarget(type_parameter_index);
                            target_info = new TargetInfo(typeParameterTarget);
                        }
                        if (target_type == 0x12) {
                            int type_parameter_index = (0xff & methods[byte_code_ptr]);
                            ++byte_code_ptr;
                            int bound_index = (0xff & methods[byte_code_ptr]);
                            ++byte_code_ptr;
                            TypeParameterBoundTarget typeParameterBoundTarget = new TypeParameterBoundTarget(type_parameter_index, bound_index);
                            target_info = new TargetInfo(typeParameterBoundTarget);
                        }
                        if (target_type == 0x14 || target_type == 0x15) {
                            EmptyTarget emptyTarget = new EmptyTarget();
                            target_info = new TargetInfo(emptyTarget);
                        }
                        if (target_type == 0x16) {
                            int formal_parameter_index = (0xff & methods[byte_code_ptr]);
                            ++byte_code_ptr;
                            FormalParameterTarget formalParameterTarget = new FormalParameterTarget(formal_parameter_index);
                            target_info = new TargetInfo(formalParameterTarget);
                        }
                        if (target_type == 0x17) {
                            int throws_type_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                            byte_code_ptr += 2;
                            ThrowsTarget throwsTarget = new ThrowsTarget(throws_type_index);
                            target_info = new TargetInfo(throwsTarget);
                        }
                        TypePath target_path;
                        int path_length = (0xff & methods[byte_code_ptr]);
                        ++byte_code_ptr;
                        TypePath.Path[] path = new TypePath.Path[path_length];
                        for (int k = 0; k < path_length; ++k) {
                            int type_path_kind = (0xff & methods[byte_code_ptr]);
                            ++byte_code_ptr;
                            int type_argument_index = (0xff & methods[byte_code_ptr]);
                            ++byte_code_ptr;
                            path[k] = new TypePath.Path(type_path_kind, type_argument_index);
                        }
                        target_path = new TypePath(path_length, path);
                        int type_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        int num_element_value_pairs = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        ElementValuePairs[] element_value_pairs = new ElementValuePairs[num_element_value_pairs];
                        for (int k = 0; k < num_element_value_pairs; ++k) {
                            int element_name_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                            byte_code_ptr += 2;
                            int[] byte_code_ptr_ptr = new int[1];
                            byte_code_ptr_ptr[0] = byte_code_ptr;
                            ElementValue elementValue = recursiveElementValueParse(methods, byte_code_ptr_ptr);
                            byte_code_ptr = byte_code_ptr_ptr[0];
                            element_value_pairs[k] = new ElementValuePairs(element_name_index, elementValue);
                        }
                        TypeAnnotation typeAnnotation = new TypeAnnotation(target_type, target_info, target_path, type_index, num_element_value_pairs, element_value_pairs);
                        annotations[j] = typeAnnotation;
                    }
                    RuntimeVisibleTypeAnnotations runtimeVisibleTypeAnnotations = new RuntimeVisibleTypeAnnotations(attribute_name_index, attribute_length, num_annotations, annotations);
                    attributes_method[x] = new Attribute(runtimeVisibleTypeAnnotations);
                }
                if (value.equals("RuntimeInvisibleTypeAnnotations")) {
                    int num_annotations = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                    byte_code_ptr += 2;
                    TypeAnnotation[] annotations = new TypeAnnotation[num_annotations];
                    for (int j = 0; j < num_annotations; ++j) {
                        int target_type = (0xff & methods[byte_code_ptr]);
                        ++byte_code_ptr;
                        TargetInfo target_info = null;
                        if (target_type == 0x01) {
                            int type_parameter_index = (0xff & methods[byte_code_ptr]);
                            ++byte_code_ptr;
                            TypeParameterTarget typeParameterTarget = new TypeParameterTarget(type_parameter_index);
                            target_info = new TargetInfo(typeParameterTarget);
                        }
                        if (target_type == 0x12) {
                            int type_parameter_index = (0xff & methods[byte_code_ptr]);
                            ++byte_code_ptr;
                            int bound_index = (0xff & methods[byte_code_ptr]);
                            ++byte_code_ptr;
                            TypeParameterBoundTarget typeParameterBoundTarget = new TypeParameterBoundTarget(type_parameter_index, bound_index);
                            target_info = new TargetInfo(typeParameterBoundTarget);
                        }
                        if (target_type == 0x14 || target_type == 0x15) {
                            EmptyTarget emptyTarget = new EmptyTarget();
                            target_info = new TargetInfo(emptyTarget);
                        }
                        if (target_type == 0x16) {
                            int formal_parameter_index = (0xff & methods[byte_code_ptr]);
                            ++byte_code_ptr;
                            FormalParameterTarget formalParameterTarget = new FormalParameterTarget(formal_parameter_index);
                            target_info = new TargetInfo(formalParameterTarget);
                        }
                        if (target_type == 0x17) {
                            int throws_type_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                            byte_code_ptr += 2;
                            ThrowsTarget throwsTarget = new ThrowsTarget(throws_type_index);
                            target_info = new TargetInfo(throwsTarget);
                        }
                        TypePath target_path;
                        int path_length = (0xff & methods[byte_code_ptr]);
                        ++byte_code_ptr;
                        TypePath.Path[] path = new TypePath.Path[path_length];
                        for (int k = 0; k < path_length; ++k) {
                            int type_path_kind = (0xff & methods[byte_code_ptr]);
                            ++byte_code_ptr;
                            int type_argument_index = (0xff & methods[byte_code_ptr]);
                            ++byte_code_ptr;
                            path[k] = new TypePath.Path(type_path_kind, type_argument_index);
                        }
                        target_path = new TypePath(path_length, path);
                        int type_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        int num_element_value_pairs = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                        byte_code_ptr += 2;
                        ElementValuePairs[] element_value_pairs = new ElementValuePairs[num_element_value_pairs];
                        for (int k = 0; k < num_element_value_pairs; ++k) {
                            int element_name_index = ((0xff & methods[byte_code_ptr]) << 8) | (0xff & methods[byte_code_ptr + 1]);
                            byte_code_ptr += 2;
                            int[] byte_code_ptr_ptr = new int[1];
                            byte_code_ptr_ptr[0] = byte_code_ptr;
                            ElementValue elementValue = recursiveElementValueParse(methods, byte_code_ptr_ptr);
                            byte_code_ptr = byte_code_ptr_ptr[0];
                            element_value_pairs[k] = new ElementValuePairs(element_name_index, elementValue);
                        }
                        TypeAnnotation typeAnnotation = new TypeAnnotation(target_type, target_info, target_path, type_index, num_element_value_pairs, element_value_pairs);
                        annotations[j] = typeAnnotation;
                    }
                    RuntimeInvisibleTypeAnnotations runtimeInvisibleTypeAnnotations = new RuntimeInvisibleTypeAnnotations(attribute_name_index, attribute_length, num_annotations, annotations);
                    attributes_method[x] = new Attribute(runtimeInvisibleTypeAnnotations);
                }
            }
            Method method = new Method(access_flag, name_index, descriptor_index, attributes_count, attributes_method);
            this.methodsList.add(method);
        }
    }

    private ElementValue recursiveElementValueParse(byte[] bytecode, int[] byte_code_ptr_ptr) {
        int byte_code_ptr = byte_code_ptr_ptr[0];

        int tag = (0xff & bytecode[byte_code_ptr]);
        ++byte_code_ptr;

        // 66, 67, 68, 70, 73, 74, 83, 90, 115 -> const_value_index
        // 101 -> enum_const_value
        // 99 -> class_info_index
        // 64 -> annotation_value
        // 91 -> array_value
        if (tag == 66 || tag == 67 || tag == 68 || tag == 70
                || tag == 73 || tag == 74 || tag == 83 || tag == 90 || tag == 115) {
            int const_value_index = (bytecode[byte_code_ptr] << 8) | bytecode[byte_code_ptr + 1];
            byte_code_ptr += 2;
            byte_code_ptr_ptr[0] = byte_code_ptr;
            ElementValue.Value value = new ElementValue.Value(const_value_index, true);
            return new ElementValue(tag, value);
        }
        if (tag == 101) {
            int type_name_index = (bytecode[byte_code_ptr] << 8) | bytecode[byte_code_ptr + 1];
            byte_code_ptr += 2;
            int const_name_index = (bytecode[byte_code_ptr] << 8) | bytecode[byte_code_ptr + 1];
            byte_code_ptr += 2;
            byte_code_ptr_ptr[0] = byte_code_ptr;
            ElementValue.Value value = new ElementValue.Value(type_name_index, const_name_index);
            return new ElementValue(tag, value);
        }
        if (tag == 99) {
            int class_info_index = (bytecode[byte_code_ptr] << 8) | bytecode[byte_code_ptr + 1];
            byte_code_ptr += 2;
            byte_code_ptr_ptr[0] = byte_code_ptr;
            ElementValue.Value value = new ElementValue.Value(class_info_index, false);
            return new ElementValue(tag, value);
        }
        if (tag == 64) {
            Annotation annotation_value;
            int type_index = (bytecode[byte_code_ptr] << 8) | bytecode[byte_code_ptr + 1];
            byte_code_ptr += 2;
            int num_element_value_pairs = (bytecode[byte_code_ptr] << 8) | bytecode[byte_code_ptr + 1];
            byte_code_ptr += 2;
            ElementValuePairs[] element_value_pairs;
            if (num_element_value_pairs > 0) {
                element_value_pairs = new ElementValuePairs[num_element_value_pairs];
                for (int i = 0; i < num_element_value_pairs; ++i) {
                    int element_name_index = (bytecode[byte_code_ptr] << 8) | bytecode[byte_code_ptr + 1];
                    byte_code_ptr += 2;
                    byte_code_ptr_ptr[0] = byte_code_ptr;
                    ElementValue elementValue = recursiveElementValueParse(bytecode, byte_code_ptr_ptr);
                    byte_code_ptr = byte_code_ptr_ptr[0];
                    element_value_pairs[i] = new ElementValuePairs(element_name_index, elementValue);
                }
            } else {
                element_value_pairs = null;
            }
            byte_code_ptr_ptr[0] = byte_code_ptr;
            annotation_value = new Annotation(type_index, num_element_value_pairs, element_value_pairs);
            ElementValue.Value value = new ElementValue.Value(annotation_value);
            return new ElementValue(tag, value);
        }
        if (tag == 91) {
            int num_values = (bytecode[byte_code_ptr] << 8) | bytecode[byte_code_ptr + 1];
            byte_code_ptr += 2;
            ElementValue[] values;
            if (num_values > 0) {
                values = new ElementValue[num_values];
                for (int i = 0; i < num_values; ++i) {
                    byte_code_ptr_ptr[0] = byte_code_ptr;
                    ElementValue elementValue = recursiveElementValueParse(bytecode, byte_code_ptr_ptr);
                    byte_code_ptr = byte_code_ptr_ptr[0];
                    values[i] = elementValue;
                }
            } else {
                values = null;
            }
            byte_code_ptr_ptr[0] = byte_code_ptr;
            ElementValue.Value value = new ElementValue.Value(num_values, values);
            return new ElementValue(tag, value);
        }
        return null;
    }

    private void determineVerificationTypeInfo(int tag, VerificationTypeInfo[] verificationTypeInfo_ptr, byte[] method_bytecode, int[] byte_code_ptr_ptr, int indexToSet) {
        VerificationTypeInfo verificationTypeInfo0;
        if (tag == 0) {
            Top_variable_info topVariableInfo = new Top_variable_info();
            verificationTypeInfo0 = new VerificationTypeInfo(topVariableInfo);
            verificationTypeInfo_ptr[indexToSet] = verificationTypeInfo0;
        }
        if (tag == 1) {
            Integer_variable_info integerVariableInfo = new Integer_variable_info();
            verificationTypeInfo0 = new VerificationTypeInfo(integerVariableInfo);
            verificationTypeInfo_ptr[indexToSet] = verificationTypeInfo0;
        }
        if (tag == 2) {
            Float_variable_info floatVariableInfo = new Float_variable_info();
            verificationTypeInfo0 = new VerificationTypeInfo(floatVariableInfo);
            verificationTypeInfo_ptr[indexToSet] = verificationTypeInfo0;
        }
        if (tag == 3) {
            Double_variable_info doubleVariableInfo = new Double_variable_info();
            verificationTypeInfo0 = new VerificationTypeInfo(doubleVariableInfo);
            verificationTypeInfo_ptr[indexToSet] = verificationTypeInfo0;
        }
        if (tag == 4) {
            Long_variable_info longVariableInfo = new Long_variable_info();
            verificationTypeInfo0 = new VerificationTypeInfo(longVariableInfo);
            verificationTypeInfo_ptr[indexToSet] = verificationTypeInfo0;
        }
        if (tag == 5) {
            Null_variable_info nullVariableInfo = new Null_variable_info();
            verificationTypeInfo0 = new VerificationTypeInfo(nullVariableInfo);
            verificationTypeInfo_ptr[indexToSet] = verificationTypeInfo0;
        }
        if (tag == 6) {
            UninitializedThis_variable_info uninitializedThisVariableInfo = new UninitializedThis_variable_info();
            verificationTypeInfo0 = new VerificationTypeInfo(uninitializedThisVariableInfo);
            verificationTypeInfo_ptr[indexToSet] = verificationTypeInfo0;
        }
        if (tag == 7) {
            int cpool_index = (method_bytecode[byte_code_ptr_ptr[0]] << 8) | method_bytecode[byte_code_ptr_ptr[0] + 1];
            byte_code_ptr_ptr[0] += 2;
            Object_variable_info objectVariableInfo = new Object_variable_info(cpool_index);
            verificationTypeInfo0 = new VerificationTypeInfo(objectVariableInfo);
            verificationTypeInfo_ptr[indexToSet] = verificationTypeInfo0;
        }
        if (tag == 8) {
            int offset = (method_bytecode[byte_code_ptr_ptr[0]] << 8) | method_bytecode[byte_code_ptr_ptr[0] + 1];
            byte_code_ptr_ptr[0] += 2;
            Uninitialized_variable_info uninitializedVariableInfo = new Uninitialized_variable_info(offset);
            verificationTypeInfo0 = new VerificationTypeInfo(uninitializedVariableInfo);
            verificationTypeInfo_ptr[indexToSet] = verificationTypeInfo0;
        }
    }

    @Getter
    public static class Method {
        private final int access_flags; /*u2*/
        private final int name_index; /*u2*/
        private final int descriptor_index; /*u2*/
        private final int attributes_count; /*u2*/
        private final Attribute[] attributes;

        public Method(int accessFlags, int nameIndex, int descriptorIndex, int attributesCount, Attribute[] attributes) {
            access_flags = accessFlags;
            name_index = nameIndex;
            descriptor_index = descriptorIndex;
            attributes_count = attributesCount;
            this.attributes = attributes;
        }
    }


    @Getter
    public static class Code {
        private final int attribute_name_index;/*u2*/
        private final int attribute_length;/*u4*/
        private final int max_stack; /*u2*/
        private final int max_locals; /*u2*/
        private final int code_length; /*u4*/
        private final byte[] code; /*u1*/
        private final int exception_table_length; /*u2*/

        private final ExceptionTable[] exception_table;
        private final int attributes_count; /*u2*/
        private final Attribute[] attributes;

        public Code(int attributeNameIndex,
                    int attributeLength,
                    int maxStack,
                    int maxLocals,
                    int codeLength,
                    byte[] code,
                    int exceptionTableLength,
                    ExceptionTable[] exceptionTable,
                    int attributesCount,
                    Attribute[] attributes) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            max_stack = maxStack;
            max_locals = maxLocals;
            code_length = codeLength;
            this.code = code;
            exception_table_length = exceptionTableLength;
            exception_table = exceptionTable;
            attributes_count = attributesCount;
            this.attributes = attributes;
        }

        @Getter
        public static class ExceptionTable {
            private final int start_pc; /*u2*/
            private final int end_pc; /*u2*/
            private final int handler_pc; /*u2*/
            private final int catch_type; /*u2*/

            public ExceptionTable(int startPc, int endPc, int handlerPc, int catchType) {
                start_pc = startPc;
                end_pc = endPc;
                handler_pc = handlerPc;
                catch_type = catchType;
            }
        }
    }

    @Getter
    public static class Attribute {
        private final Class<?> clazz;
        private final Object value;

        public Attribute(Object value) {
            this.clazz = value.getClass();
            this.value = value;
        }
    }

    @Getter
    public static class MethodParameters {
        private final int attribute_name_index;/*u2*/
        private final int attribute_length;/*u4*/
        private final int parameters_count; /*u1*/
        private final Parameters[] parameters;

        public MethodParameters(int attributeNameIndex, int attributeLength, int parametersCount, Parameters[] parameters) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            parameters_count = parametersCount;
            this.parameters = parameters;
        }

        @Getter
        public static class Parameters {
            private final int name_index; /*u2*/
            private final int access_flag; /*u2*/

            public Parameters(int nameIndex, int accessFlag) {
                name_index = nameIndex;
                access_flag = accessFlag;
            }
        }
    }

    @Getter
    public static class Signature {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/
        private final int signature_index; /*u2*/

        public Signature(int attributeNameIndex, int attributeLength, int signatureIndex) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            signature_index = signatureIndex;
        }
    }

    @Getter
    public static class ExceptionsTable {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/
        private final int number_of_exceptions; /*u2*/
        private final int[] exception_index_table; /*u2 array*/

        public ExceptionsTable(int attributeNameIndex, int attributeLength, int numberOfExceptions, int[] exceptionIndexTable) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            number_of_exceptions = numberOfExceptions;
            exception_index_table = exceptionIndexTable;
        }
    }

    @Getter
    public static class RuntimeVisibleAnnotations {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/
        private final int num_annotations; /*u2*/
        private final Annotation[] annotations;

        public RuntimeVisibleAnnotations(int attributeNameIndex, int attributeLength, int numAnnotations, Annotation[] annotations) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            num_annotations = numAnnotations;
            this.annotations = annotations;
        }
    }

    @Getter
    public static class RuntimeInvisibleAnnotations {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/
        private final int num_annotations; /*u2*/
        private final Annotation[] annotations;

        public RuntimeInvisibleAnnotations(int attributeNameIndex, int attributeLength, int numAnnotations, Annotation[] annotations) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            num_annotations = numAnnotations;
            this.annotations = annotations;
        }
    }

    @Getter
    public static class RuntimeVisibleTypeAnnotations {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/
        private final int num_annotations; /*u2*/
        private final TypeAnnotation[] annotations;


        public RuntimeVisibleTypeAnnotations(int attributeNameIndex, int attributeLength, int numAnnotations, TypeAnnotation[] annotations) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            num_annotations = numAnnotations;
            this.annotations = annotations;
        }
    }

    @Getter
    public static class RuntimeInvisibleTypeAnnotations {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/
        private final int num_annotations; /*u2*/
        private final TypeAnnotation[] annotations;


        public RuntimeInvisibleTypeAnnotations(int attributeNameIndex, int attributeLength, int numAnnotations, TypeAnnotation[] annotations) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            num_annotations = numAnnotations;
            this.annotations = annotations;
        }
    }

    @Getter
    public static class RuntimeInvisibleParameterAnnotations {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/
        private final int num_parameters; /*u1*/
        private final ParameterAnnotation[] parameterAnnotation;

        public RuntimeInvisibleParameterAnnotations(int attributeNameIndex, int attributeLength, int numParameters, ParameterAnnotation[] parameterAnnotation) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            num_parameters = numParameters;
            this.parameterAnnotation = parameterAnnotation;
        }
    }

    @Getter
    public static class RuntimeVisibleParameterAnnotations {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/
        private final int num_parameters; /*u1*/
        private final ParameterAnnotation[] parameterAnnotation;

        public RuntimeVisibleParameterAnnotations(int attributeNameIndex, int attributeLength, int numParameters, ParameterAnnotation[] parameterAnnotation) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            num_parameters = numParameters;
            this.parameterAnnotation = parameterAnnotation;
        }
    }

    @Getter
    public static class TypeAnnotation {
        private final int target_type; /*u1*/
        private final TargetInfo target_info;
        private final TypePath target_path;
        private final int type_index; /*u2*/
        private final int num_element_value_pairs; /*u2*/
        private final ElementValuePairs[] element_value_pairs;

        public TypeAnnotation(int targetType, TargetInfo targetInfo, TypePath targetPath, int typeIndex, int numElementValuePairs, ElementValuePairs[] elementValuePairs) {
            target_type = targetType;
            target_info = targetInfo;
            target_path = targetPath;
            type_index = typeIndex;
            num_element_value_pairs = numElementValuePairs;
            element_value_pairs = elementValuePairs;
        }
    }

    @Getter
    public static class TypePath {
        private final int path_length; /*u1*/
        private final Path[] path;

        public TypePath(int pathLength, Path[] path) {
            path_length = pathLength;
            this.path = path;
        }

        @Getter
        public static class Path {
            private final int type_path_kind; /*u1*/
            private final int type_argument_index; /*u1*/

            public Path(int typePathKind, int typeArgumentIndex) {
                type_path_kind = typePathKind;
                type_argument_index = typeArgumentIndex;
            }
        }
    }

    @Getter
    public static class TargetInfo {
        private final TypeParameterTarget type_parameter_target;
        private final SupertypeTarget supertype_target;
        private final TypeParameterBoundTarget type_parameter_bound_target;
        private final EmptyTarget empty_target;
        private final FormalParameterTarget formal_parameter_target;
        private final ThrowsTarget throws_target;
        private final LocalvarTarget localvar_target;
        private final CatchTarget catch_target;
        private final OffsetTarget offset_target;
        private final TypeArgumentTarget type_argument_target;

        public TargetInfo(TypeParameterTarget type_parameter_target) {
            this.type_parameter_target = type_parameter_target;
            this.supertype_target = null;
            this.type_parameter_bound_target = null;
            this.empty_target = null;
            this.formal_parameter_target = null;
            this.throws_target = null;
            this.localvar_target = null;
            this.catch_target = null;
            this.offset_target = null;
            this.type_argument_target = null;
        }

        public TargetInfo(SupertypeTarget supertype_target) {
            this.type_parameter_target = null;
            this.supertype_target = supertype_target;
            this.type_parameter_bound_target = null;
            this.empty_target = null;
            this.formal_parameter_target = null;
            this.throws_target = null;
            this.localvar_target = null;
            this.catch_target = null;
            this.offset_target = null;
            this.type_argument_target = null;
        }

        public TargetInfo(TypeParameterBoundTarget type_parameter_bound_target) {
            this.type_parameter_target = null;
            this.supertype_target = null;
            this.type_parameter_bound_target = type_parameter_bound_target;
            this.empty_target = null;
            this.formal_parameter_target = null;
            this.throws_target = null;
            this.localvar_target = null;
            this.catch_target = null;
            this.offset_target = null;
            this.type_argument_target = null;
        }

        public TargetInfo(EmptyTarget empty_target) {
            this.type_parameter_target = null;
            this.supertype_target = null;
            this.type_parameter_bound_target = null;
            this.empty_target = empty_target;
            this.formal_parameter_target = null;
            this.throws_target = null;
            this.localvar_target = null;
            this.catch_target = null;
            this.offset_target = null;
            this.type_argument_target = null;
        }

        public TargetInfo(FormalParameterTarget formal_parameter_target) {
            this.type_parameter_target = null;
            this.supertype_target = null;
            this.type_parameter_bound_target = null;
            this.empty_target = null;
            this.formal_parameter_target = formal_parameter_target;
            this.throws_target = null;
            this.localvar_target = null;
            this.catch_target = null;
            this.offset_target = null;
            this.type_argument_target = null;
        }

        public TargetInfo(ThrowsTarget throws_target) {
            this.type_parameter_target = null;
            this.supertype_target = null;
            this.type_parameter_bound_target = null;
            this.empty_target = null;
            this.formal_parameter_target = null;
            this.throws_target = throws_target;
            this.localvar_target = null;
            this.catch_target = null;
            this.offset_target = null;
            this.type_argument_target = null;
        }

        public TargetInfo(LocalvarTarget localvar_target) {
            this.type_parameter_target = null;
            this.supertype_target = null;
            this.type_parameter_bound_target = null;
            this.empty_target = null;
            this.formal_parameter_target = null;
            this.throws_target = null;
            this.localvar_target = localvar_target;
            this.catch_target = null;
            this.offset_target = null;
            this.type_argument_target = null;
        }

        public TargetInfo(CatchTarget catch_target) {
            this.type_parameter_target = null;
            this.supertype_target = null;
            this.type_parameter_bound_target = null;
            this.empty_target = null;
            this.formal_parameter_target = null;
            this.throws_target = null;
            this.localvar_target = null;
            this.catch_target = catch_target;
            this.offset_target = null;
            this.type_argument_target = null;
        }

        public TargetInfo(OffsetTarget offset_target) {
            this.type_parameter_target = null;
            this.supertype_target = null;
            this.type_parameter_bound_target = null;
            this.empty_target = null;
            this.formal_parameter_target = null;
            this.throws_target = null;
            this.localvar_target = null;
            this.catch_target = null;
            this.offset_target = offset_target;
            this.type_argument_target = null;
        }

        public TargetInfo(TypeArgumentTarget type_argument_target) {
            this.type_parameter_target = null;
            this.supertype_target = null;
            this.type_parameter_bound_target = null;
            this.empty_target = null;
            this.formal_parameter_target = null;
            this.throws_target = null;
            this.localvar_target = null;
            this.catch_target = null;
            this.offset_target = null;
            this.type_argument_target = type_argument_target;
        }


    }

    @Getter
    public static class TypeParameterTarget {
        private final int type_parameter_index; /*u1*/

        public TypeParameterTarget(int typeParameterIndex) {
            this.type_parameter_index = typeParameterIndex;
        }
    }

    @Getter
    public static class SupertypeTarget {

        private final int supertype_index; /*u2*/

        public SupertypeTarget(int supertypeIndex) {
            this.supertype_index = supertypeIndex;
        }
    }

    @Getter
    public static class TypeParameterBoundTarget {

        private final int type_parameter_index; /*u1*/
        private final int bound_index; /*u1*/

        public TypeParameterBoundTarget(int typeParameterIndex, int boundIndex) {
            this.type_parameter_index = typeParameterIndex;
            this.bound_index = boundIndex;
        }
    }

    @Getter
    public static class EmptyTarget {
    }

    @Getter
    public static class FormalParameterTarget {

        private final int formal_parameter_index; /*u1*/

        public FormalParameterTarget(int formalParameterIndex) {
            this.formal_parameter_index = formalParameterIndex;
        }
    }

    @Getter
    public static class ThrowsTarget {

        private final int throws_type_index; /*u2*/

        public ThrowsTarget(int throwsTypeIndex) {
            this.throws_type_index = throwsTypeIndex;
        }
    }

    @Getter
    public static class LocalvarTarget {

        private final int table_length; /*u2*/
        private final Table[] table;

        public LocalvarTarget(int tableLength, Table[] table) {
            this.table_length = tableLength;
            this.table = table;
        }

        @Getter
        public static class Table {

            private final int start_pc; /*u2*/
            private final int length; /*u2*/
            private final int index; /*u2*/

            public Table(int startPc, int length, int index) {
                this.start_pc = startPc;
                this.length = length;
                this.index = index;
            }
        }
    }

    @Getter
    public static class CatchTarget {

        private final int exception_table_index; /*u2*/

        public CatchTarget(int exceptionTableIndex) {
            this.exception_table_index = exceptionTableIndex;
        }
    }

    @Getter
    public static class OffsetTarget {

        private final int offset; /*u2*/

        public OffsetTarget(int offset) {
            this.offset = offset;
        }
    }

    @Getter
    public static class TypeArgumentTarget {

        private final int offset; /*u2*/
        private final int type_argument_index; /*u1*/

        public TypeArgumentTarget(int offset, int typeArgumentIndex) {
            this.offset = offset;
            this.type_argument_index = typeArgumentIndex;
        }
    }

    @Getter
    public static class ParameterAnnotation {
        private final int num_annotations; /*u2*/
        private final Annotation[] annotations;

        public ParameterAnnotation(int numAnnotations, Annotation[] annotations) {
            num_annotations = numAnnotations;
            this.annotations = annotations;
        }
    }

    @Getter
    public static class Annotation {
        private final int type_index; /*u2*/
        private final int num_element_value_pairs; /*u2*/
        private final ElementValuePairs[] element_value_pairs;

        public Annotation(int typeIndex, int numElementValuePairs, ElementValuePairs[] elementValuePairs) {
            type_index = typeIndex;
            num_element_value_pairs = numElementValuePairs;
            element_value_pairs = elementValuePairs;
        }
    }

    @Getter
    private static class ElementValuePairs {
        private final int element_name_index; /*u2*/
        private final ElementValue value;

        private ElementValuePairs(int elementNameIndex, ElementValue value) {
            element_name_index = elementNameIndex;
            this.value = value;
        }
    }

    @Getter
    public static class ElementValue {
        private final int tag; /*u1*/
        private final Value value;

        public ElementValue(int tag, Value value) {
            this.tag = tag;
            this.value = value;
        }

        @Getter
        public static class Value {
            private final int const_value_index; /*u2*/
            private final int type_name_index; /*u2*/
            private final int const_name_index; /*u2*/
            private final int class_info_index; /*u2*/
            private final Annotation annotation_value;
            private final int num_values; /*u2*/
            private final ElementValue[] values;

            public Value(int value, boolean flag) {
                if (flag) {
                    this.const_value_index = value;
                    this.class_info_index = -1;
                } else {
                    this.const_value_index = -1;
                    this.class_info_index = const_value_index;
                }
                this.type_name_index = -1;
                this.const_name_index = -1;
                this.annotation_value = null;
                this.num_values = -1;
                this.values = null;
            }

            public Value(int typeNameIndex, int constNameIndex) {
                this.const_value_index = -1;
                this.type_name_index = typeNameIndex;
                this.const_name_index = constNameIndex;
                this.class_info_index = -1;
                this.annotation_value = null;
                this.num_values = -1;
                this.values = null;
            }

            public Value(Annotation annotationValue) {
                this.const_value_index = -1;
                this.type_name_index = -1;
                this.const_name_index = -1;
                this.class_info_index = -1;
                this.annotation_value = annotationValue;
                this.num_values = -1;
                this.values = null;
            }

            public Value(int numValues,
                         ElementValue[] values) {
                this.const_value_index = -1;
                this.type_name_index = -1;
                this.const_name_index = -1;
                this.class_info_index = -1;
                this.annotation_value = null;
                this.num_values = numValues;
                this.values = values;
            }

            public Value(int constValueIndex,
                         int typeNameIndex,
                         int constNameIndex,
                         int classInfoIndex,
                         Annotation annotationValue,
                         int numValues,
                         ElementValue[] values) {
                this.const_value_index = constValueIndex;
                this.type_name_index = typeNameIndex;
                this.const_name_index = constNameIndex;
                this.class_info_index = classInfoIndex;
                this.annotation_value = annotationValue;
                this.num_values = numValues;
                this.values = values;
            }
        }
    }

    @Getter
    public static class AnnotationDefault {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/
        private final ElementValue default_value;

        public AnnotationDefault(int attributeNameIndex, int attributeLength, ElementValue defaultValue) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            default_value = defaultValue;
        }
    }

    @Getter
    public static class LineNumberTable {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/
        private final int line_number_table_length; /*u2*/
        private final LineNumberTable$[] line_number_table;

        public LineNumberTable(int attributeNameIndex, int attributeLength, int lineNumberTableLength, LineNumberTable$[] lineNumberTable) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            line_number_table_length = lineNumberTableLength;
            line_number_table = lineNumberTable;
        }

        public static class LineNumberTable$ {
            private final int start_pc; /*u2*/
            private final int line_number; /*u2*/

            public LineNumberTable$(int startPc, int lineNumber) {
                start_pc = startPc;
                line_number = lineNumber;
            }
        }
    }

    @Getter
    public static class LocalVariableTable {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/
        private final int local_variable_table_length; /*u2*/
        private final LocalVariableTable$[] local_variable_table;

        public LocalVariableTable(int attributeNameIndex, int attributeLength, int localVariableTableLength, LocalVariableTable$[] localVariableTable) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            local_variable_table_length = localVariableTableLength;
            local_variable_table = localVariableTable;
        }

        @Getter
        public static class LocalVariableTable$ {
            private final int start_pc; /*u2*/
            private final int length; /*u2*/
            private final int name_index; /*u2*/
            private final int descriptor_index; /*u2*/
            private final int index; /*u2*/

            public LocalVariableTable$(int startPc, int length, int nameIndex, int descriptorIndex, int index) {
                start_pc = startPc;
                this.length = length;
                name_index = nameIndex;
                descriptor_index = descriptorIndex;
                this.index = index;
            }
        }
    }

    @Getter
    public static class LocalVariableTypeTable {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/
        private final int local_variable_type_table_length; /*u2*/
        private final LocalVariableTypeTable$[] local_variable_type_table;

        public LocalVariableTypeTable(int attributeNameIndex, int attributeLength, int localVariableTypeTableLength, LocalVariableTypeTable$[] localVariableTypeTable) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            local_variable_type_table_length = localVariableTypeTableLength;
            local_variable_type_table = localVariableTypeTable;
        }

        @Getter
        public static class LocalVariableTypeTable$ {
            private final int start_pc; /*u2*/
            private final int length; /*u2*/
            private final int name_index; /*u2*/
            private final int signature_index; /*u2*/
            private final int index; /*u2*/

            public LocalVariableTypeTable$(int startPc, int length, int nameIndex, int signatureIndex, int index) {
                start_pc = startPc;
                this.length = length;
                name_index = nameIndex;
                signature_index = signatureIndex;
                this.index = index;
            }
        }
    }

    @Getter
    public static class StackMapTable {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/
        private final int number_of_entries; /*u2*/
        private final StackMapFrame stack_map_frame;

        public StackMapTable(int attributeNameIndex, int attributeLength, int numberOfEntries, StackMapFrame stackMapFrame) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
            number_of_entries = numberOfEntries;
            stack_map_frame = stackMapFrame;
        }
    }

    @Getter
    public static class StackMapFrame {
        private final SameFrame same_frame;
        private final SameLocalsOneStackItemFrame same_locals_1_stack_item_frame;
        private final SameLocalsOneStackItemFrameExtended same_locals_1_stack_item_frame_extended;
        private final ChopFrame chop_frame;
        private final SameFrameExtended same_frame_extended;
        private final AppendFrame append_frame;
        private final FullFrame full_frame;

        public StackMapFrame(SameFrame sameFrame) {
            same_frame = sameFrame;
            same_locals_1_stack_item_frame = null;
            same_locals_1_stack_item_frame_extended = null;
            chop_frame = null;
            same_frame_extended = null;
            append_frame = null;
            full_frame = null;
        }

        public StackMapFrame(SameLocalsOneStackItemFrame sameLocals1StackItemFrame) {
            same_frame = null;
            same_locals_1_stack_item_frame = sameLocals1StackItemFrame;
            same_locals_1_stack_item_frame_extended = null;
            chop_frame = null;
            same_frame_extended = null;
            append_frame = null;
            full_frame = null;
        }

        public StackMapFrame(SameLocalsOneStackItemFrameExtended sameLocals1StackItemFrameExtended) {
            same_frame = null;
            same_locals_1_stack_item_frame = null;
            same_locals_1_stack_item_frame_extended = sameLocals1StackItemFrameExtended;
            chop_frame = null;
            same_frame_extended = null;
            append_frame = null;
            full_frame = null;
        }

        public StackMapFrame(ChopFrame chopFrame) {
            same_frame = null;
            same_locals_1_stack_item_frame = null;
            same_locals_1_stack_item_frame_extended = null;
            chop_frame = chopFrame;
            same_frame_extended = null;
            append_frame = null;
            full_frame = null;
        }

        public StackMapFrame(SameFrameExtended sameFrameExtended) {
            same_frame = null;
            same_locals_1_stack_item_frame = null;
            same_locals_1_stack_item_frame_extended = null;
            chop_frame = null;
            same_frame_extended = sameFrameExtended;
            append_frame = null;
            full_frame = null;
        }

        public StackMapFrame(AppendFrame appendFrame) {
            same_frame = null;
            same_locals_1_stack_item_frame = null;
            same_locals_1_stack_item_frame_extended = null;
            chop_frame = null;
            same_frame_extended = null;
            append_frame = appendFrame;
            full_frame = null;
        }

        public StackMapFrame(FullFrame fullFrame) {
            same_frame = null;
            same_locals_1_stack_item_frame = null;
            same_locals_1_stack_item_frame_extended = null;
            chop_frame = null;
            same_frame_extended = null;
            append_frame = null;
            full_frame = fullFrame;
        }
    }

    @Getter
    public static class SameFrame {
        private final int frameType; /* SAME 0-63 u1*/

        public SameFrame(int frameType) {
            this.frameType = frameType;
        }
    }

    @Getter
    public static class SameLocalsOneStackItemFrame {
        private final int frameType; /* SAME 64-127 u1*/
        private final VerificationTypeInfo[] stack;

        public SameLocalsOneStackItemFrame(int frameType, VerificationTypeInfo[] stack) {
            this.frameType = frameType;
            this.stack = stack;
        }
    }

    @Getter
    public static class SameLocalsOneStackItemFrameExtended {
        private final int frameType; /* SAME 247 u1*/
        private final int offset_delta; /*u2*/
        private final VerificationTypeInfo[] stack;

        public SameLocalsOneStackItemFrameExtended(int frameType, int offsetDelta, VerificationTypeInfo[] stack) {
            this.frameType = frameType;
            offset_delta = offsetDelta;
            this.stack = stack;
        }
    }

    @Getter
    public static class ChopFrame {
        private final int frameType; /* CHOP 248-250 u1*/
        private final int offset_delta; /*u2*/

        public ChopFrame(int frameType, int offsetDelta) {
            this.frameType = frameType;
            offset_delta = offsetDelta;
        }
    }

    @Getter
    public static class SameFrameExtended {
        private final int frameType; /* SAME_FRAME_EXTENDED 251 u1*/
        private final int offset_delta; /*u2*/

        public SameFrameExtended(int frameType, int offsetDelta) {
            this.frameType = frameType;
            offset_delta = offsetDelta;
        }
    }

    @Getter
    public static class AppendFrame {
        private final int frameType; /* APPEND 252-254 u1*/
        private final int offset_delta; /*u2*/
        private final VerificationTypeInfo[] stack;

        public AppendFrame(int frameType, int offsetDelta, VerificationTypeInfo[] stack) {
            this.frameType = frameType;
            offset_delta = offsetDelta;
            this.stack = stack;
        }
    }

    @Getter
    public static class FullFrame {
        private final int frameType; /* FULL_FRAME 255 u1*/
        private final int offset_delta; /*u2*/
        private final int number_of_locals; /*u2*/
        private final VerificationTypeInfo[] locals;
        private final int number_of_stack_items; /*u2*/
        private final VerificationTypeInfo[] stack;

        public FullFrame(int frameType, int offsetDelta, int numberOfLocals, VerificationTypeInfo[] locals, int numberOfStackItems, VerificationTypeInfo[] stack) {
            this.frameType = frameType;
            this.offset_delta = offsetDelta;
            this.number_of_locals = numberOfLocals;
            this.locals = locals;
            this.number_of_stack_items = numberOfStackItems;
            this.stack = stack;
        }
    }

    @Getter
    public static class VerificationTypeInfo {
        private final Top_variable_info top_variable_info;
        private final Integer_variable_info integer_variable_info;
        private final Float_variable_info float_variable_info;
        private final Long_variable_info long_variable_info;
        private final Double_variable_info double_variable_info;
        private final Null_variable_info null_variable_info;
        private final UninitializedThis_variable_info uninitializedThis_variable_info;
        private final Object_variable_info object_variable_info;
        private final Uninitialized_variable_info uninitialized_variable_info;

        public VerificationTypeInfo(Top_variable_info topVariableInfo) {
            top_variable_info = topVariableInfo;
            integer_variable_info = null;
            float_variable_info = null;
            long_variable_info = null;
            double_variable_info = null;
            null_variable_info = null;
            uninitializedThis_variable_info = null;
            object_variable_info = null;
            uninitialized_variable_info = null;
        }

        public VerificationTypeInfo(Integer_variable_info integerVariableInfo) {
            top_variable_info = null;
            integer_variable_info = integerVariableInfo;
            float_variable_info = null;
            long_variable_info = null;
            double_variable_info = null;
            null_variable_info = null;
            uninitializedThis_variable_info = null;
            object_variable_info = null;
            uninitialized_variable_info = null;
        }

        public VerificationTypeInfo(Float_variable_info floatVariableInfo) {
            top_variable_info = null;
            integer_variable_info = null;
            float_variable_info = floatVariableInfo;
            long_variable_info = null;
            double_variable_info = null;
            null_variable_info = null;
            uninitializedThis_variable_info = null;
            object_variable_info = null;
            uninitialized_variable_info = null;
        }

        public VerificationTypeInfo(Long_variable_info longVariableInfo) {
            top_variable_info = null;
            integer_variable_info = null;
            float_variable_info = null;
            long_variable_info = longVariableInfo;
            double_variable_info = null;
            null_variable_info = null;
            uninitializedThis_variable_info = null;
            object_variable_info = null;
            uninitialized_variable_info = null;
        }

        public VerificationTypeInfo(Double_variable_info doubleVariableInfo) {
            top_variable_info = null;
            integer_variable_info = null;
            float_variable_info = null;
            long_variable_info = null;
            double_variable_info = doubleVariableInfo;
            null_variable_info = null;
            uninitializedThis_variable_info = null;
            object_variable_info = null;
            uninitialized_variable_info = null;
        }

        public VerificationTypeInfo(Null_variable_info nullVariableInfo) {
            top_variable_info = null;
            integer_variable_info = null;
            float_variable_info = null;
            long_variable_info = null;
            double_variable_info = null;
            null_variable_info = nullVariableInfo;
            uninitializedThis_variable_info = null;
            object_variable_info = null;
            uninitialized_variable_info = null;
        }

        public VerificationTypeInfo(UninitializedThis_variable_info uninitializedThisVariableInfo) {
            top_variable_info = null;
            integer_variable_info = null;
            float_variable_info = null;
            long_variable_info = null;
            double_variable_info = null;
            null_variable_info = null;
            uninitializedThis_variable_info = uninitializedThisVariableInfo;
            object_variable_info = null;
            uninitialized_variable_info = null;
        }

        public VerificationTypeInfo(Object_variable_info objectVariableInfo) {
            top_variable_info = null;
            integer_variable_info = null;
            float_variable_info = null;
            long_variable_info = null;
            double_variable_info = null;
            null_variable_info = null;
            uninitializedThis_variable_info = null;
            object_variable_info = objectVariableInfo;
            uninitialized_variable_info = null;
        }

        public VerificationTypeInfo(Uninitialized_variable_info uninitializedVariableInfo) {
            top_variable_info = null;
            integer_variable_info = null;
            float_variable_info = null;
            long_variable_info = null;
            double_variable_info = null;
            null_variable_info = null;
            uninitializedThis_variable_info = null;
            object_variable_info = null;
            uninitialized_variable_info = uninitializedVariableInfo;
        }
    }

    @Getter
    public static class Top_variable_info {
        private final int tag = 0;
    }

    @Getter
    public static class Integer_variable_info {
        private final int tag = 1;
    }

    @Getter
    public static class Float_variable_info {
        private final int tag = 2;
    }

    @Getter
    public static class Double_variable_info {
        private final int tag = 3;
    }

    @Getter
    public static class Long_variable_info {
        private final int tag = 4;
    }

    @Getter
    public static class Null_variable_info {
        private final int tag = 5;
    }

    @Getter
    public static class UninitializedThis_variable_info {
        private final int tag = 6;
    }

    @Getter
    public static class Object_variable_info {
        private final int tag = 7;
        private final int cpool_index; /*u2*/

        public Object_variable_info(int cpoolIndex) {
            cpool_index = cpoolIndex;
        }
    }

    @Getter
    public static class Uninitialized_variable_info {
        private final int tag = 8;
        private final int offset; /*u2*/

        public Uninitialized_variable_info(int offset) {
            this.offset = offset;
        }
    }

    @Getter
    public static class Synthetic {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/

        public Synthetic(int attributeNameIndex, int attributeLength) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
        }
    }

    @Getter
    public static class Deprecated {
        private final int attribute_name_index; /*u2*/
        private final int attribute_length; /*u4*/

        public Deprecated(int attributeNameIndex, int attributeLength) {
            attribute_name_index = attributeNameIndex;
            attribute_length = attributeLength;
        }
    }
}
