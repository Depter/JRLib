package org.jreserve.jrlib.triangle;

/**
 * Class that checks if the geometry of the triangle complies to the 
 * contract of the {@link Triangle Triangle} interface.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleDimensionCheck {
    
    static void validateDimensions(Triangle triangle) {
        int accidents = triangle.getAccidentCount();
        if(accidents == 0) {
            validateEmptyDevelopments(triangle);
        } else if (accidents == 1) {
            validateDevCountGeneralFirst(triangle);
            validateNotEmptyAccident(triangle, 0);
        } else {
            validateDevCountGeneralFirst(triangle);
            validateNotEmptyAccident(triangle, 0);
            
            int dif = getDevelopmentDifference(triangle);
            for(int a=1; a<accidents; a++) {
                validateNotEmptyAccident(triangle, a);
                validateDevDifference(triangle, a, dif);
            }
        }
    }
    
    private static void validateEmptyDevelopments(Triangle triangle) {
        if(triangle.getDevelopmentCount() > 0) {
            String msg = "Triangle does not have any accident periods, "+
                    "but it has development periods!";
            throw new IllegalArgumentException(msg);
        }
        
    }
    
    private static void validateDevCountGeneralFirst(Triangle triangle) {
        int devs = triangle.getDevelopmentCount();
        int d0 = triangle.getDevelopmentCount(0);
        if(devs != d0) {
            String msg = "General development count (%d) must be the same as the "+
                    "numer of developments in the first accident period (%d)!";
            throw new IllegalArgumentException(String.format(msg, devs, d0));
        }
    }
    
    private static void validateNotEmptyAccident(Triangle triangle, int accident) {
        int devs = triangle.getDevelopmentCount(accident);
        if(devs <= 0) {
            String msg = "Accident period [%d] contains no development periods!";
            throw new IllegalArgumentException(String.format(msg, devs));
        }
    }
    
    private static int getDevelopmentDifference(Triangle triangle) {
        int d0 = triangle.getDevelopmentCount();
        int d1 = triangle.getDevelopmentCount(1);
        if(d1 >= d0)
            throw firstNotLongerThenSecond(d0, d1);
        return d0 - d1;
    }
    
    private static IllegalArgumentException firstNotLongerThenSecond(int d0, int d1) {
        String msg = "First accident period must contain more development "+
                "then the second! First: %d, Second: %d.";
        msg = String.format(msg, d0, d1);
        return new IllegalArgumentException(msg);
    }

    private static void validateDevDifference(Triangle triangle, int accident, int dif) {
        int d1 = triangle.getDevelopmentCount(accident-1);
        int d2 = triangle.getDevelopmentCount(accident);
        if(dif != (d1-d2)) {
            String msg = "Unexpected difference in length between accident "+
                    "period [%d] and [%d]! Difference is [%d], expected [%d].";
            msg = String.format(msg, accident-1, accident, d1-d2, dif);
            throw new IllegalArgumentException(msg);
        }
    }
}
