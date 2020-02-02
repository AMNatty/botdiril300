package cz.tefek.botdiril.util;

import java.util.Locale;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import cz.tefek.botdiril.framework.util.BigNumbers;

public class BotdirilFmt
{
    public static String format(double number)
    {
        var formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator('\'');

        String format = "#,##0.##";
        DecimalFormat numberFormatter = new DecimalFormat(format, formatSymbols);
        numberFormatter.setGroupingSize(3);

        /*
        if (number > 100_000 || number < -100_000)
        {
            var sign = number < 0 ? "-" : "";
            return numberFormatter.format(number) + " (" + sign + stringifyBigNumber(BigDecimal.valueOf(Math.abs(number))) + ")";
        }
        */

        return numberFormatter.format(number);
    }

    public static String format(long number)
    {
        var formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setGroupingSeparator('\'');

        String format = "#,##0";
        DecimalFormat numberFormatter = new DecimalFormat(format, formatSymbols);
        numberFormatter.setGroupingSize(3);

        /*
        if (number > 100_000 || number < -100_000)
        {
            var sign = number < 0 ? "-" : "";
            return numberFormatter.format(number) + " (" + sign + stringifyBigNumber(BigDecimal.valueOf(Math.abs(number))) + ")";
        }
        */

        return numberFormatter.format(number);
    }

    public static String stringifyBigNumber(BigDecimal bigInteger)
    {
        NumberFormat formatter = new DecimalFormat("0.##E0", DecimalFormatSymbols.getInstance(Locale.ROOT));
        formatter.setMaximumIntegerDigits(3);

        var orig = formatter.format(bigInteger);

        var strb = orig.split("E");

        var nrp = strb[0];
        var scf = Integer.parseInt(strb[1]);

        return nrp + " " + BigNumbers.getNameOfExp(scf);
    }
}
