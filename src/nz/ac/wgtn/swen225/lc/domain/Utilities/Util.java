package nz.ac.wgtn.swen225.lc.domain.Utilities;

import java.util.Objects;

public class Util {
    public static void checkNull(Object o, String s) {
        if (Objects.isNull(o)) {
            throw new NullPointerException(s);
        }
    }
}
