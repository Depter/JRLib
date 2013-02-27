package org.jreserve.triangle;

/**
 *
 * @author Peter Decsi
 */
public enum TriangleOperation {
    ADD {
        @Override
        public double operate(double v1, double v2) {
            return v1+v2;
        }
    },
    SUBTRACT {
        @Override
        public double operate(double v1, double v2) {
            return v1-v2;
        }
    },
    MULTIPLY {
        @Override
        public double operate(double v1, double v2) {
            return v1 * v2;
        }
    },
    DIVIDE {
        @Override
        public double operate(double v1, double v2) {
            return v2==0d? Double.NaN :  v1 / v2;
        }
    };
    
    public abstract double operate(double v1, double v2);
}
