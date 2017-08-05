package io.threesixty.compare.converter;

import net.sf.flatpack.converter.Converter;
import net.sf.flatpack.util.ParserUtils;

public class ConvertLong implements Converter {

    /*
     * (non-Javadoc)
     *
     * @see net.sf.flatpack.converter#convertValue(java.lang.String)
     */
    @Override
    public Object convertValue(final String valueToConvert) {
        return Long.valueOf(ParserUtils.stripNonLongChars(valueToConvert));
    }

}
