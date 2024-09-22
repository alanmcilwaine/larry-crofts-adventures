package nz.ac.wgtn.swen225.lc.domain.Utilities;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Util {
    public static void checkNull(Object o, String s) {
        if (Objects.isNull(o)) {
            throw new NullPointerException(s);
        }
    }

    public static <T> void checkNull(List<T> o) {
        for(var e: o) {
            checkNull(e, e.toString());
        }
    }

    public static void checkNegative(String property, int number) {
        if(number <0) {
            throw new IllegalArgumentException(String.format("%s can't be negative, currently is %s", property,number));
        }
    }

    public static void checkNegative(Map<String, Integer> ls) {
        for(var e: ls.entrySet()) {
            checkNegative(String.format("%s is valid,", e.getKey()), e.getValue());
        }
    }
}
