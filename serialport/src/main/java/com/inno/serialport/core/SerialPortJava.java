package com.inno.serialport.core;

import java.io.FileDescriptor;

// 到SerialPortJava文件目录 javac -h . .\SerialPortJava.java
public class SerialPortJava {

    static {
        System.loadLibrary("serial_port");
    }

    private native FileDescriptor open(String absolutePath, int baudRate, int dataBits, int parity,
                                       int stopBits, int flags);

    public native void close();

    public native int test();
}
