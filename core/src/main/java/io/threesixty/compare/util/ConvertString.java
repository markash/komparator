package io.threesixty.compare.util;

import net.sf.flatpack.converter.Converter;
import net.sf.flatpack.util.ParserUtils;

public class ConvertString implements Converter {

    /*
     * (non-Javadoc)
     *
     * @see net.sf.flatpack.converter#convertValue(java.lang.String)
     */
    @Override
    public Object convertValue(final String valueToConvert) {
        return valueToConvert;
    }

}
