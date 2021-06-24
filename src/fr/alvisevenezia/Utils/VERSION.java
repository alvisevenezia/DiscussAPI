package fr.alvisevenezia.Utils;

import fr.alvisevenezia.Web.Utils.DATAType;

public enum VERSION {

    BETA(0,7,"BETA");

    int id;
    int packetHeaderSize;
    String name;

    VERSION(int id,int packetHeaderSize,String name) {
        this.id = id;
        this.packetHeaderSize = packetHeaderSize;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getPacketHeaderSize() {
        return packetHeaderSize;
    }

    public String getName() {
        return name;
    }

    public static VERSION getByName(String name){

        for(VERSION version : values()){

            if(version.name.equalsIgnoreCase(name))return version;

        }

        return null;
    }

    public static VERSION getByID(int id){

        for(VERSION version : values()){

            if(version.getId() == id)return version;

        }

        return null;
    }

}
