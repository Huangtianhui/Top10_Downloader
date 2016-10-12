package com.example.tianhuihuang.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by tianhuihuang on 2016-09-19.
 */
public class ParseApplications {
    //this file is used to store multiple applications
    private String xmlData;
    private ArrayList<Application> applications;

    public ParseApplications(String xmlData) {
        this.xmlData = xmlData;
        applications=new ArrayList<Application>();
    }

    public ArrayList<Application> getApplications(){
        return applications;
    }

    public boolean process(){
        boolean Status=true;
        //如果不赋值,就会让后面的程序报错
        Application currentRecord=null;
        boolean inEntry=false;
        String textValue="";

        try{

            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp=factory.newPullParser();
            xpp.setInput(new StringReader(this.xmlData));
            int eventType =xpp.getEventType();

            while(eventType!=XmlPullParser.END_DOCUMENT){
                String tagName=xpp.getName();
                switch(eventType){
                    case XmlPullParser.START_TAG:
                        //Log.d("ParseApplications","Starting tag for "+ tagName);
                        if (tagName.equalsIgnoreCase("entry")){
                            inEntry=true;
                            currentRecord =new Application();
                            break;
                        }
                        //break;
                    case XmlPullParser.TEXT:
                        textValue=xpp.getText();
                        //每一个case都要有一个break
                        break;

                    case XmlPullParser.END_TAG:
                        //Log.d("ParseApplications","Endding tag for "+ tagName);
                        if (inEntry){
                            if(tagName.equalsIgnoreCase("entry")){

                                applications.add(currentRecord);
                                inEntry=false;

                            }else if(tagName.equalsIgnoreCase("name")){
                                currentRecord.setName(textValue);
                            }else if(tagName.equalsIgnoreCase("artist")){
                                currentRecord.setArtist(textValue);
                            }else if(tagName.equalsIgnoreCase("releaseDate")){
                                currentRecord.setReleaseDate(textValue);
                            }

                        }
                        break;
                    default:
                        //nothing else to do

                }
                eventType=xpp.next();
            }

            return true;
        }catch (Exception e){
            Status=false;
            e.printStackTrace();
        }

        for(Application app:applications){
            Log.d("ParseApplications","*********");
            Log.d("ParseApplicationsName","name"+app.getName());
            Log.d("ParseApplicationsArtist","artist"+app.getArtist());
            Log.d("ParseApplicationsDate","date"+app.getReleaseDate());
        }
        return true;
    }
}
