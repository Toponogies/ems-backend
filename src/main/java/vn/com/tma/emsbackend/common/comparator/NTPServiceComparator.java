package vn.com.tma.emsbackend.common.comparator;

import vn.com.tma.emsbackend.model.entity.NTPServer;

import java.util.Comparator;

public class NTPServiceComparator implements Comparator<NTPServer> {
    @Override
    public int compare(NTPServer o1, NTPServer o2) {
        return o1.getServerAddress().compareTo(o2.getServerAddress());
    }
}
