package fr.alvisevenezia.Web.DiscussPacket;

import fr.alvisevenezia.Utils.VERSION;
import fr.alvisevenezia.Web.Utils.DATAType;

import java.nio.ByteBuffer;

/**
 * Architecture DiscussPacket
 *
 * header{
 *
 *      - version : 2 bytes
 *      - packetId : 2 bytes
 *      - dataType : 1 byte
 *      - size : 2 bytes
 *      - actual data size : 2 bytes
 *
 * }
 *
 * body{
 *
 *      - data : size - headerSize bytes
 *
 *  }
 */

public class DiscussPacket {

    private final int packetId;
    private final int size;
    private final int actualSize;
    private final VERSION version;
    private final DATAType dataType;
    private byte data[];

    public DiscussPacket(int packetId, int size, int actualSize, VERSION version, DATAType dataType) {
        this.packetId = packetId;
        this.size = size;
        this.actualSize = actualSize;
        this.version = version;
        this.dataType = dataType;
        this.data = data;
    }

    public int getActualSize() {
        return actualSize;
    }

    public int getPacketId() {
        return packetId;
    }

    public int getSize() {
        return size;
    }

    public VERSION getVersion() {
        return version;
    }

    public DATAType getDataType() {
        return dataType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] buildData(){


        ByteBuffer byteBuffer = ByteBuffer.allocate(size);

        //building the header of the discussPacket

        byteBuffer.put(ByteBuffer.allocate(4).putInt(version.getId()).array()[0]);
        byteBuffer.put(ByteBuffer.allocate(4).putInt(version.getId()).array()[1]);
        byteBuffer.put(Integer.valueOf(((packetId)>>8)).byteValue());
        byteBuffer.put(Integer.valueOf(packetId).byteValue());
        byteBuffer.put(ByteBuffer.allocate(4).putInt(dataType.getId()).array()[0]);
        byteBuffer.put(Integer.valueOf(((size)>>8)).byteValue());
        byteBuffer.put(Integer.valueOf(size).byteValue());
        byteBuffer.put(Integer.valueOf(((actualSize)>>8)).byteValue());
        byteBuffer.put(Integer.valueOf(actualSize).byteValue());

        //building the body of the discussPacket

        for(byte b : data){

            byteBuffer.put(b);

        }

        return byteBuffer.array();
    }
}
