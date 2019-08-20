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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;


public class DBUtil4Initial {

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
     * @return
     */
    public static ArrayList<LinkedHashMap<String, String>> QuerySQL(String sql) {
        String result = "";
        ArrayList<LinkedHashMap<String, String>> answer = new ArrayList<>();
        try {
            Connection conn = getSQLConnection(地址密码用户名);
            if(conn!=null){
            Statement stmt = conn.createStatement();//
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String after = strChangeXML(rs.getString(1));
                answer = parseXMLWithPull(after, answer);
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
     * @param answer2
     * @return
     */
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

    /**
     * 通过运行jdbc语句获取获取用户权限情况
     * @param sql
     * @param username
     * @param link
     * @return
     */
    public static int QuerySQLAccess(String sql,String username,String link) {
       int id=-10;
        try {
            Connection conn = getSQLConnection("10.82.244.53", "sa", "Dell@2008", "PCWebsite");
            if(conn!=null){
                CallableStatement cst = null;
                cst = conn.prepareCall(sql);
                cst.setString(1,"ASIA-PACIFIC\\"+username);
                cst.setString(2,link);
                cst.registerOutParameter(3, Types.INTEGER);
                int cou = cst.executeUpdate();
                 id= cst.getInt(3);
                System.out.println(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

}