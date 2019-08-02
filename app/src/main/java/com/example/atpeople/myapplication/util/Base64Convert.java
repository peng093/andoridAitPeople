package com.example.atpeople.myapplication.util;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Base64Convert {
    public static List<String> StringListToBase64(List<String> srcListString,int type)
    {
        List<String> listRecord=new ArrayList<>();
        String str64="";
        //String img64="";
        try {
            if (srcListString!=null) {
                if (type==1)//音频
                {
                    for (int i = 0; i < srcListString.size(); i++) {
                        str64 = "data:audio/amr;base64,";
                        str64 = str64 + encodeBase64File(srcListString.get(i));
                        listRecord.add(str64);
                    }
                }
                if (type==2)//图片
                {
                    String end;
                    for (int i = 0; i < srcListString.size(); i++) {
                        File file = new File(srcListString.get(i));
                        end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
                        if (end.equals("jpg")) {
                            str64 = "data:image/jpg;base64,";
                        }
                        if (end.equals("jpeg")) {
                            str64 = "data:image/jpeg;base64,";
                        }
                        if (end.equals("png")) {
                            str64 = "data:image/png;base64,";
                        }
                        if (end.equals("gif")) {
                            str64 = "data:image/gif;base64,";
                        }
                        str64 = str64 + encodeBase64File(srcListString.get(i));
                        listRecord.add(str64);
                    }
                }
            }
        }catch (Exception e)
        {
            System.out.println(e);
        }
        return listRecord;
    }

    public static String StringToBase64(String srcPath,int type)
    {
       // List<String> listRecord=new ArrayList<>();
        String str64="";
        //String img64="";
        try {
            if (srcPath!=null) {
                if (type==1)//音频
                {

                        str64 = "data:audio/amr;base64,";
                        str64 = str64 + encodeBase64File(srcPath);
                        //listRecord.add(str64);
                }
                if (type==2)//图片
                {
                    String end;
//                    for (int i = 0; i < srcListString.size(); i++) {
                        File file = new File(srcPath);
                        end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
                        if (end.equals("jpg")) {
                            str64 = "data:image/jpg;base64,";
                        }
                        if (end.equals("jpeg")) {
                            str64 = "data:image/jpeg;base64,";
                        }
                        if (end.equals("png")) {
                            str64 = "data:image/png;base64,";
                        }
                        if (end.equals("gif")) {
                            str64 = "data:image/gif;base64,";
                        }
                        str64 = str64 + encodeBase64File(srcPath);
                        //listRecord.add(str64);
                }
            }
        }catch (Exception e)
        {
            System.out.println(e);
        }
        return str64;
    }


    public static String encodeBase64File(String path) throws Exception {
        File  file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.NO_WRAP);
    }
    /**
     * decoderBase64File:(将base64字符解码保存文件). <br/>
     * @author guhaizhou@126.com
     * @param base64Code 编码后的字串
     * @param savePath  文件保存路径
     * @throws Exception
     * @since JDK 1.6
     */
    public static void decoderBase64File(String base64Code,String savePath) throws Exception {
//byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        byte[] buffer =Base64.decode(base64Code, Base64.DEFAULT);
        FileOutputStream out = new FileOutputStream(savePath);
        out.write(buffer);
        out.close();
    }
}
