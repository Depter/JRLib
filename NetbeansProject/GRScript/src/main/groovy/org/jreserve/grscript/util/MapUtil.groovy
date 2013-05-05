package org.jreserve.grscript.util

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MapUtil {
    
    private static MapUtil INSTANCE = new MapUtil()
    
    static MapUtil getInstance() {
        return INSTANCE
    }
    
    private MapUtil() {
    }
    
    int getAccident(Map map) {
        return getInt(map, "accident", "a")
    }
	
    int getDevelopment(Map map) {
        return getInt(map, "development", "d")
    }
    
    int getInt(Map map, String... names) {
        def value = getValue(map, names)
        switch(value) {
            case Number: return ((Number)value).intValue()
            default:
                throw new IllegalArgumentException("Value '${value}' can not be used as integer!")
        }
    }
    
    def getValue(Map map, String... names) {
        def result = map.find {key, value -> 
            ((key instanceof String) && containsKey(key, names))
        }

        if(result)
            return result.value
        String parameters = getParamNames(names)
        throw new IllegalArgumentException("Parameter '${parameters}' not found in the parameters")
    }
    
    private boolean containsKey(String key, String... names) {
        for(name in names)
            if(key.equalsIgnoreCase(name))
                return true
        return false
    }
    
    private String getParamNames(String... names) {
        if(names.length == 1)
            return names[0]
        
        String str = ""
        for(name in names)
            str += str.length()==0? "{$name" : " | $name"
        str += "}"
        return str
    }
	
    int getDouble(Map map, String... names) {
        def value = getValue(map, names)
        switch(value) {
            case Number: return ((Number)value).doubleValue()
            default:
                throw new IllegalArgumentException("Value '${value}' can not be used as double!")
        }
    }
	
    boolean getBoolean(Map map, String... names) {
        def value = getValue(map, names)
        if(value)
            return true
        return false
    }
	
    String getString(Map map, String... names) {
        return getValue(map, names)
    }	
}

