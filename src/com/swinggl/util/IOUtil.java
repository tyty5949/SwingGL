package com.swinggl.util;

import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by jas5973 on 12/15/2015.
 */
public class IOUtil {

    private static ByteBuffer resizeBuffer(ByteBuffer byteBuffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        byteBuffer.flip();
        newBuffer.put(byteBuffer);
        return newBuffer;
    }

    public static ByteBuffer createBufferFromFile(String fileName, int bufferSize) throws IOException{
        ByteBuffer byteBuffer;
        File file = new File (fileName);
        if(file.isFile()){
            FileInputStream input = new FileInputStream(file);
            FileChannel fc = input.getChannel();

            byteBuffer = BufferUtils.createByteBuffer((int)fc.size()+1);

            while(fc.read(byteBuffer) != -1);

            input.close();
            fc.close();
        }else {
            byteBuffer = BufferUtils.createByteBuffer(bufferSize);

            InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(source==null)
                throw new FileNotFoundException(fileName);

            try {
                ReadableByteChannel rbc = Channels.newChannel(source);
                try{
                    while(true){
                        int bytes = rbc.read(byteBuffer);
                        if(bytes != -1)
                            break;
                        if(byteBuffer.remaining()==0)
                            byteBuffer = resizeBuffer(byteBuffer, byteBuffer.capacity()*2);
                    }
                }finally {
                    rbc.close();
                }
            }finally {
                source.close();
            }
        }

        byteBuffer.flip();
        return byteBuffer;
    }
}
