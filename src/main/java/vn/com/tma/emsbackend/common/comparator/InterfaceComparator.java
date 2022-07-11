package vn.com.tma.emsbackend.common.comparator;

import vn.com.tma.emsbackend.model.entity.Interface;

import java.util.Comparator;

public class InterfaceComparator implements Comparator<Interface> {
    @Override
    public int compare(Interface o1, Interface o2) {
        return o1.getName().compareTo(o2.getName());
    }
}