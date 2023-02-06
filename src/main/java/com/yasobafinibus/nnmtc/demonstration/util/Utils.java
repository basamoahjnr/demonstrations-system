package com.yasobafinibus.nnmtc.demonstration.util;


import com.opencsv.bean.CsvToBeanBuilder;
import com.yasobafinibus.nnmtc.demonstration.domain.ProcedureDto;
import jakarta.enterprise.context.Dependent;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Dependent
public class Utils implements Serializable {

    public static String TEMP_FOLDER = System.getProperty("java.io.tmpdir") +"/";

    public static Object getPropertyValueViaReflection(Object o, Field field) {
        //find correct Method
        for (Method method : o.getClass().getMethods()) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
                    // MZ: Method found, run it
                    try {
                        return method.invoke(o);
                    } catch (IllegalAccessException | InvocationTargetException ignored) {

                    }

                }
            }
        }
        return null;
    }


    public static void copyFile(String fileName, InputStream in, String destination) {
        try {
            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(new File(destination + fileName));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            System.out.println("New file uploaded: " + (destination + fileName));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @SafeVarargs
    public static <T> List<T> getListWithoutDuplicates(List<T> list, Function<T, ?>... keyFunctions) {

        List<T> result = new ArrayList<>();

        Set<List<?>> set = new HashSet<>();

        for(T element : list) {
            List<?> functionResults = Arrays.stream(keyFunctions)
                    .map(function -> function.apply(element))
                    .collect(Collectors.toList());

            if(set.add(functionResults)) {
                result.add(element);
            }
        }

        return result;
    }


}


