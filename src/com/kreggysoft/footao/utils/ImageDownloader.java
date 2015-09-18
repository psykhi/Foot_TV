package com.kreggysoft.footao.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageDownloader {

public static Bitmap downloadImage(String url) throws MalformedURLException{
    return downloadImage(new URL(URLDecoder.decode(url)));
}

public static Bitmap downloadImage(URL url){
    Bitmap bmImg = null;
    try {
    HttpURLConnection conn= (HttpURLConnection)url.openConnection();
    conn.setDoInput(true);
    conn.connect();
    InputStream is = conn.getInputStream();

    bmImg = BitmapFactory.decodeStream(is);
    } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
    return null;
    }


    return bmImg;
 }  
}