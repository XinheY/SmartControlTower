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
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Timeout;


public class DBUtil {

    private static ArrayList<LinkedHashMap<String, String>> answer = new ArrayList<>();

    public static ArrayList<LinkedHashMap<String,String>> sendRequestWithOkHttp() {

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2/dyndyn.xml")
                    .build();
            Log.d("URL", request.toString());
            Response response = client.newCall(request).execute();
            Log.d("Content", response.toString());
            String responseData = response.body().string();
            parseXMLWithPull(responseData);

            for (int i = 0; i < answer.size(); i++) {
                LinkedHashMap<String, String> count = answer.get(i);
                for (String s : count.keySet()) {
                    Log.d("jieguo", s + ":" + count.get(s) + ":" + i);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return answer;

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
        try {

            Connection conn = getSQLConnection("10.82.244.53", "sa", "Dell@2008", "PCWebsite");
            Statement stmt = conn.createStatement();//
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String after = strChangeXML(rs.getString(1));
                parseXMLWithPull(after);
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


    private static void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // System.out.println(xmlData);
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();

            LinkedHashMap<String, String> map = new LinkedHashMap<>();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG: {
                        if ((!"rawdata".equals(nodeName)) && (!"Summary".equals(nodeName)) && (!"row".equals(nodeName)) && (!"ref_date".equals(nodeName))&&(!"gva".equals(nodeName))) {
                            String id = xmlPullParser.nextText();
                            map.put(nodeName, id);
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        if ("row".equals(nodeName)) {
                            answer.add(map);
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