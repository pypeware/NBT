package com.nukkitx.nbt;

import com.nukkitx.nbt.util.stream.LittleEndianDataInputStream;
import com.nukkitx.nbt.util.stream.LittleEndianDataOutputStream;
import com.nukkitx.nbt.util.stream.NetworkDataInputStream;
import com.nukkitx.nbt.util.stream.NetworkDataOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import static java.util.Objects.requireNonNull;
import java.util.StringJoiner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NbtUtils {

    public static final int MAX_DEPTH = 32;
    private static final char[] HEX_CODE = "0123456789ABCDEF".toCharArray();

    private NbtUtils() {
    }

    public static NBTInputStream createReader(final InputStream stream, final boolean internKeys, final boolean internValues) {
        requireNonNull(stream, "stream");
        return new NBTInputStream(new DataInputStream(stream), internKeys, internValues);
    }

    public static NBTInputStream createReaderLE(final InputStream stream, final boolean internKeys, final boolean internValues) {
        requireNonNull(stream, "stream");
        return new NBTInputStream(new LittleEndianDataInputStream(stream), internKeys, internValues);
    }

    public static NBTInputStream createGZIPReader(final InputStream stream, final boolean internKeys, final boolean internValues) throws IOException {
        return createReader(new GZIPInputStream(stream), internKeys, internValues);
    }

    public static NBTInputStream createNetworkReader(final InputStream stream, final boolean internKeys, final boolean internValues) {
        requireNonNull(stream, "stream");
        return new NBTInputStream(new NetworkDataInputStream(stream), internKeys, internValues);
    }

    public static NBTInputStream createReader(final InputStream stream) {
        return createReader(stream, false, false);
    }

    public static NBTInputStream createReaderLE(final InputStream stream) {
        return createReaderLE(stream, false, false);
    }

    public static NBTInputStream createGZIPReader(final InputStream stream) throws IOException {
        return createGZIPReader(stream, false, false);
    }

    public static NBTInputStream createNetworkReader(final InputStream stream) {
        return createNetworkReader(stream, false, false);
    }

    public static NBTOutputStream createWriter(final OutputStream stream) {
        requireNonNull(stream, "stream");
        return new NBTOutputStream(new DataOutputStream(stream));
    }

    public static NBTOutputStream createWriterLE(final OutputStream stream) {
        requireNonNull(stream, "stream");
        return new NBTOutputStream(new LittleEndianDataOutputStream(stream));
    }

    public static NBTOutputStream createGZIPWriter(final OutputStream stream) throws IOException {
        return createWriter(new GZIPOutputStream(stream));
    }

    public static NBTOutputStream createNetworkWriter(final OutputStream stream) {
        return new NBTOutputStream(new NetworkDataOutputStream(stream));
    }

    public static String toString(final Object o) {
        if (o instanceof Byte) {
            return ((byte) o) + "b";
        } else if (o instanceof Short) {
            return ((short) o) + "s";
        } else if (o instanceof Integer) {
            return ((int) o) + "i";
        } else if (o instanceof Long) {
            return ((long) o) + "l";
        } else if (o instanceof Float) {
            return ((float) o) + "f";
        } else if (o instanceof Double) {
            return ((double) o) + "d";
        } else if (o instanceof byte[]) {
            return "0x" + printHexBinary((byte[]) o);
        } else if (o instanceof String) {
            return "\"" + o + "\"";
        } else if (o instanceof int[]) {
            final StringJoiner joiner = new StringJoiner(", ");
            for (final int i : (int[]) o) {
                joiner.add(i + "i");
            }
            return "[ " + joiner + " ]";
        } else if (o instanceof long[]) {
            final StringJoiner joiner = new StringJoiner(", ");
            for (final long l : (long[]) o) {
                joiner.add(l + "l");
            }
            return "[ " + joiner + " ]";
        }
        return o.toString();
    }

    public static <T> T copy(final T val) {
        if (val instanceof byte[]) {
            final byte[] bytes = (byte[]) val;
            return (T) Arrays.copyOf(bytes, bytes.length);
        } else if (val instanceof int[]) {
            final int[] ints = (int[]) val;
            return (T) Arrays.copyOf(ints, ints.length);
        } else if (val instanceof long[]) {
            final long[] longs = (long[]) val;
            return (T) Arrays.copyOf(longs, longs.length);
        }
        return val;
    }

    public static String indent(final String string) {
        final StringBuilder builder = new StringBuilder("  " + string);
        for (int i = 2; i < builder.length(); i++) {
            if (builder.charAt(i) == '\n') {
                builder.insert(i + 1, "  ");
                i += 2;
            }
        }
        return builder.toString();
    }

    public static String printHexBinary(final byte[] data) {
        final StringBuilder r = new StringBuilder(data.length << 1);
        for (final byte b : data) {
            r.append(HEX_CODE[(b >> 4) & 0xF]);
            r.append(HEX_CODE[(b & 0xF)]);
        }
        return r.toString();
    }
}
