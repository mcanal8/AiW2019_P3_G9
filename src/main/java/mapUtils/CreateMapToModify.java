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
    private static final String POSITIVE = "POSITIVE";
    private static final String POS = "POS";
    private static final String NEGATIVE = "NEGATIVE";
    private static final String NEG = "NEG";
    private static final String NEU = "NEU";

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
            File inDir = new File("./data/analysed2");
            File[] flist = inDir.listFiles();
            String floc;

            Document d;
            Annotation tweet;
            AnnotationSet tweets;
            String app="???";
            FeatureMap fm;

            Map<Object,Object> mp;
            Map<Object,Object> mpUsr;
            ArrayList<Double> coordinates;
            Double lati;
            Double longi;
            String usrName;
            String id;
            String creation;
            String text;
            int num_locs;
            int num_orgs;
            int num_urls;
            int num_pers;
            String newTextHeat = "";
            String newTextCircle = "";
            String color;
            color = "#FF00FF";
            String posColor = "#39d639";
            String negColor = "#c1140b";
            String neuColor = "#d9e01f";
            StringBuilder textToWriteHeat = new StringBuilder();
            StringBuilder textToWriteCircle = new StringBuilder();
            String sentiLabel = null;
            int numOfGeoFound = 0;

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
                mpUsr = (Map<Object, Object>) fm.get("user");
                id = (String) fm.get("id");
                creation = (String) fm.get("created_at");
                usrName = (String) mpUsr.get("screen_name");

                text = d.getContent().toString();
                if (mp != null) {
                    coordinates = (ArrayList<Double>) mp.get("coordinates");
                    lati = coordinates.get(0);
                    longi = coordinates.get(1);
                    numOfGeoFound++;
                    System.out.println(numOfGeoFound + " of " + flist.length);

                    num_locs = getNumberOfLocation(d);
                    num_orgs = getNumberOrganization(d);
                    num_pers = getNumberPerson(d);
                    num_urls = getNumberUrl(d);

                    try {
                        sentiLabel = getSentiment(d);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

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

                    switch (sentiLabel) {
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
            MapsUtils.createNewMap(inputFileHeat, textToWriteHeat.toString());
            MapsUtils.createNewMap(inputFileCircle, textToWriteCircle.toString());

        } catch (GateException | MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    private static int getNumberUrl(Document document) {
        return document.getAnnotations().get("URL").size();

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

    private static String getSentiment(Document document) throws Exception{

        AnnotationSet sentimentsSets = document.getNamedAnnotationSets().get("Sentiment");
        Annotation lookup;
        FeatureMap fm;
        Iterator<Annotation> ite= sentimentsSets.iterator();
        String label;
        int pos = 0;
        int neg = 0;

        while(ite.hasNext()) {
            lookup = ite.next();
            fm = lookup.getFeatures();
            label = fm.get("majorType").toString();

            // compute sentiment value

            if (label.equals(POSITIVE)) {
                pos = pos + 1;
            } else if (label.equals(NEGATIVE)) {
                neg = neg + 1;
            } else {
                throw new Exception("Unexpected label.");
            }
        }

        int sentimentNumber = pos - neg;
        // compute sentiment label according to senti value
        if (sentimentNumber < 0){
            return NEG;
        }
        else if (sentimentNumber > 0){
            return POS;
        }
        else {
            return NEU;
        }
    }
}