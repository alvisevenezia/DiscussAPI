package fr.alvisevenezia.web.utils;

public enum DATAType {

    TEXT(0),AUDIO(1),ID(2);

    int id;

    DATAType(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static DATAType getByID(int id){

        for(DATAType dataType : values()){

            if(dataType.getId() == id)return dataType;

        }

        return null;
    }

}
