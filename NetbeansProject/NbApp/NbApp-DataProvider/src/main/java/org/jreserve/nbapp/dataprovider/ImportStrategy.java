package org.jreserve.nbapp.dataprovider;

import java.util.Set;

/**
 * Specifies the strategy to use, when the user imports new data records.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface ImportStrategy {
    
    /**
     * The method should merge the two records to a single set.
     * 
     * @param oldRecors The set of already existing records.
     * @param importedRecords The records, the user wants to import.
     * @return The new set of records, merged, according to this strategy.
     */
    public Set<DataRecord> getRecords(Set<DataRecord> oldRecors, Set<DataRecord> importedRecords);
}
