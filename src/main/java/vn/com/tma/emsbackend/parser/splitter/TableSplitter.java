package vn.com.tma.emsbackend.parser.splitter;

import vn.com.tma.emsbackend.model.exception.ApplicationException;
import vn.com.tma.emsbackend.model.exception.ParserException;

import java.util.ArrayList;
import java.util.List;

import static vn.com.tma.emsbackend.parser.ParserUtils.getResultAfterCommandPrompt;

public class TableSplitter {
    private final String[] lines;
    private List<String> keys;
    private List<List<String>> rows;
    private List<Integer> columnLimitLengths;

    private int endHeaderLineIndex = 0;

    private int currentIndex = -1;

    public TableSplitter(String executeResult) {
        executeResult = getResultAfterCommandPrompt(executeResult);
        lines = executeResult.split("\n");
        this.split();
    }

    private void split() {
        //get index of end header line and limit data lengths each cell
        endHeaderLineIndex = getEndHeaderLineIndex();

        //get limit string size by column
        columnLimitLengths = getColumnLimitLengths();

        //get all keys
        keys = getKeys();

        //split
        rows = splitAllDataLines();
    }

    /**
     * Move cursor to next row
     *
     * @return if there is no more row
     */
    public boolean next() {
        if (currentIndex < rows.size() - 1) {
            currentIndex++;
            return true;
        }
        return false;
    }

    public String getValue(String key) {
        for (int keyIndex = 0; keyIndex < keys.size(); keyIndex++) {
            if (key.compareTo(keys.get(keyIndex)) == 0) {
                String value = rows.get(currentIndex).get(keyIndex);
                if (value.compareTo("---") == 0) {
                    return null;
                }
                return rows.get(currentIndex).get(keyIndex);
            }
        }
        return null;
    }


    private List<String> splitLine(String line)
    {
        List<String> result = new ArrayList<>();
        int curIndex = 0;
        for (Integer length : columnLimitLengths) {
            if (curIndex + length < line.length()) {
                result.add(line.substring(curIndex, curIndex + length).trim());
            } else {
                result.add(line.substring(curIndex).trim());
            }
            curIndex += length;
        }
        return result;
    }

    private List<List<String>> splitAllDataLines() {
        List<List<String>> rowsData = new ArrayList<>();
        for (int index = endHeaderLineIndex + 1; endHeaderLineIndex < lines.length; index++) {
            if (isEmptyLine(lines[index])){
                break;
            }
            rowsData.add(splitLine(lines[index]));
        }
        return rowsData;
    }

    private int getEndHeaderLineIndex() {
        for (int index = 0; index < lines.length; index++) {
            if (lines[index].trim().matches("^-.*-$")) {
                return index;
            }
        }
        throw new ParserException(String.join("\n",lines));
    }

    private List<Integer> getColumnLimitLengths() {
        List<Integer> limitLengths = new ArrayList<>();
        String[] tuple = lines[endHeaderLineIndex].split("(?= -)");
        for (String str : tuple) {
            limitLengths.add(str.length());
        }
        return limitLengths;
    }

    private List<String> getKeys() {
        return splitLine(lines[endHeaderLineIndex - 1]);
    }

    private boolean isEmptyLine(String line){
        return line.length() < 2;
    }

}