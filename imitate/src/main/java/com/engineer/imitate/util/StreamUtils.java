/*
 * Copyright (c) 2015 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.engineer.imitate.util;


import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import kotlin.text.Charsets;

public class StreamUtils {

    public static final int IO_BUFFER_SIZE = 8 * 1024;

    private static final int END_OF_STREAM = -1;

    public static String readAsciiLine(final InputStream pInputStream) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder(80);

        while (true) {
            final int oneByte = pInputStream.read();

            if (oneByte == -1) {
                throw new EOFException();
            } else if (oneByte == '\n') {
                break;
            }

            stringBuilder.append((char) oneByte);
        }

        final int length = stringBuilder.length();

        if ((length > 0) && (stringBuilder.charAt(length - 1) == '\r')) {
            stringBuilder.setLength(length - 1);
        }

        return stringBuilder.toString();
    }

    public static String readFully(final InputStream pInputStream) throws IOException {
        return StreamUtils.readFully(pInputStream, Charsets.UTF_8);
    }

    public static String readFully(final InputStream pInputStream, final Charset pCharset) throws IOException {
        return StreamUtils.readFully(new InputStreamReader(pInputStream, pCharset));
    }

    public static String readFully(final Reader pReader) throws IOException {
        try {
            final StringWriter writer = new StringWriter();
            final char[] buffer = new char[StreamUtils.IO_BUFFER_SIZE];

            int count;

            while ((count = pReader.read(buffer)) != StreamUtils.END_OF_STREAM) {
                writer.write(buffer, 0, count);
            }

            return writer.toString();
        } finally {
            StreamUtils.close(pReader);
        }
    }

    public static byte[] streamToBytes(final InputStream pInputStream) throws IOException {
        return StreamUtils.streamToBytes(pInputStream, StreamUtils.END_OF_STREAM);
    }

    public static byte[] streamToBytes(final InputStream pInputStream, final int pReadLimit) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream((pReadLimit == StreamUtils.END_OF_STREAM) ?
                StreamUtils.IO_BUFFER_SIZE : pReadLimit);
        StreamUtils.copy(pInputStream, os, pReadLimit);
        return os.toByteArray();
    }

    /**
     * @see {@link StreamUtils#streamToBytes(InputStream, int, byte[], int)}
     */
    public static void streamToBytes(final InputStream pInputStream, final int pByteLimit, final byte[] pData) throws
            IOException {
        StreamUtils.streamToBytes(pInputStream, pByteLimit, pData, 0);
    }

    /**
     * @param pInputStream the sources of the bytes.
     * @param pByteLimit   the amount of bytes to read.
     * @param pData        the array to place the read bytes in.
     * @param pOffset      the offset within pData.
     * @throws IOException
     */
    public static void streamToBytes(final InputStream pInputStream, final int pByteLimit, final byte[] pData, final
    int pOffset) throws IOException {
        if (pByteLimit > (pData.length - pOffset)) {
            throw new IOException("pData is not big enough.");
        }

        int pBytesLeftToRead = pByteLimit;
        int readTotal = 0;
        int read;
        while ((read = pInputStream.read(pData, pOffset + readTotal, pBytesLeftToRead)) != StreamUtils.END_OF_STREAM) {
            readTotal += read;
            if (pBytesLeftToRead > read) {
                pBytesLeftToRead -= read;
            } else {
                break;
            }
        }

        if (readTotal != pByteLimit) {
            throw new IOException("ReadLimit: '" + pByteLimit + "', Read: '" + readTotal + "'.");
        }
    }

    public static void copy(final InputStream pInputStream, final OutputStream pOutputStream) throws IOException {
        StreamUtils.copy(pInputStream, pOutputStream, StreamUtils.END_OF_STREAM);
    }

    public static void copy(final InputStream pInputStream, final byte[] pData) throws IOException {
        int dataOffset = 0;
        final byte[] buf = new byte[StreamUtils.IO_BUFFER_SIZE];
        int read;
        while ((read = pInputStream.read(buf)) != StreamUtils.END_OF_STREAM) {
            System.arraycopy(buf, 0, pData, dataOffset, read);
            dataOffset += read;
        }
    }

    public static void copy(final InputStream pInputStream, final ByteBuffer pByteBuffer) throws IOException {
        final byte[] buf = new byte[StreamUtils.IO_BUFFER_SIZE];
        int read;
        while ((read = pInputStream.read(buf)) != StreamUtils.END_OF_STREAM) {
            pByteBuffer.put(buf, 0, read);
        }
    }

    /**
     * Copy the content of the input stream into the output stream, using a
     * temporary byte array buffer whose size is defined by
     * {@link #IO_BUFFER_SIZE}.
     *
     * @param pInputStream  The input stream to copy from.
     * @param pOutputStream The output stream to copy to.
     * @param pByteLimit    not more than so much bytes to read, or unlimited if
     *                      {@link StreamUtils#END_OF_STREAM}.
     * @throws IOException If any error occurs during the copy.
     */
    public static void copy(final InputStream pInputStream, final OutputStream pOutputStream, final int pByteLimit)
            throws IOException {
        if (pByteLimit == StreamUtils.END_OF_STREAM) {
            final byte[] buf = new byte[StreamUtils.IO_BUFFER_SIZE];
            int read;
            while ((read = pInputStream.read(buf)) != StreamUtils.END_OF_STREAM) {
                pOutputStream.write(buf, 0, read);
            }
        } else {
            final byte[] buf = new byte[StreamUtils.IO_BUFFER_SIZE];
            final int bufferReadLimit = Math.min(pByteLimit, StreamUtils.IO_BUFFER_SIZE);
            long pBytesLeftToRead = pByteLimit;

            int read;
            while ((read = pInputStream.read(buf, 0, bufferReadLimit)) != StreamUtils.END_OF_STREAM) {
                if (pBytesLeftToRead > read) {
                    pOutputStream.write(buf, 0, read);
                    pBytesLeftToRead -= read;
                } else {
                    pOutputStream.write(buf, 0, (int) pBytesLeftToRead);
                    break;
                }
            }
        }
        pOutputStream.flush();
    }

    public static boolean copyAndClose(final InputStream pInputStream, final OutputStream pOutputStream) {
        try {
            StreamUtils.copy(pInputStream, pOutputStream, StreamUtils.END_OF_STREAM);
            return true;
        } catch (final IOException e) {
            return false;
        } finally {
            StreamUtils.close(pInputStream);
            StreamUtils.close(pOutputStream);
        }
    }

    /**
     * Closes the specified stream.
     *
     * @param pCloseable The stream to close.
     */
    public static void close(final Closeable pCloseable) {
        if (pCloseable != null) {
            try {
                pCloseable.close();
            } catch (final IOException e) {

            }
        }
    }

    /**
     * Flushes and closes the specified stream.
     *
     * @param pOutputStream The stream to close.
     */
    public static void flushCloseStream(final OutputStream pOutputStream) {
        if (pOutputStream != null) {
            try {
                pOutputStream.flush();
            } catch (final IOException e) {

            } finally {
                StreamUtils.close(pOutputStream);
            }
        }
    }

    /**
     * Flushes and closes the specified stream.
     *
     * @param pWriter The Writer to close.
     */
    public static void flushCloseWriter(final Writer pWriter) {
        if (pWriter != null) {
            try {
                pWriter.flush();
            } catch (final IOException e) {

            } finally {
                StreamUtils.close(pWriter);
            }
        }
    }
}
