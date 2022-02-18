package vn.com.tma.emsbackend.common;

public class Mapper {
    public static <T, N> N map(T source, Class<N> destinationClass) {
        return Constant.modelMapper.map(source, destinationClass);
    }
}
