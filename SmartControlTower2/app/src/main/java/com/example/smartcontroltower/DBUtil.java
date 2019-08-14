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

public class DBUtil {

    /**
     * 通过ip地址，用户名，密码和数据库名称连接数据库
     * @param ip
     * @param user
     * @param pwd
     * @param db
     * @return
     */
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

    /**
     * 获取数据库数据
     * @param sql
     * @param no
     * @return
     */
    public static ArrayList<LinkedHashMap<String, String>> QuerySQL(String sql, int no) {
        String result = "";
        ArrayList<LinkedHashMap<String, String>> answer = new ArrayList<>();
        try {
            Connection conn = getSQLConnection(地址密码用户名);
            if(conn!=null){
            Statement stmt = conn.createStatement();//
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Log.e("rs", rs.getString(1));
                String after = strChangeXML(rs.getString(1));
                if (no == 2) {
                    answer = parseXMLWithPull2(after,answer);
                } else {
                    answer = parseXMLWithPull(after, answer);
                }
            }
            rs.close();
            stmt.close();
            conn.close();}
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    /**
     * 将被解析成string的xml文件放进存储变量
     * @param xmlData
     * @param answer
     * @return
     */
    private static ArrayList<LinkedHashMap<String, String>> parseXMLWithPull(String xmlData, ArrayList<LinkedHashMap<String, String>> answer) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();

            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG: {
                        if ((!"Summary".equals(nodeName)) && (!"gva".equals(nodeName)) && (!"rawdata".equals(nodeName)) && (!"systemoverall".equals(nodeName)) && (!"row".equals(nodeName)) && (!"systemclient".equals(nodeName))
                                && (!"systemisg".equals(nodeName)) && (!"client".equals(nodeName)) && (!"Consumer".equals(nodeName)) && (!"Commercial".equals(nodeName)) && (!"Workstation".equals(nodeName)) && (!"Alienware".equals(nodeName)) && (!"Lat_Opt".equals(nodeName))
                                && (!"ALIENWARE_DESKTOPS".equals(nodeName)) && (!"Personal_Vostro".equals(nodeName)) && (!"XPS_DT_NB".equals(nodeName)) && (!"CLOUD_CLIENT_IOT".equals(nodeName)) && (!"CHROME".equals(nodeName)) && (!"ALIENWARE_NOTEBOOKS".equals(nodeName))
                                && (!"OPTIPLEX_DESKTOPS".equals(nodeName)) && (!"LATITUDE".equals(nodeName)) && (!"PERSONAL_DESKTOPS".equals(nodeName)) && (!"PERSONAL_NOTEBOOKS".equals(nodeName)) && (!"VOSTRO_DESKTOPS".equals(nodeName)) && (!"VOSTRO_NOTEBOOKS".equals(nodeName))
                                && (!"FIXED_WORKSTATIONS".equals(nodeName)) && (!"MOBILE_WORKSTATIONS".equals(nodeName)) && (!"XPS_DESKTOPS".equals(nodeName)) && (!"XPS_NOTEBOOKS".equals(nodeName)) && (!"CLOUD_CLIENT".equals(nodeName)) && (!"INTERNET_OF_THINGS".equals(nodeName))
                                && (!"isg_overall".equals(nodeName)) && (!"isg_system".equals(nodeName)) && (!"isg_PowerEdge".equals(nodeName)) && (!"isg_Cloud".equals(nodeName)) && (!"isg_Non_Sys".equals(nodeName)) && (!"isg_storage".equals(nodeName))
                                && (!"isg_Networking".equals(nodeName)) && (!"isg_hit".equals(nodeName)) && (!"result".equals(nodeName))) {
                            String id = xmlPullParser.nextText();
                            if (nodeName.equals("COL_TYPE")) {
                                nodeName = "Item";
                                map.put(nodeName, id);
                            } else {
                                map.put(nodeName, id);
                            }
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

        return answer;

    }

    /**
     * 解析analysis page专用
     * @param xmlData
     * @param answer
     * @return
     */
    private static ArrayList<LinkedHashMap<String, String>> parseXMLWithPull2(String xmlData, ArrayList<LinkedHashMap<String, String>> answer) {
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
                        if ((!"Summary".equals(nodeName)) && (!"gva".equals(nodeName)) && (!"rawdata".equals(nodeName)) && (!"systemoverall".equals(nodeName)) && (!"row".equals(nodeName)) && (!"systemclient".equals(nodeName))
                                && (!"systemisg".equals(nodeName)) && (!"client".equals(nodeName)) && (!"Consumer".equals(nodeName)) && (!"Commercial".equals(nodeName)) && (!"Workstation".equals(nodeName)) && (!"Alienware".equals(nodeName)) && (!"Lat_Opt".equals(nodeName))
                                && (!"ALIENWARE_DESKTOPS".equals(nodeName)) && (!"Personal_Vostro".equals(nodeName)) && (!"XPS_DT_NB".equals(nodeName)) && (!"CLOUD_CLIENT_IOT".equals(nodeName)) && (!"CHROME".equals(nodeName)) && (!"ALIENWARE_NOTEBOOKS".equals(nodeName))
                                && (!"OPTIPLEX_DESKTOPS".equals(nodeName)) && (!"LATITUDE".equals(nodeName)) && (!"PERSONAL_DESKTOPS".equals(nodeName)) && (!"PERSONAL_NOTEBOOKS".equals(nodeName)) && (!"VOSTRO_DESKTOPS".equals(nodeName)) && (!"VOSTRO_NOTEBOOKS".equals(nodeName))
                                && (!"FIXED_WORKSTATIONS".equals(nodeName)) && (!"MOBILE_WORKSTATIONS".equals(nodeName)) && (!"XPS_DESKTOPS".equals(nodeName)) && (!"XPS_NOTEBOOKS".equals(nodeName)) && (!"CLOUD_CLIENT".equals(nodeName)) && (!"INTERNET_OF_THINGS".equals(nodeName))
                                && (!"isg_overall".equals(nodeName)) && (!"isg_system".equals(nodeName)) && (!"isg_PowerEdge".equals(nodeName)) && (!"isg_Cloud".equals(nodeName)) && (!"isg_Non_Sys".equals(nodeName)) && (!"isg_storage".equals(nodeName))
                                && (!"isg_Networking".equals(nodeName)) && (!"isg_hit".equals(nodeName)) && (!"result".equals(nodeName))) {
                            String id = xmlPullParser.nextText();
                            if (nodeName.equals("COL_TYPE")) {
                                nodeName = "Item";
                                map.put(nodeName, id);
                            } else {
                                map.put(nodeName, id);
                            }
                        } else if ((!"Summary".equals(nodeName)) && (!"gva".equals(nodeName)) && (!"rawdata".equals(nodeName)) && (!"row".equals(nodeName))) {
                            map.put("Title", nodeName);
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
        return answer;

    }

    /**
     * 将xml文件解析成string
     * @param str
     * @return
     * @throws IOException
     */
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