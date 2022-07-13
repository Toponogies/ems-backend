package vn.com.tma.emsbackend.common.comparator;


import vn.com.tma.emsbackend.model.entity.Port;

import java.util.Comparator;

public class PortComparator implements Comparator<Port> {
    @Override
    public int compare(Port o1, Port o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
