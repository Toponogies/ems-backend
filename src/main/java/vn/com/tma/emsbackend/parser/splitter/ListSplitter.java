package vn.com.tma.emsbackend.parser.splitter;


import java.util.HashMap;
import java.util.Map;

import static vn.com.tma.emsbackend.parser.ParserUtils.getResultAfterCommandPrompt;

public class ListSplitter {
    private final String[] lines;
    private final Map<String, String> keyValuePair;

    public ListSplitter(String executeResult){
        executeResult = getResultAfterCommandPrompt(executeResult);
        lines = executeResult.split("\n");
        keyValuePair = new HashMap<>();
        this.split();
    }
    private void split(){
        for(String line:lines){
            if(isLineHasKeyValueFormat(line)){
                String[] tuple = line.split(":",2);
                keyValuePair.put(tuple[0].trim(), tuple[1].trim());
            }
        }
    }
    public String get(String key){
        return keyValuePair.get(key);
    }

    private boolean isLineHasKeyValueFormat(String line){
        return line.trim().matches("^.*:.*$");
    }
}