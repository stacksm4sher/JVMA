package org.stacksm4sher.jvma;

import lombok.Getter;

@Getter
public class ClassInfo {

    private byte[] magicClassValue;
    private int minorClassVersion;
    private int majorClassVersion;
    private int classAccessFlags;
    private int thisClassConstantPoolIndex;
    private int superClassConstantPoolIndex;
    private int attributesCount;
    private byte[] attributes;
    private final RawBytecode rawBytecode;


    public ClassInfo(RawBytecode rawBytecode) {
        this.rawBytecode = rawBytecode;
        this.parse();
    }

    public void parse() {
        this.magicClassValue = rawBytecode.getMagic_u4();

        byte[] minor_version_u2 = rawBytecode.getMinor_version_u2();
        this.minorClassVersion = (0xff & (minor_version_u2[0] << 8)) | (0xff & minor_version_u2[1]);

        byte[] major_version_u2 = rawBytecode.getMajor_version_u2();
        this.majorClassVersion = (0xff & (major_version_u2[0] << 8)) | (0xff & major_version_u2[1]);

        byte[] access_flags = rawBytecode.getAccess_flags();
        this.classAccessFlags = (0xff & (access_flags[0] << 8)) | (0xff & access_flags[1]);

        byte[] this_class = rawBytecode.getThis_class();
        this.thisClassConstantPoolIndex = (0xff & (this_class[0] << 8)) | (0xff & this_class[1]);

        byte[] super_class = rawBytecode.getSuper_class();
        this.superClassConstantPoolIndex = (0xff & (super_class[0] << 8)) | (0xff & super_class[1]);

        byte[] attributes_count = rawBytecode.getAttributes_count();
        this.attributesCount = (0xff & (attributes_count[0] << 8)) | (0xff & attributes_count[1]);

        this.attributes = rawBytecode.getAttributes();
    }

}
