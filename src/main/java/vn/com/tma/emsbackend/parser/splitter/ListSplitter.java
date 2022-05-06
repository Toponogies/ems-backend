package vn.com.tma.emsbackend.parser.splitter;

import java.util.HashMap;
import java.util.Map;

public class ListSplitter {
    private final String[] lines;
    private final Map<String, String> keyValuePair;

    public ListSplitter(String executeResult){
        lines = executeResult.split("\n");
        keyValuePair = new HashMap<>();
    }
    public ListSplitter split(){
        for(String line:lines){
            if(line.trim().matches("^.*:.*$")){
                String[] tuple = line.split(":",2);
                keyValuePair.put(tuple[0].trim(), tuple[1].trim());
            }
        }
        return this;
    }
    public String get(String key){
        return keyValuePair.get(key);
    }
}
