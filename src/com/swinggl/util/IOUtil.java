package com.swinggl.util;

import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import static org.lwjgl.BufferUtils.*;

/**
 * Created by tyty5949 on 12/14/15 at 2:46 PM.
 * Project: SwingGL
 */
public class IOUtil {

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    /**
     * Reads the specified resource and returns the raw data as a ByteBuffer.
     *
     * @param resource   the resource to read
     * @param bufferSize the initial buffer size
     *
     * @return the resource data
     *
     * @throws IOException if an IO error occurs
     */
    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize){
        ByteBuffer buffer = null;

        try {
            File file = new File(resource);
            if ( file.isFile() ) {
                FileInputStream fis = new FileInputStream(file);
                FileChannel fc = fis.getChannel();

                buffer = createByteBuffer((int)fc.size() + 1);

                fis.close();
                fc.close();
            } else {
                buffer = createByteBuffer(bufferSize);

                InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
                if ( source == null )
                    throw new FileNotFoundException(resource);

                try {
                    try (ReadableByteChannel rbc = Channels.newChannel(source)) {
                        while (true) {
                            int bytes = rbc.read(buffer);
                            if (bytes == -1)
                                break;
                            if (buffer.remaining() == 0)
                                buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                        }
                    }
                } finally {
                    source.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        buffer.flip();
        return buffer;
    }

}
