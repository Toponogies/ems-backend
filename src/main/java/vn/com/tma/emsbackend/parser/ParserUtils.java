package vn.com.tma.emsbackend.parser;

public class ParserUtils {
    public static String getResultAfterCommandPrompt(String executeResult){
        return executeResult.split("(: )|(> )",2)[1].trim();
    }
}