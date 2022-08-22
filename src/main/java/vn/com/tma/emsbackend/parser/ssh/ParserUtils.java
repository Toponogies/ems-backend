package vn.com.tma.emsbackend.parser.ssh;

public class ParserUtils {
    private ParserUtils() {
    }

    public static String getResultAfterCommandPrompt(String executeResult) {
        return executeResult.split("(: )|(> )", 2)[1].trim();
    }
}