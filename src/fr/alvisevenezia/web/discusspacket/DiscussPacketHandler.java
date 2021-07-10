package fr.alvisevenezia.web.discusspacket;

import fr.alvisevenezia.utils.error.InvalidDataTypeError;
import fr.alvisevenezia.utils.VERSION;
import fr.alvisevenezia.web.utils.DATAType;
import fr.alvisevenezia.encryption.symmetrical.SymmetricalEncryptedMessage;

import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DiscussPacketHandler {

    private int discussPacketSize;
    private int startIndex;
    private int actualIndex;
    private DATAType dataType;
    private VERSION version;
    private byte[] message;

    public DiscussPacketHandler(VERSION version, DATAType dataType, int discussPacketSize, int startIndex) {
        this.discussPacketSize = discussPacketSize;
        this.startIndex = startIndex;
        this.dataType = dataType;
        this.version = version;
    }

    public void setMessage(String message) {

        //on verifie que le type de donnée soit bien du texte
        if(dataType != DATAType.TEXT){

            //sinon on renvoie une erreur
            throw new InvalidDataTypeError();

        }

        this.message = message.getBytes(StandardCharsets.UTF_16LE);
    }

    public byte[] getMessage() {
        return message;
    }

    public void setEncryptedMessage(String message, String key) {

        //on verifie que le type de donnée soit bien du texte
        if(dataType != DATAType.TEXT){

            //sinon on renvoie une erreur
            throw new InvalidDataTypeError();

        }

        this.message = SymmetricalEncryptedMessage.getEncryptedMessage(message,key);
    }

    public DiscussPacket getFirstPacket(DiscussPacket[] discussPacketsArray){

        int min = -1;
        DiscussPacket firstPacket = null;

        for(DiscussPacket discussPacket : discussPacketsArray){

            if(min == -1)min = discussPacket.getPacketId();
            else if(discussPacket.getPacketId() < min){
                min = discussPacket.getPacketId();
                firstPacket = discussPacket;
            }

        }

        return firstPacket;
    }



    public byte[] getMergedByteArray(DiscussPacket[] discussPacketsArray){

        byte[] byteArray;

        if(discussPacketsArray[discussPacketsArray.length-1].getActualSize() !=discussPacketsArray[discussPacketsArray.length-1].getSize()) {

            byteArray = new byte[(discussPacketsArray.length-1) * (discussPacketsArray[0].getSize() - discussPacketsArray[0].getVersion().getPacketHeaderSize()) + discussPacketsArray[discussPacketsArray.length-1].getActualSize()];

        }else{

            byteArray = new byte[discussPacketsArray.length * (discussPacketsArray[0].getSize() - discussPacketsArray[0].getVersion().getPacketHeaderSize())];

        }
        int index = 0;

        for(DiscussPacket packet : discussPacketsArray){

            System.out.println("MERGED PACKET ID : "+packet.getPacketId());

            byte[] byteDataArray = packet.getData();

            for(int i = 0;i<byteDataArray.length;i++){

                if((packet.getPacketId()*byteDataArray.length)+i == byteArray.length)break;

                byteArray[(packet.getPacketId()*byteDataArray.length)+i] = byteDataArray[i];

            }

        }


        return byteArray;
    }

    public DiscussPacket[] getDiscussPacketArrayFromByteArray(byte[] byteArray){

        DiscussPacket[] discussPacketArray = new DiscussPacket[1];
        DiscussPacket discussPacket;

        int packetId;
        int packetSize = 0;
        int actualPacketSize = 0;
        int limit = 1;
        byte[] packetData;

        for(int turn = 0;turn < limit;turn++){

            VERSION packetVersion = VERSION.getByID(ByteBuffer.wrap(new byte[]{0, 0, byteArray[turn * packetSize], byteArray[turn * packetSize + 1]}).getInt());
            packetId = ByteBuffer.wrap(new byte[]{0, 0, byteArray[turn * packetSize + 2], byteArray[turn * packetSize + 3]}).getInt();
            DATAType packetDataType = DATAType.getByID(ByteBuffer.wrap(new byte[]{0, 0, 0, byteArray[turn * packetSize + 4]}).getInt());
            packetSize = ByteBuffer.wrap(new byte[]{0, 0, byteArray[turn * packetSize + 5], byteArray[turn * packetSize + 6]}).getInt();
            actualPacketSize = ByteBuffer.wrap(new byte[]{0, 0, byteArray[turn * packetSize + 7], byteArray[turn * packetSize + 8]}).getInt();

            if((byteArray.length/packetSize) != limit){

                limit = (byteArray.length/packetSize);
                discussPacketArray = new DiscussPacket[limit];

            }

            packetData = new byte[packetSize - version.getPacketHeaderSize()];

            for (int i = version.getPacketHeaderSize(); i < packetSize; i++) {

                packetData[i - version.getPacketHeaderSize()] = byteArray[turn * packetSize + i];

            }

            discussPacket = new DiscussPacket(packetId, packetSize, actualPacketSize, packetVersion, packetDataType);
            discussPacket.setData(packetData);

            discussPacketArray[turn] = discussPacket;

        }

        return discussPacketArray;
    }

    public void sendDiscussPacket(Socket socket,DiscussPacket[] discussPackets){

        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            System.out.print("PACKET BYTE MSG : ");

            for(int i = 0; i< discussPackets.length;i++){

                System.out.println("PACKET ID : "+i);
                byte[] data = discussPackets[i].buildData();

                for(byte b : data) {

                    System.out.print(b);

                }

                System.out.println('\n');

                outputStream.write(data);
                outputStream.flush();

            }

            outputStream.close();

        }catch (Exception e){

            e.printStackTrace();

        }

    }

    public DiscussPacket[] createPacket(){

        int bodySize = discussPacketSize - version.getPacketHeaderSize();

        byte[] data;
        byte[] packetData;
        int trigger = -1;
        DiscussPacket discussPacket;

        switch (dataType){

            case TEXT:

                if(message == null)throw new NullPointerException();

                DiscussPacket[] packets = new DiscussPacket[(message.length/bodySize)+1];

                for(int packetID = 0;packetID < (message.length/bodySize)+1;packetID++){

                    packetData = new byte[bodySize];

                    for(int dataID = 0;dataID < bodySize;dataID++){

                        if((packetID*bodySize)+dataID >= message.length){

                            packetData[dataID] = 0b00000000;

                            if(trigger == -1)trigger = dataID;


                        }else {

                            packetData[dataID] = message[(packetID * bodySize) + dataID];

                        }
                    }

                    if(trigger != -1) {

                        discussPacket = new DiscussPacket(packetID, discussPacketSize, trigger, version, dataType);

                    }else{

                        discussPacket = new DiscussPacket(packetID, discussPacketSize, discussPacketSize, version, dataType);

                    }
                    discussPacket.setData(packetData);
                    packets[packetID] = discussPacket;

                }

                return packets;


        }

        return null;

    }


}
