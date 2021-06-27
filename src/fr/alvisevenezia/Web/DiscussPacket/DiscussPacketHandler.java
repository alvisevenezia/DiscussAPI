package fr.alvisevenezia.Web.DiscussPacket;

import fr.alvisevenezia.Utils.Error.InvalidDataTypeError;
import fr.alvisevenezia.Utils.VERSION;
import fr.alvisevenezia.Web.DiscussPacket.DiscussPacket;
import fr.alvisevenezia.Web.Utils.DATAType;
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
    private String message;

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

        this.message = message;
    }

    public String getMessage() {
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

        byte[] byteArray = new byte[discussPacketsArray.length*(discussPacketsArray[0].getSize()-discussPacketsArray[0].getVersion().getPacketHeaderSize())];
        int index = 0;

        for(DiscussPacket packet : discussPacketsArray){

            System.out.println(packet.getPacketId());

            byte[] byteDataArray = packet.getData();

            for(int i = 0;i<byteDataArray.length;i++){

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
        int limit = 1;
        byte[] packetData;

        for(int turn = 0;turn < limit;turn++){

            VERSION packetVersion = VERSION.getByID(ByteBuffer.wrap(new byte[]{0, 0, byteArray[turn * packetSize + 0], byteArray[turn * packetSize + 1]}).getInt());
            packetId = ByteBuffer.wrap(new byte[]{0, 0, byteArray[turn * packetSize + 2], byteArray[turn * packetSize + 3]}).getInt();
            DATAType packetDataType = DATAType.getByID(ByteBuffer.wrap(new byte[]{0, 0, 0, byteArray[turn * packetSize + 4]}).getInt());
            packetSize = ByteBuffer.wrap(new byte[]{0, 0, byteArray[turn * packetSize + 5], byteArray[turn * packetSize + 6]}).getInt();

            if((byteArray.length/packetSize) != limit){

                limit = (byteArray.length/packetSize);
                discussPacketArray = new DiscussPacket[limit];

            }

            packetData = new byte[packetSize - version.getPacketHeaderSize()];

            for (int i = 7; i < packetSize; i++) {

                packetData[i - version.getPacketHeaderSize()] = byteArray[turn * packetSize + i];

            }

            discussPacket = new DiscussPacket(packetVersion, packetId, packetDataType, packetSize);
            discussPacket.setData(packetData);

            discussPacketArray[turn] = discussPacket;

        }

        return discussPacketArray;
    }

    public void sendDiscussPacket(Socket socket,DiscussPacket[] discussPackets){

        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            for(int i = 0; i< discussPackets.length;i++){

                byte[] data = discussPackets[i].buildData();

                System.out.print("BYTE MSG : ");

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
        DiscussPacket discussPacket;

        switch (dataType){

            case TEXT:

                if(message == null)throw new NullPointerException();

                data = message.getBytes(StandardCharsets.UTF_16);

                DiscussPacket[] packets = new DiscussPacket[(data.length/bodySize)+1];

                for(int packetID = 0;packetID < (data.length/bodySize)+1;packetID++){

                    packetData = new byte[bodySize];

                    for(int dataID = 0;dataID < bodySize;dataID++){

                        if((packetID*bodySize)+dataID >= data.length){

                            packetData[dataID] = 0b00000000;

                        }else {

                            packetData[dataID] = data[(packetID * bodySize) + dataID];

                        }
                    }

                    discussPacket = new DiscussPacket(version,packetID,dataType,discussPacketSize);

                    discussPacket.setData(packetData);
                    packets[packetID] = discussPacket;

                }

                return packets;


        }

        return null;

    }


}
