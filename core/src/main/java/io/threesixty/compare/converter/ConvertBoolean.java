package io.threesixty.compare.converter;

import net.sf.flatpack.converter.Converter;
import net.sf.flatpack.util.ParserUtils;

import java.util.HashSet;
import java.util.Set;

public class ConvertBoolean implements Converter {

    private static final Set<String> trueValues = new HashSet<String>(4);
    private static final Set<String> falseValues = new HashSet<String>(4);

    static {
        trueValues.add("true");
        trueValues.add("on");
        trueValues.add("yes");
        trueValues.add("1");

        falseValues.add("false");
        falseValues.add("off");
        falseValues.add("no");
        falseValues.add("0");
    }

    @Override
    public Object convertValue(final String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        value = value.toLowerCase();
        if (trueValues.contains(value)) {
            return Boolean.TRUE;
        }
        else if (falseValues.contains(value)) {
            return Boolean.FALSE;
        }
        else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }
    }

}
