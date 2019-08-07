package com.example.smartcontroltower;


import android.util.Log;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DBUtil4Initial {

    public static ArrayList<LinkedHashMap<String, String>> sendRequestWithOkHttp() {
        ArrayList<LinkedHashMap<String, String>> anss = new ArrayList<>();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2/initial.xml")
                    .build();
            Log.d("URL", request.toString());
            Response response = client.newCall(request).execute();
            Log.d("Content", response.toString());
            String responseData = response.body().string();
            anss=parseXMLWithPull(responseData, anss);


            for (int i = 0; i < anss.size(); i++) {
                LinkedHashMap<String, String> count = anss.get(i);
                for (String s : count.keySet()) {
                     Log.e("jieguo", s + ":" + count.get(s) + ":" + i);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return anss;

    }


    private static Connection getSQLConnection(String ip, String user, String pwd, String db) {
        Connection con = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":1433/" + db, user, pwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static ArrayList<LinkedHashMap<String, String>> QuerySQL(String sql) {
        String result = "";
        ArrayList<LinkedHashMap<String, String>> answer = new ArrayList<>();
        try {
            Connection conn = getSQLConnection(地址账号用户名);
            Statement stmt = conn.createStatement();//
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String after = strChangeXML(rs.getString(1));
                answer = parseXMLWithPull(after, answer);
            }


//            for(int i=0;i<answer.size();i++){
//                LinkedHashMap<String,String> count=answer.get(i);
//                for(String s:count.keySet()){
//                    Log.d("jieguo",s+":"+count.get(s)+":"+i);
//                }
//            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }


    private static ArrayList parseXMLWithPull(String xmlData,ArrayList<LinkedHashMap<String, String>> answer2) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //System.out.println(xmlData);
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();

            LinkedHashMap<String, String> map = new LinkedHashMap<>();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG: {
                        if ((!"rawdata".equals(nodeName)) && (!"row".equals(nodeName)) && (!"version".equals(nodeName))&& (!"version_addclosing".equals(nodeName))
                                && !"version_year".equals(nodeName) && !"version_year_quar".equals(nodeName) && !"version_year_quar_week".equals(nodeName)) {
                            String id = xmlPullParser.nextText();
                            map.put(nodeName, id);
                            answer2.add(map);
                            map=new LinkedHashMap<>();
                        }
                        else if (!"row".equals(nodeName)&&!"rawdata".equals(nodeName)){
                            map.put("title", nodeName);
                            answer2.add(map);
                            map=new LinkedHashMap<>();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        if ("option".equals(nodeName)) {
                            answer2.add(map);
                            map = new LinkedHashMap<>();
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer2;

    }

    public static String strChangeXML(String str) throws IOException {
        SAXReader saxReader = new SAXReader();
        Document document;
        String afterFormat = "";
        try {
            document = saxReader.read(new ByteArrayInputStream(str.getBytes("UTF-8")));
            OutputFormat format = OutputFormat.createPrettyPrint();
            StringWriter a = new StringWriter();
            XMLWriter writer = new XMLWriter(a, format);
            writer.write(document);
            afterFormat = a.toString();
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return afterFormat;
    }


}