package com.sdu.inas.beans;

/**
 * 生成类似Mongodb的id
 *
 * @author icatzfd
 * Created on 2020/5/21 16:01.
 */


import com.google.common.base.Objects;

import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 15  * <p>A globally unique identifier for objects.</p>
 * 16  * <p/>
 * 17  * <p>Consists of 12 bytes, divided as follows:</p>
 * 18  * <table border="1">
 * 19  * <caption>ObjectID layout</caption>
 * 20  * <tr>
 * 21  * <td>0</td><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td><td>11</td>
 * 22  * </tr>
 * 23  * <tr>
 * 24  * <td colspan="4">time</td><td colspan="3">machine</td> <td colspan="2">pid</td><td colspan="3">inc</td>
 * 25  * </tr>
 * 26  * </table>
 * 27  * <p/>
 * 28  * <p>Instances of this class are immutable.</p>
 * 29
 */

public class ObjectId implements Comparable<ObjectId>, java.io.Serializable {

    private final int _time;

    private final int _machine;

    private final int _inc;

    private boolean _new;

    private static final int _genmachine;


    private static AtomicInteger _nextInc = new AtomicInteger((new Random()).nextInt());

    private static final long serialVersionUID = -4415279469780082174L;

    private static final Logger LOGGER = Logger.getLogger("org.bson.ObjectId");

    /**
     * 45      * Create a new object id.
     * 46
     */
    public ObjectId() {
        _time = (int) (System.currentTimeMillis() / 1000);
        _machine = _genmachine;
        _inc = _nextInc.getAndIncrement();
        _new = true;

    }

    public static String id() {
        return get().toHexString();
    }

    /**
     * 59      * Gets a new object id.
     * 60      *
     * 61      * @return the new id
     * 62
     */
    public static ObjectId get() {
        return new ObjectId();
    }

    /**
     * 68      * Checks if a string could be an {@code ObjectId}.
     * 69      *
     * 70      * @param s a potential ObjectId as a String.
     * 71      * @return whether the string could be an object id
     * 72      * @throws IllegalArgumentException if hexString is null
     * 73
     */

    public static boolean isValid(String s) {
        if (s == null) {
            return false;
        }

        final int len = s.length();
        if (len != 24) {
            return false;
        }

        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                continue;
            }
            if (c >= 'a' && c <= 'f') {
                continue;
            }
            if (c >= 'A' && c <= 'F') {
                continue;
            }

            return false;

        }
        return true;
    }

    /**
     * 99      * Converts this instance into a 24-byte hexadecimal string representation.
     * 100      *
     * 101      * @return a string representation of the ObjectId in hexadecimal format
     * 102
     */

    public String toHexString() {
        final StringBuilder buf = new StringBuilder(24);
        for (final byte b : toByteArray()) {
            buf.append(String.format("%02x", b & 0xff));

        }
        return buf.toString();

    }

    /**
     * 112      * Convert to a byte array.  Note that the numbers are stored in big-endian order.
     * 113      *
     * 114      * @return the byte array
     * 115
     */


    public byte[] toByteArray() {
        byte b[] = new byte[12];
        ByteBuffer bb = ByteBuffer.wrap(b);
        // by default BB is big endian like we need
        bb.putInt(_time);
        bb.putInt(_machine);
        bb.putInt(_inc);
        return b;

    }


    private int _compareUnsigned(int i, int j) {
        long li = 0xFFFFFFFFL;
        li = i & li;
        long lj = 0xFFFFFFFFL;
        lj = j & lj;
        long diff = li - lj;
        if (diff < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        if (diff > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) diff;

    }


    @Override
    public int compareTo(ObjectId id) {
        if (id == null) {
            return -1;
        }

        int x = _compareUnsigned(_time, id._time);
        if (x != 0) {
            return x;
        }

        x = _compareUnsigned(_machine, id._machine);
        if (x != 0) {
            return x;
        }

        return _compareUnsigned(_inc, id._inc);

    }

    /**
     * 155      * Gets the timestamp (number of seconds since the Unix epoch).
     * 156      *
     * 157      * @return the timestamp
     * 158
     */


    public int getTimestamp() {
        return _time;

    }

    /**
     * 164      * Gets the timestamp as a {@code Date} instance.
     * 165      *
     * 166      * @return the Date
     * 167
     */

    public Date getDate() {
        return new Date(_time * 1000L);

    }

    /**
     * 174      * Gets the current value of the auto-incrementing counter.
     * 175      *
     * 176      * @return the current counter value.
     * 177
     */


    public static int getCurrentCounter() {
        return _nextInc.get();

    }


    static {

        try {
            // build a 2-byte machine piece based on NICs info
            int machinePiece;
            {
                try {
                    StringBuilder sb = new StringBuilder();
                    Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
                    while (e.hasMoreElements()) {
                        NetworkInterface ni = e.nextElement();
                        sb.append(ni.toString());

                    }
                    machinePiece = sb.toString().hashCode() << 16;

                } catch (Throwable e) {
                    // exception sometimes happens with IBM JVM, use random
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                    machinePiece = (new Random().nextInt()) << 16;

                }
                LOGGER.fine("machine piece post: " + Integer.toHexString(machinePiece));

            }

            // add a 2 byte process piece. It must represent not only the JVM but the class loader.
            // Since static var belong to class loader there could be collisions otherwise
            final int processPiece;
            {
                int processId = new Random().nextInt();
                try {
                    processId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();

                } catch (Throwable t) {

                }

                ClassLoader loader = ObjectId.class.getClassLoader();
                int loaderId = loader != null ? System.identityHashCode(loader) : 0;

                StringBuilder sb = new StringBuilder();
                sb.append(Integer.toHexString(processId));
                sb.append(Integer.toHexString(loaderId));
                processPiece = sb.toString().hashCode() & 0xFFFF;
                LOGGER.fine("process piece: " + Integer.toHexString(processPiece));

            }
            _genmachine = machinePiece | processPiece;
            LOGGER.fine("machine : " + Integer.toHexString(_genmachine));

        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ObjectId that = (ObjectId) o;

        return Objects.equal(serialVersionUID, serialVersionUID) &&
                Objects.equal(LOGGER, LOGGER) &&
                Objects.equal(this._time, that._time) &&
                Objects.equal(this._machine, that._machine) &&
                Objects.equal(this._inc, that._inc) &&
                Objects.equal(this._new, that._new) &&
                Objects.equal(_nextInc, _nextInc) &&
                Objects.equal(_genmachine, _genmachine);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serialVersionUID, LOGGER, _time, _machine, _inc, _new,
                _nextInc, _genmachine);

    }


    public static void main(String[] args) {
        System.out.println(new ObjectId().toHexString());
        System.out.println(new ObjectId().toHexString());
        System.out.println(new ObjectId().toHexString());

    }

}