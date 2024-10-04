package nz.ac.wgtn.swen225.lc.domain.Utilities;

/**
 * Item colour for the Doors and Keys etc.
 * @author Yee Li
 */
public enum ItemColor {
    RED {
        @Override
        public String toString() { return "Red"; }
    },
    BLUE {
        @Override
        public String toString() { return "Blue"; }
    },
    GREEN {
        @Override
        public String toString() { return "Green"; }
    },
    InvalidColor
    ;
}
