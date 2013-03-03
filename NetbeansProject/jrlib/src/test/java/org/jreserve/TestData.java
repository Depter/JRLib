package org.jreserve;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jreserve.triangle.InputTriangle;
import org.jreserve.triangle.Triangle;
import org.jreserve.triangle.TriangleCummulation;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TestData {
    
    public final static String INCURRED = "/org/jrlib/testdata/apc_incurred.csv";
    public final static String PAID = "/org/jrlib/testdata/apc_paid.csv";
    public final static String NoC = "/org/jrlib/testdata/apc_noc.csv";
    public final static String EXPOSURE = "/org/jrlib/testdata/apc_exposure.csv";
    public final static String MACK_DATA = "/org/jrlib/testdata/mack_data.csv";
    public final static String MACK_DATA2 = "/org/jrlib/testdata/mack_data2.csv";
    public final static String Q_INCURRED = "/org/jrlib/testdata/q_incurred.csv";
    public final static String Q_PAID = "/org/jrlib/testdata/q_paid.csv";

    private final static Map<String, double[][]> CAHCE = new HashMap<String, double[][]>();
    
    public static double[][] getCachedMatrix(String path) {
        double[][] result = CAHCE.get(path);
        if(result == null) {
            result = readMatrix(path);
            CAHCE.put(path, result);
        }
        return result;
    }
    
    public static double[] getCachedVector(String path) {
        return getCachedMatrix(path)[0];
    }
    
    public static double[][] getMatrix(String path) {
        return readMatrix(path);
    }
    
    public static double[] getVector(String path) {
        return getMatrix(path)[0];
    }
    
    public static Triangle getTriangle(String path) {
        return new InputTriangle(getCachedMatrix(path));
    }
    
    public static Triangle getCummulatedTriangle(String path) {
        return new TriangleCummulation(getTriangle(path));
    }
    
//    public final static double[][] INCURRED = {
//        {4873559, 853069 , -83150 ,  59732 ,  83881 ,  71846, 3624, -70663},
//        {5130849, 967272 ,  79653 , -12315 ,  126541,  12102, 235 },
//        {5945611, 1580045,  32159 , -180480,  64785 , -23254},
//        {6632221, 1228881, -328165, -4469  , -2600},	
//        {7020974, 1667612,  24278 , -37897 },		
//        {8275453, 1591873,  64978 },			
//        {9000368, 1239520},				
//        {9511539}
//    };
//    
//    public final static double[][] PAID = {
//        {4426765, 992330 , 88952 , 13240 , 38622 , 26720, 36818, 10750},
//        {4388958, 984169 , 60162 , 35004 , 75768 , 23890, 572},
//        {5280130, 1239396, 76122 , 110189, 112895, 11751},
//        {5445384, 1164234, 171583, 16427 , 6451  },
//        {5612138, 1837950, 155863, 127146},
//        {6593299, 1592418, 74189 },
//        {6603091, 1659748},
//        {7194587}
//    };
//    
//    public final static double[][] NoC = {
//        {26373, 1173, 14, 4 , 2, 11, 0, 0},
//        {27623, 1078, 19, 11, 8, 0 , 0},
//        {30908, 1299, 27, 17, 2, 1},
//        {31182, 1392, 46, 2 , 1},
//        {32855, 1754, 46, 15},
//        {37661, 1634, 54},
//        {38160, 1696},
//        {40194}
//    };
//    
//    public final static double[] EXPOSURE = {
//        345872,
//        395282,
//        444693,
//        494103,
//        544932,
//        620275,
//        668107,
//        718563
//    };
//    
//    public final static double[][] Q_PAID = {
//        {3, 21, 41, 76 , 132, 145, 132, 142, 122, 62 , 40 , 43, 9  , 34, 18, 11, 10, -6, 10, 9 , 6 , 8 , 7 , 4 , 2 , 3 , 2 , 3 , 2, 3, 1, 1 , 1, 1, 0, 1, 0, 1, -2, 0, 0, 1, 0, 1, 0},
//        {1, 15, 38, 81 , 125, 138, 196, 164, 113, 93 , 53 , 35, 37 , 19, 33, 34, 19, 14, 9 , 9 , 5 , 12, 6 , 3 , 1 , 3 , 2 , 3 , 5, 1, 1, 20, 0, 1, 2, 5, 0, 1,  1, 0, 0},
//        {1, 16, 38, 111, 130, 146, 145, 114, 110, 80 , 49 , 36, 24 , 22, 21, 16, 9 , 12, 11, 8 , 5 , 26, 5 , 3 , 3 , 6 , 16, 21, 1, 1, 3, 3 , 2, 1, 2, 0, 1},
//        {1, 10, 29, 53 , 92 , 158, 131, 169, 101, 87 , 71 , 49, 38 , 33, 31, 21, 19, 39, 13, 17, 15, 15, 5 , 16, 12, 12, 28, 9 , 1, 3, 8, 2 , 5},
//        {1, 13, 33, 66 , 112, 154, 191, 145, 117, 123, 93 , 70, 65 , 31, 79, 34, 36, 20, 13, 42, 19, 17, 30, 17, 11, 15, 12, 6 , 8},
//        {1, 5 , 22, 72 , 94 , 103, 118, 106, 95 , 81 , 61 , 52, 49 , 33, 24, 19, 15, 17, 15, 22, 9 , 15, 8 , 10, 8},
//        {1, 6 , 30, 91 , 143, 156, 152, 143, 116, 99 , 83 , 71, 69 , 47, 32, 66, 18, 24, 18, 10, 12},
//        {1, 9 , 35, 65 , 126, 206, 226, 222, 188, 120, 127, 87, 112, 91, 38, 67, 40},
//        {1, 8 , 22, 63 , 98 , 107, 109, 384, 81 , 76 , 70 , 45, 36 },
//        {4, 12, 33, 121, 119, 153, 159, 192, 155},
//        {1, 6 , 29, 61 , 86 },
//        {1}
//    };
//    
//    public final static double[][] Q_INCURRED = {
//        {44, 52, 98 , 226, 201,  94 , 33 , 158, 44 ,  23 , 24, 33, -10 ,  15 , 20 , 17, -2 , -19, 11, 8 , -1 , 7 , 5 ,  7 ,  1,  2 , -1 ,  4 ,  0, 1 ,  3, -2 ,  1, 0,  4, -3,  1, 4, -3, -2,  2, 0, -2, 3, -1},
//        {42, 94, 66 , 163, 176,  110, 166, 171, 64 ,  70 , 17, 34,  -4 ,  5  , 31 , 20,  13, -10, 11, 15, -5 , 13, 2 ,  3 , -1, -3 ,  15, -5 , -4, 10,  2,  18, -3, 6,  1, -3,  0, 2, -2,  3, -3},
//        {17, 26, 92 , 245, 150,  184, 99 , 132, 21 ,  42 , 20, 41,  -5 ,  9  , 13 , 26, -12,  11, 4 , 13,  0 , 14, 28, -11, -4,  12,  10,  17,  2, 2 , -1,  1 ,  3, 0, -1,  3, -3},
//        {10, 33, 64 , 131, 155,  181, 158, 162, 41 ,  32 , 52, 18,  25 ,  17 , 20 , 39, -12,  37, 35, 9 ,  2 , 6 , 1 ,  24,  3,  16,  22,  3 ,  2, 3 ,  3,  5 ,  1},
//        {13, 28, 68 , 197, 175,  176, 164, 186, 14 ,  120, 30, 78,  18 ,  22 , 69 , 42,  0 ,  9 , 28, 31,  8 , 12, 36,  26,  0,  11,  5 ,  19, -2},
//        {2 , 27, 59 , 166, 126,  121, 114, 120, 53 ,  54 , 70, 3 ,  38 ,  10 , 14 , 13,  11,  4 , 8 , 16,  1 , 26, 0 ,  10,  0},
//        {4 , 21, 126, 182, 444, -114, 193, 132, 75 ,  104, 32, 43,  65 , -2  , 43 , 14,  0 ,  14, 0 , 7 ,  28},
//        {2 , 32, 81 , 175, 182,  337, 245, 489, 74 , -112, 94, 96,  123, -102, 103, 20, -19},
//        {3 , 16, 71 , 602, -95,  332, -46, 234, -25,  84 , 22, 32,  -9 },
//        {4 , 34, 100, 233, 212,  173, 146, 209, 101},
//        {21, 58, 36 , 184, 123},
//        {13}
//    };
//    
//    public final static double[][] MACK_DATA = {
//        {58046, 69924 , 348629 , 551093 , 332797 , 286821, 171869, 87673, 43253},
//        {24492, 117275, 842521 , 1158368, 819322 , 721962, 364958, 66862},
//        {32848, 241834, 1247955, 1680790, 1242500, 712854, 183804},
//        {21439, 508389, 2370473, 2098718, 1461093, 393792},
//        {40397, 722997, 2157351, 2068827, 658991 },
//        {90748, 861246, 3258646, 1655842},
//        {62096, 806384, 1086317},
//        {24983, 259458},
//        {13121}
//    };
    
    private static double[][] readMatrix(String path) {
        BufferedReader reader = null;
        try {
            List<double[]> values = new ArrayList<double[]>();
            reader = new BufferedReader(new InputStreamReader(TestData.class.getResourceAsStream(path)));
            String line;
            while((line = reader.readLine()) != null)
                if(!line.startsWith("#"))
                    values.add(lineToDouble(line));
            
            int size = values.size();
            double[][] result = new double[size][];
            for(int i=0; i<size; i++)
                result[i] = values.get(i);
            return result;
            
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if(reader != null)
                try {reader.close();} catch (Exception ex) {}
        }
    }
    
    private static double[] lineToDouble(String line) {
        String[] lineArr = line.split(",");
        int size = lineArr.length;
        double[] result = new double[size];
        
        for(int i=0; i<size; i++)
            result[i] = Double.parseDouble(lineArr[i].trim());
        return result;
    }
}
