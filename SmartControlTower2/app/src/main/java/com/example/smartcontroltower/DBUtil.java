package com.example.smartcontroltower;


import android.graphics.Color;
import android.util.Log;


import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;

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
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DBUtil {

    private static ArrayList<LinkedHashMap<String,String>> answer=new ArrayList<>();

    public static void sendRequestWithOkHttp(final SmartTable<Object> table) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://10.0.2.2/test.xml")
                            .build();
                    Log.d("URL", request.toString());
                    Response response = client.newCall(request).execute();
                    Log.d("Content", response.toString());
                    String responseData = response.body().string();
                    parseXMLWithPull(responseData);

                    for(int i=0;i<answer.size();i++){
                        LinkedHashMap<String,String> count=answer.get(i);
                        for(String s:count.keySet()){
                            Log.d("jieguo",s+":"+count.get(s)+":"+i);
                        }
                    }

                    setNumber(table);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

    public static String QuerySQL() {
        String result = "";
        try {
            Connection conn = getSQLConnection("10.82.244.53", "sa", "Dell@2008", "PCWebsite");
            String sql = "EXEC [SP_IDC_EOQ_SUMMARY1] '" + "IDC" + "','" + "QuarView" + "','" + "FY20Q2" + "','" + "" + "','" + "overall" + "','" + "system" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "','" + "overall" + "'";
            Statement stmt = conn.createStatement();//
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String[] res = rs.getString(1).split("</Summary>");
                for (int i = 0; i < res.length; i++) {
                    Log.d("Array", res[i]);
                }

                String after = strChangeXML(rs.getString(1));
                parseXMLWithPull(after);

            }



            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    private static void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // System.out.println(xmlData);
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();

            LinkedHashMap<String,String> map=new LinkedHashMap<>();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG: {
                        if ((!"rawdata".equals(nodeName)) && (!"Summary".equals(nodeName)) && (!"row".equals(nodeName)) && (!"ref_date".equals(nodeName))) {
                            String id = xmlPullParser.nextText();
                            map.put(nodeName,id);
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        if("row".equals(nodeName)){
                            answer.add(map);
                            map=new LinkedHashMap<>();
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
            /** 将document中的内容写入文件中 */
            StringWriter a = new StringWriter();
            XMLWriter writer = new XMLWriter(a, format);
            writer.write(document);
            afterFormat = a.toString();
//            System.out.println(afterFormat);
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return afterFormat;
    }


    public static void setNumber(SmartTable<Object> table){



        //表格数据 datas 是需要填充的数据
        List<Object> maplist = new ArrayList<>();
        for(LinkedHashMap<String,String> a:answer){
            maplist.add(a);
        }

        table.setTableData(null);
        MapTableData tableData = MapTableData.create("表格名", maplist);
        Column groupColumn = new Column("组合", tableData.getColumns().get(0), tableData.getColumns().get(1));
        table.getConfig().setFixedTitle(true);
        tableData.getColumns().get(0).setFixed(true);
        table.setZoom(true,2,1);
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);
//设置数据
        table.setTableData(tableData);
        table.getConfig().setContentStyle(new FontStyle(40, Color.BLUE));
        table.getConfig().setColumnTitleStyle(new FontStyle(40,Color.BLUE));
    }


}