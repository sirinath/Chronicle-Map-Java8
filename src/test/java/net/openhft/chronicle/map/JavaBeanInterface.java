package net.openhft.chronicle.map;

import net.openhft.lang.model.constraints.MaxSize;

public interface JavaBeanInterface {
    void setFlag(boolean flag);

    boolean getFlag();

    void setByte(byte b);

    byte getByte();

    void setShort(short s);

    short getShort();

    void setChar(char ch);

    char getChar();

    void setInt(int i);

    int getVolatileInt();

    void setOrderedInt(int i);

    int getInt();

    void setFloat(float f);

    float getFloat();

    void setX(long l);

    long getX();

    long addX(long toAdd);

    void setDouble(double d);

    double getDouble();

    double addAtomicDouble(double toAdd);

    void setString(@MaxSize(8) String s);

    String getString();

    StringBuilder getUsingString(StringBuilder b);
}
