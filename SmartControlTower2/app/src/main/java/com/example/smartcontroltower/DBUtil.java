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

    public static ArrayList<LinkedHashMap<String,String>> sendRequestWithOkHttp() {
        ArrayList<LinkedHashMap<String, String>> anss=new ArrayList<>();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://10.0.2.2/analysis.xml")
                    .build();
            Log.d("URL", request.toString());
            Response response = client.newCall(request).execute();
            Log.d("Content", response.toString());
            String responseData = response.body().string();
            parseXMLWithPull(responseData,anss);

            for (int i = 0; i < anss.size(); i++) {
                LinkedHashMap<String, String> count = anss.get(i);
                for (String s : count.keySet()) {
                   // Log.d("jieguo", s + ":" + count.get(s) + ":" + i);
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
        ArrayList<LinkedHashMap<String, String>> answer=new ArrayList<>();
        try {
            Connection conn = getSQLConnection("10.82.244.53", "sa", "Dell@2008", "PCWebsite");
            Statement stmt = conn.createStatement();//
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String after = strChangeXML(rs.getString(1));
                answer=parseXMLWithPull(after,answer);
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


    private static ArrayList<LinkedHashMap<String, String>>  parseXMLWithPull(String xmlData,ArrayList<LinkedHashMap<String, String>> answer) {
        try {
            //ans.clear();
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
                        if ((!"rawdata".equals(nodeName)) && (!"systemoverall".equals(nodeName)) && (!"row".equals(nodeName)) &&(!"systemclient".equals(nodeName))
                                &&(!"systemisg".equals(nodeName))&&(!"client".equals(nodeName))&&(!"Consumer".equals(nodeName))&&(!"Commercial".equals(nodeName))&&(!"Workstation".equals(nodeName))&&(!"Alienware".equals(nodeName))&&(!"Lat_Opt".equals(nodeName))
                                &&(!"ALIENWARE_DESKTOPS".equals(nodeName))&&(!"Personal_Vostro".equals(nodeName))&&(!"XPS_DT_NB".equals(nodeName))&&(!"CLOUD_CLIENT_IOT".equals(nodeName))&&(!"CHROME".equals(nodeName))&&(!"ALIENWARE_NOTEBOOKS".equals(nodeName))
                                &&(!"OPTIPLEX_DESKTOPS".equals(nodeName))&&(!"LATITUDE".equals(nodeName))&&(!"PERSONAL_DESKTOPS".equals(nodeName))&&(!"PERSONAL_NOTEBOOKS".equals(nodeName))&&(!"VOSTRO_DESKTOPS".equals(nodeName))&&(!"VOSTRO_NOTEBOOKS".equals(nodeName))
                                &&(!"FIXED_WORKSTATIONS".equals(nodeName))&&(!"MOBILE_WORKSTATIONS".equals(nodeName))&&(!"XPS_DESKTOPS".equals(nodeName))&&(!"XPS_NOTEBOOKS".equals(nodeName))&&(!"CLOUD_CLIENT".equals(nodeName))&&(!"INTERNET_OF_THINGS".equals(nodeName))
                                &&(!"isg_overall".equals(nodeName))&&(!"isg_system".equals(nodeName))&&(!"isg_PowerEdge".equals(nodeName))&&(!"isg_Cloud".equals(nodeName))&&(!"isg_Non_Sys".equals(nodeName))&&(!"isg_storage".equals(nodeName))
                                &&(!"isg_Networking".equals(nodeName))&&(!"isg_hit".equals(nodeName))) {
                            String id = xmlPullParser.nextText();
                            map.put(nodeName, id);
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        if (("rawdata".equals(nodeName)) || ("systemoverall".equals(nodeName)) || ("row".equals(nodeName)) || ("Commercial".equals(nodeName))||("systemclient".equals(nodeName))||
                                ("systemisg".equals(nodeName))||("client".equals(nodeName))||("Consumer".equals(nodeName))||("Workstation".equals(nodeName))||("Alienware".equals(nodeName))||("Lat_Opt".equals(nodeName))
                                ||("ALIENWARE_DESKTOPS".equals(nodeName))||("Personal_Vostro".equals(nodeName))||("XPS_DT_NB".equals(nodeName))||("CLOUD_CLIENT_IOT".equals(nodeName))||("CHROME".equals(nodeName))||("ALIENWARE_NOTEBOOKS".equals(nodeName))
                                ||("OPTIPLEX_DESKTOPS".equals(nodeName))||("LATITUDE".equals(nodeName))||("PERSONAL_DESKTOPS".equals(nodeName))||("PERSONAL_NOTEBOOKS".equals(nodeName))||("VOSTRO_DESKTOPS".equals(nodeName))||("VOSTRO_NOTEBOOKS".equals(nodeName))
                                ||("FIXED_WORKSTATIONS".equals(nodeName))||("MOBILE_WORKSTATIONS".equals(nodeName))||("XPS_DESKTOPS".equals(nodeName))||("XPS_NOTEBOOKS".equals(nodeName))||("CLOUD_CLIENT".equals(nodeName))||("INTERNET_OF_THINGS".equals(nodeName))
                                ||("isg_overall".equals(nodeName))||("isg_system".equals(nodeName))||("isg_PowerEdge".equals(nodeName))||("isg_Cloud".equals(nodeName))||("isg_Non_Sys".equals(nodeName))||("isg_storage".equals(nodeName))
                                ||("isg_Networking".equals(nodeName))||("isg_hit".equals(nodeName)))  {
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

        return answer;

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