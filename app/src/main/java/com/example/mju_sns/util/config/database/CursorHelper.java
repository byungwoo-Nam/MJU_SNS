package com.example.mju_sns.util.config.database;

import android.database.Cursor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CursorHelper {

    private Cursor cursor;
    private Class cls;
    private Class<?> ctype;
    private Method method;
    private Field field;

    public CursorHelper(Cursor cursor, Class cls){
        this.cursor = cursor;
        this.cls = cls;
    }

    public List getList(){
        System.out.println("-----------------------------------커서작업");
        List list = new ArrayList();
        try {
            String [] columns;
            //Method method = cls.getDeclaredMethod("printIt", noparams);
            columns = cursor.getColumnNames();

            //int parameter
            Class[] paramInt = new Class[1];
            paramInt[0] = Integer.TYPE;

            //String parameter
            Class[] paramString = new Class[1];
            paramString[0] = String.class;

            //Boolean parameter
            Class[] paramBoolean = new Class[1];
            paramBoolean[0] = Boolean.TYPE;

            while(cursor.moveToNext()){
                Object obj = cls.newInstance();
                for(int i = 0; i < columns.length; i++) {
                    System.out.println(columns[i].toString() + "::" + getFieldType(columns[i]));
                    switch (getFieldType(columns[i])){
                        case "int":
                            method = cls.getMethod("set"+stringFirstUpper(columns[i]), paramInt);
                            method.invoke(obj, cursor.getInt(i));
                            break;
                        case "String":
                            method = cls.getMethod("set"+stringFirstUpper(columns[i]), paramString);
                            method.invoke(obj, new String(cursor.getString(i)));
                            break;
                        case "boolean":
                            method = cls.getMethod("set"+stringFirstUpper(columns[i]), paramBoolean);
                            method.invoke(obj, cursor.getInt(i)>0);
                            break;
                        default:
                            break;
                    }
                }
                list.add(obj);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            this.cursor.close();
        }

        return list;
    }

    private String stringFirstUpper(String str){
        String transString = str.substring(0, 1);
        transString = transString.toUpperCase();
        transString += str.substring(1);

        return transString;
    }

    private String getFieldType(String name){
        String typeSimpleName = "";
        try {
            field = cls.getDeclaredField(name);
            field.setAccessible(true);
            ctype = field.getType();
            typeSimpleName = ctype.getSimpleName();
        }catch(Exception e){
            e.printStackTrace();
        }
        return typeSimpleName;
    }
}