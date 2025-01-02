package com.twistercambodia.karasbackend.utils;
import org.modelmapper.ModelMapper;

public class MappingUtils {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static <S, T> T map(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }
}