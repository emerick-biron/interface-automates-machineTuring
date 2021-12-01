package machines.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FichiersMachine {
    public static String ajoutBalises(String nomBalise, Object contenuBalise){
        return "<"+nomBalise+">"+contenuBalise+"</"+nomBalise+">";
    }

    public static String getContenuBalise(String nomBalise, String donnes){
        Pattern pattern = Pattern.compile("(?<=<"+nomBalise+">)(.*)(?=</"+nomBalise+">)");
        Matcher matcher = pattern.matcher(donnes);
        if (matcher.find()) return matcher.group();
        else return "";
    }
}
