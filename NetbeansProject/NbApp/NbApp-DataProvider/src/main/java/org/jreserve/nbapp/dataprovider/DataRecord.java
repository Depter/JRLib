package org.jreserve.nbapp.dataprovider;

import java.util.Date;
import org.joda.time.LocalDate;

/**
 * Instances of DataRecord are representing one claim or exposure
 * data. A data record is identified by it's accident and development date.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class DataRecord implements Comparable<DataRecord>{
    
    private final LocalDate accident;
    private final LocalDate development;
    private final double value;
    
    /**
     * Creates an instance for the given dates. The time part of the date will
     * be
     * 
     * @throws NullPointerException if `accident` or `development` is null.
     */
    public DataRecord(Date accident, Date development, double value) {
        this.accident = new LocalDate(accident);
        this.development = new LocalDate(development);
        this.value = value;
    }
    
    public Date getAccidentDate() {
        return accident.toDate();
    }
    
    public Date getDevelopmentDate() {
        return development.toDate();
    }
    
    public double getValue() {
        return value;
    }
    
    @Override
    public int compareTo(DataRecord record) {
        int dif = accident.compareTo(record.accident);
        return dif!=0? dif : development.compareTo(development);
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof DataRecord) &&
               compareTo((DataRecord)o) == 0;
    }
    
    @Override
    public int hashCode() {
        int hash = 31 * accident.hashCode();
        return 17 * hash + development.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("DataRecord[%s; %s; %f]", 
                accident.toString(), development.toString(), 
                value);
    }
}
