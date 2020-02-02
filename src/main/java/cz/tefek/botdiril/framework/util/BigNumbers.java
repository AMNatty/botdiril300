package cz.tefek.botdiril.framework.util;

import java.util.Locale;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import cz.tefek.botdiril.BotMain;

public class BigNumbers
{
    private static BiMap<Integer, String> bigIntMap = HashBiMap.create();

    public static void load()
    {
        try
        {
            var lines = Files.readAllLines(new File("assets/bignumbers.txt").toPath());

            lines.forEach(BigNumbers::procLine);
        }
        catch (IOException e)
        {
            BotMain.logger.fatal("Error retrieving big number lookup data.", e);
            System.exit(7);
        }
    }

    public static Integer getExpForName(String name)
    {
        return bigIntMap.inverse().get(name);
    }

    public static String getNameOfExp(Integer exp)
    {
        return bigIntMap.get(exp);
    }

    private static void procLine(String line)
    {
        if (line.trim().isEmpty())
        {
            return;
        }

        var lp = line.trim().split(",");

        if (lp.length != 2)
        {
            return;
        }

        var ni = Integer.parseInt(lp[0]);
        var sn = lp[1].trim();

        bigIntMap.put(ni, sn);
    }

    public static String stringifyScientific(BigInteger bigInteger)
    {
        if (bigInteger.equals(BigInteger.ZERO))
        {
            return "0";
        }

        if (bigInteger.compareTo(BigInteger.valueOf(9999)) == -1)
        {
            return String.format("%d", bigInteger.intValueExact());
        }

        NumberFormat formatter = new DecimalFormat("0.##E0", DecimalFormatSymbols.getInstance(Locale.ROOT));
        formatter.setMaximumIntegerDigits(3);

        var orig = formatter.format(bigInteger);

        return orig;
    }

    public static String stringify(BigInteger bigInteger)
    {
        if (bigInteger.equals(BigInteger.ZERO))
        {
            return "0";
        }

        if (bigInteger.compareTo(BigInteger.valueOf(9999)) == -1)
        {
            return String.format("%d", bigInteger.intValueExact());
        }

        NumberFormat formatter = new DecimalFormat("0.##E0", DecimalFormatSymbols.getInstance(Locale.ROOT));
        formatter.setMaximumIntegerDigits(3);

        var orig = formatter.format(bigInteger);

        var strb = orig.split("E");

        var nrp = strb[0];
        var scf = Integer.parseInt(strb[1]);

        return nrp + " " + bigIntMap.get(scf);
    }

    public static String stringifyBoth(BigInteger bigInteger)
    {
        if (bigInteger.equals(BigInteger.ZERO))
        {
            return "0";
        }

        if (bigInteger.compareTo(BigInteger.valueOf(9999)) == -1 && bigInteger.compareTo(BigInteger.valueOf(-9999)) == 1)
        {
            return String.format("%d", bigInteger.intValueExact());
        }

        NumberFormat formatter = new DecimalFormat("0.##E0", DecimalFormatSymbols.getInstance(Locale.ROOT));
        formatter.setMaximumIntegerDigits(3);

        var orig = formatter.format(bigInteger);

        var strb = orig.split("E");

        var nrp = strb[0];
        var scf = Integer.parseInt(strb[1]);

        return nrp + " " + bigIntMap.get(scf) + " (" + orig + ")";
    }
}
