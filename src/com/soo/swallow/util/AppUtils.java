package com.soo.swallow.util;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * @author Soo
 */
public class AppUtils {
    
    public interface OnBindHandler<T> {
        T onBind(Cursor cursor);
    }
    
    /**
     * 获取系统所有图片目录及其目录下的图片文件
     */
    public synchronized static <T> LinkedHashMap<String, ArrayList<T>> getSystemImageObjects(Context context, Class<T> classT, OnBindHandler<T> handler) {
        ArgsUtils.notNull(context, "Context");
        ArgsUtils.notNull(classT, "Dest class");
        ArgsUtils.notNull(handler, "OnBindHandler");
        
        LinkedHashMap<String, ArrayList<T>> imgDirs = new LinkedHashMap<String, ArrayList<T>>();
        
        ContentResolver cr = context.getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = cr.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            
//            int i1 = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//            int i2 = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
//            int i3 = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
//            int i4 = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
//            int i5 = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
//            int i6 = cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION);
//            int i7 = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
//            int i8 = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT);
//            int i9 = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH);
//            int i10 = cursor.getColumnIndex(MediaStore.Images.Media.TITLE);
//            int i11 = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
//            int i12 = cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
//            int i13 = cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE);
//            
//            String bucket_display_name = cursor.getString(i1);
//            long bucket_id = cursor.getLong(i2);
//            long date_add = cursor.getLong(i3);
//            long date_modified = cursor.getLong(i4);
//            long date_taken = cursor.getLong(i5);
//            String description = cursor.getString(i6);
//            String display_name = cursor.getString(i7);
//            int height = cursor.getInt(i8);
//            int width = cursor.getInt(i9);
//            String title = cursor.getString(i10);
//            int size = cursor.getInt(i11);
//            double latitude = cursor.getDouble(i12);
//            double longtitude = cursor.getDouble(i13);
            
            int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String path = cursor.getString(index);
            File file = new File(path);
            String dir = file.getParent();
            
            ArrayList<T> listFile = null;
            if (!imgDirs.containsKey(dir)) {
                listFile = new ArrayList<T>();
                imgDirs.put(dir, listFile);
            } else {
                listFile = imgDirs.get(dir);
            }
            T t = handler.onBind(cursor);
            if (t != null) {
                listFile.add(t);
            }
        }
        cursor.close();
        return imgDirs;
    }

}
