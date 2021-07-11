package fr.alvisevenezia.web.user;

import fr.alvisevenezia.utils.csv.CSVBuilder;
import fr.alvisevenezia.utils.csv.CSVReader;

import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.Key;
import java.util.Base64;

public class IDUserList {


    /**
     * File Format
     *
     * UserID, Public KEY, UserID, Public KEY, UserID, Public KEY...
     *
     */

    private File listFile;

    public IDUserList(File listFile) {
        this.listFile = listFile;
    }

    public IDUserList(String path){

        listFile = new File(path);

    }

    public Key getKeyFromID(String path,String ID,String algorithm){

        String keyString = CSVReader.readStringFile(path).get(ID);
        return new SecretKeySpec(Base64.getDecoder().decode(ID),0,Base64.getDecoder().decode(ID).length,algorithm);

    }

    public void setKeyForID(String path,Key key){

        CSVBuilder.addToFile(path,Base64.getEncoder().encodeToString(key.getEncoded()));

    }

    public File getListFile() {
        return listFile;
    }

    public void setListFile(File listFile) {
        this.listFile = listFile;
    }
}
