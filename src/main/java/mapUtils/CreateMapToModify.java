package mapUtils;

import gate.*;
import gate.util.GateException;
import org.springframework.util.Assert;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class CreateMapToModify {

    private static final String USER_PROJECT_DIRECTORY = System.getProperty("user.dir");
    private static final String USER_HOME_DIRECTORY = System.getProperty("user.home");
    private static final String POS = "positive";
    private static final String NEG = "negative";
    private static final String NEU = "neutral";

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {

        try {

            if(Gate.getGateHome() == null) {
                Gate.setGateHome(new File(USER_HOME_DIRECTORY + "/GATE_Developer_8.0"));
                //Gate.setGateHome(new File("C:\\Users\\u124275\\Desktop\\gate-8.0-build4825-BIN"));
                //Gate.setGateHome(new File("D:\\Program Files\\GATE_Developer_8.0"));
            }
            if(Gate.getPluginsHome() == null) {
                Gate.setPluginsHome(new File(USER_HOME_DIRECTORY + "/GATE_Developer_8.0/plugins"));
                //Gate.setPluginsHome(new File("C:\\Users\\u124275\\Desktop\\gate-8.0-build4825-BIN\\plugins"));
                //Gate.setPluginsHome(new File("D:\\Program Files\\GATE_Developer_8.0\\plugins"));
            }
            Gate.init();
            String encoding = "UTF-8";
            File inDir=new File("./data/analysed");
            File[] flist=inDir.listFiles();
            String floc;

            Document d;

            Annotation tweet;
            AnnotationSet tweets;
            AnnotationSet locations;
            AnnotationSet lookups;
            AnnotationSet orgs;
            AnnotationSet URLs;
            String app="???";
            AnnotationSet persons;
            FeatureMap fm;

            Map<Object,Object> mp;
            Map<Object,Object> mp_usr;
            ArrayList<Double> coordinates;
            Double lati, longi;
            String usrName;
            String id;
            String creation;
            String text;
            int sent;
            int num_locs=0;
            int num_orgs=0;
            int num_urls=0;
            int num_pers=0;
            String newTextHeat = "";
            String newTextCircle = "";
            String color;
            color = "#FF00FF";
            String posColor = "#39d639";
            String negColor = "#c1140b";
            String neuColor = "#d9e01f";
            StringBuilder textToWriteHeat = new StringBuilder();
            StringBuilder textToWriteCircle = new StringBuilder();
            String sentiLabel;

            assert flist != null;

            for (File file : flist) {
                newTextHeat = "";
                newTextCircle = "";
                floc = file.getAbsolutePath();
                System.out.println(floc);
                d = Factory.newDocument(new URL("file:///" + floc), encoding);

                // Document
                tweets = d.getAnnotations("Original markups").get("Tweet");
                tweet = tweets.iterator().next();
                fm = tweet.getFeatures();
                mp = (Map<Object, Object>) fm.get("geo");
                mp_usr = (Map<Object, Object>) fm.get("user");
                id = (String) fm.get("id");
                creation = (String) fm.get("created_at");
                usrName = (String) mp_usr.get("screen_name");

                text = d.getContent().toString();
                if (mp != null) {
                    coordinates = (ArrayList<Double>) mp.get("coordinates");
                    lati = coordinates.get(0);
                    longi = coordinates.get(1);
                    System.out.println(lati);
                    if(d.getAnnotations().get("Locations").size() > 0) {
                        System.out.println("Hola");
                    }
                    num_locs = getNumberOfLocation(d);
                    num_orgs = getNumberOrganization(d);
                    num_pers = getNumberPerson(d);
                    num_urls = getNumberUrl(d);
                    sentiLabel = Sentiment(d);

                    //---- HEAT MAP ----
                    newTextHeat = "new google.maps.LatLng(" + lati + "," + longi + "),";

                    //---- CRCL MAP ----

                    StringBuilder newtext = new StringBuilder();
                    text = text.replace("\n", " ").replace("'", " ");
                    for (String token : text.split(" ")) {
                        if (token.contains("http")) {
                            newtext.append("<a href=\"").append(token).append("\" target=\"_blank\"> link </a>");
                        } else newtext.append(token).append(" ");
                    }

                    Assert.notNull(sentiLabel);

                    switch (sentiLabel){
                        case POS:
                            color = posColor;
                            break;
                        case NEG:
                            color = negColor;
                            break;
                        case NEU:
                            color = neuColor;
                            break;
                            default:
                                break;
                    }

                    newTextCircle = "  id" + id + ": {center: {lat: " + lati + ", lng: " + longi + "}," +
                            "color: '" + color + "'," +
                            "user: '" + usrName + " " + id + " ·+'," +
                            "application: '" + app + "'," +
                            "time: '" + creation + "'," +
                            "text: '(SENT: " + sentiLabel +
                            ", Locations: " + num_locs +
                            ", Organizations: " + num_orgs +
                            ", Persons: " + num_pers +
                            ", Urls: " + num_urls +
                            ", NewText: " + newtext + "'," +
                            "},";

                }
                textToWriteHeat.append(newTextHeat).append("\n");
                textToWriteCircle.append(newTextCircle).append("\n");
            }

            String fs = System.getProperty("file.separator");

            String inputFileHeat = "maps"+fs+ "heat-map-madrid.html";
            String inputFileCircle = "maps"+fs+ "circle-map-madrid.html";

            //other if here to decide which kind of map to create
            maps.MapsUtils.createNewMap(inputFileHeat, textToWriteHeat.toString());
            maps.MapsUtils.createNewMap(inputFileCircle, textToWriteCircle.toString());

        } catch (GateException | MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    private static int getNumberUrl(Document document) {
        return document.getAnnotations().get("Url").size();

    }

    private static int getNumberOrganization(Document document) {
        return document.getAnnotations().get("Organization").size();
    }

    private static int getNumberPerson(Document document) {
        return document.getAnnotations().get("Person").size();
    }

    private static int getNumberOfLocation(Document document) {
        return document.getAnnotations().get("Location").size();
    }
    
    public static String Sentiment(Document doc) {
        String sentiment="";
        int senti=0;
        AnnotationSet lookups=doc.getAnnotations("Sentiment").get("Lookup");
        Annotation lookup;
        FeatureMap fm;
        Iterator<Annotation> ite=lookups.iterator();
        String label;
        int pos = 0;
        int neg = 0;
                
        while(ite.hasNext()) {
            lookup = ite.next();
            fm = lookup.getFeatures();
            label = fm.get("majorType").toString();
            // compute sentiment value
            if (label.equals("positive"))
                pos = pos+1; 
            else if (label.equals("negative"))
                neg = neg+1;
        }
        senti = pos - neg;
        // compute sentiment label according to senti value
        if (senti < 0){
            sentiment = "NEG";
        }
        else if (senti > 0){
            sentiment = "POS";
        }
        else if (senti == 0){
            sentiment = "NEU";
        }
        return sentiment;
    }

    private static String getSentiment(Document document) {

        String sentiment = String.valueOf(document.getAnnotations().get("Sentiment"));

        switch (sentiment) {
            case POS:
                return POS;
            case NEG:
                return NEG;
            case NEU:
                return NEU;
            default:
                return null;
        }
    }
}