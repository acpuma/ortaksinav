package net.yazsoft.frame.sms;

import com.thoughtworks.xstream.XStream;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.security.UsersDao;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Users;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Named
@ViewScoped
public class SmsDao {

    private static final Logger logger = Logger.getLogger(SmsDao.class);

    private ArrayList<Users> users;
    private ArrayList<String> phones;
    private String mesaj;

    @Inject UsersDao usersDao;

    public SmsDao() {
        users=new ArrayList<>();
        phones=new ArrayList<String>();

    }

    @PostConstruct
    public void init() {

    }

    public void initUsers() {
        Collection usersCollection=Util.getActiveSchool().getUsersCollection();
        users= new ArrayList<Users>(usersCollection);
    }
    private String prepareXml() {
        logger.info("LOG02530: USERS : " + users);
        XmlClass xmlClass=new XmlClass();
        logger.info("LOG02520: MESSAGE : " + mesaj);

        for (String phone:phones) {
            xmlClass.addYolla(new Yolla(phone, mesaj));
        }
        //xmlClass.addYolla(new Yolla("5062532953", "test mesaj"));
        //xmlClass.addYolla(new Yolla("5062532953", "test mesaj2"));
        //xmlClass.getBilgi().setGonderim_tarih(Util.getNow().toString());
        //xmlClass.getISLEM().getYOLLA().setMESAJ("test mesaj");
        //xmlClass.getISLEM().getYOLLA().setNO("5062532953");

        XStream xstream = new XStream();
        xstream.alias("xml", XmlClass.class);
        xstream.alias("YOLLA", Yolla.class);
        //xstream.alias("BILGI", XmlClass.Bilgi.class);
        xstream.aliasField("GONDERIM#TARIH", XmlClass.BILGI.class, "GONDERIM_TARIH");
        xstream.aliasField("KULLANICI#ADI",XmlClass.BILGI.class, "KULLANICI_ADI");
        String xml=xstream.toXML(xmlClass);
        xml=xml.substring(xml.indexOf('>')+1);
        xml=xml.substring(0, xml.lastIndexOf('<'));
        xml=xml.replace('#','_');

        logger.info("LOG02470: XML : " + xml);
        return xml;
    }

    public void sendSms() {
        String xml=prepareXml();
        send(xml);
    }
    public void send(String xml) {
        String resp=null;
        try {

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://www.ayelsms.com/services/api.php?islem=sms");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            /*
            nvps.add(new BasicNameValuePair("username", "21B18"));
            nvps.add(new BasicNameValuePair("password", "Y7e0jhv0."));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            */

            HttpEntity myEntity = new StringEntity(xml,
                    ContentType.create("application/xml", "UTF-8"));
            httpPost.setEntity(myEntity);
            CloseableHttpResponse response2 = httpclient.execute(httpPost);
            logger.info("LOG02510: REQUEST : " + httpPost);
            logger.info("LOG02510: REQUEST ENTITY: " + EntityUtils.toString(myEntity));

            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                logger.info("LOG02490: RESPONSE : " + response2);
                resp=EntityUtils.toString(entity2);
                logger.info("LOG02500: ENTITY : " + resp);
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
            } finally {
                response2.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp=e.getMessage();
        }
        Util.setFacesMessage(resp);

    }

    public ArrayList<Users> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Users> users) {
        this.users = users;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public ArrayList<String> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }
}
