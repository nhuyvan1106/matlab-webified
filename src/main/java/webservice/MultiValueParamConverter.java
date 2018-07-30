package webservice;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class MultiValueParamConverter implements ParamConverterProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.getName() == List.class.getName() && genericType.getTypeName().contains("Integer"))
            return (ParamConverter<T>) new MultiIntegerValueParamConverter();
        if (rawType.getName() == List.class.getName() && genericType.getTypeName().contains("Double"))
            return (ParamConverter<T>) new MultiDoubleValueParamConverter();
        if (rawType.getName() == List.class.getName() && genericType.getTypeName().contains("String"))
            return (ParamConverter<T>) new MultiStringValueParamConverter();
        return null;
    }

    private class MultiIntegerValueParamConverter implements ParamConverter<List<Integer>> {

        @Override
        public List<Integer> fromString(String value) {
            if (value.contains(","))
                return unmodifiableList(
                        Arrays.stream(value.split(",\\s*"))
                                .map(Integer::parseInt)
                                .collect(toList()));
            return null;
        }

        @Override
        public String toString(List<Integer> value) {
            return value.stream()
                    .map(String::valueOf)
                    .collect(joining(",", "[", "]"));
        }

    }

    private class MultiDoubleValueParamConverter implements ParamConverter<List<Double>> {

        @Override
        public List<Double> fromString(String value) {
            if (value.contains(","))
                return unmodifiableList(
                        Arrays.stream(value.split(",\\s*"))
                                .map(Double::parseDouble)
                                .collect(toList()));
            return null;
        }

        @Override
        public String toString(List<Double> value) {
            return value.stream()
                    .map(String::valueOf)
                    .collect(joining(",", "[", "]"));
        }

    }

    private class MultiStringValueParamConverter implements ParamConverter<List<String>> {

        @Override
        public List<String> fromString(String value) {
            if (value.contains(","))
                return unmodifiableList(
                        Arrays.stream(value.split(",\\s*"))
                                .collect(toList()));
            return null;
        }

        @Override
        public String toString(List<String> value) {
            return value.stream()
                    .collect(joining(",", "[", "]"));
        }

    }
}