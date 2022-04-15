package vn.com.tma.emsbackend.parser;

import vn.com.tma.emsbackend.exception.ApplicationException;

import java.util.ArrayList;
import java.util.List;

public class TableReader {
    private final String[] lines;
    private List<String> keys;
    private List<List<String>> rows;
    private List<Integer> columnLimitLengths;

    private int endHeaderLineIndex = 0;

    private int currentIndex = -1;

    public TableReader(String commandResult) {
        lines = commandResult.split("\n");
    }

    public TableReader split() {
        //get index of end header line and limit data lengths each cell
        endHeaderLineIndex = getEndHeaderLineIndex();

        //get limit string size by column
        columnLimitLengths = getColumnLimitLengths();

        //get all keys
        keys = getKeys();

        //split
        rows = splitAllDataLines();

        return this;
    }

    /**
     * Move cursor to next row
     *
     * @return if there is no more row
     */
    public boolean next() {
        if (currentIndex < rows.size()) {
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


    private List<String> splitLine(String line) {
        //split line into list of string, which each string is data of a column
        List<String> result = new ArrayList<>();
        int curIndex = 0;
        for (Integer length : columnLimitLengths) {
            if (curIndex + length < line.length()) {
                result.add(line.substring(curIndex, curIndex + length).trim());
            } else {
                result.add(line.substring(curIndex, line.length() - 1).trim());
            }
            curIndex += length + 1;
        }
        return result;
    }

    private List<List<String>> splitAllDataLines() {
        List<List<String>> rowsData = new ArrayList<>();
        for (int index = endHeaderLineIndex + 1; endHeaderLineIndex < lines.length; endHeaderLineIndex++) {
            //empty line
            if (lines[endHeaderLineIndex].length() < 2) {
                break;
            }
            rowsData.add(splitLine(lines[index]));
        }
        return rowsData;
    }

    private int getEndHeaderLineIndex() {
        for (int index = 0; index < lines.length; index++) {
            if (lines[index].matches("^-.*-$")) {
                return index;
            }
        }
        throw new ApplicationException("Command result has wrong format");
    }

    private List<Integer> getColumnLimitLengths() {
        List<Integer> limitLengths = new ArrayList<>();
        String[] tuple = lines[endHeaderLineIndex].split(" ");
        for (String str : tuple) {
            limitLengths.add(str.length());
        }
        return limitLengths;
    }

    private List<String> getKeys() {
        return splitLine(lines[endHeaderLineIndex - 1]);
    }
}
