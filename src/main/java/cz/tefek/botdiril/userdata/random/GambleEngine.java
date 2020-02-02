package cz.tefek.botdiril.userdata.random;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import net.dv8tion.jda.api.EmbedBuilder;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.internal.GlobalProperties;
import cz.tefek.botdiril.userdata.item.Icons;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.botdiril.userdata.xp.XPRewards;

public class GambleEngine
{
    public enum GambleOutcome
    {
        LOSE_EVERYTHING("Lose everything", "You lost everything - %d " + Icons.KEK + "... Better luck next time.", 15, (level, bet) -> -bet),
        LOSE_THREE_QUARTERS("Lose 75%", "You lost 75%% of your bet. You lost %d " + Icons.KEK + ". Pretty unlucky.", 40, (level, bet) -> Math.round(-bet * 0.75)),
        LOSE_HALF("Lose half", "You lost half of your bet. You lose %d " + Icons.KEK + ".", 75, (level, bet) -> Math.round(-bet * 0.5)),
        LOSE_QUARTER("Lose 25%", "You lost %d " + Icons.KEK + ". (-25%%)", 100, (level, bet) -> Math.round(-bet * 0.25)),
        KEEP_BET("Win nothing, lose nothing", "You keep your bet this time.", 20, (level, bet) -> 0L),
        WIN_QUARTER("Win 125%", "You win %d " + Icons.KEK + ". (+25%%)", 100, (level, bet) -> Math.round(bet * 0.25)),
        WIN_HALF("Win 150%", "You win %d " + Icons.KEK + ". (+50%%)", 40, (level, bet) -> Math.round(bet * 0.5)),
        WIN_DOUBLE("Win double", "You doubled your bet! +%d " + Icons.KEK, 25, (level, bet) -> bet),
        WIN_TRIPLE("Win triple", "POGKEK. You win %d " + Icons.KEK + ". (+200%%)", 10, (level, bet) -> bet * 2),
        WIN_QUADRUPLE("Win quadruple!", Icons.OTHER_KEKOVERDRIVE + " You win four times your original bet! " + Icons.OTHER_THEFAST + " +%d " + Icons.KEK + " (+300%%).", 5, (level, bet) -> bet * 3),
        JACKPOT("JACKPOT!!!", Icons.OTHER_KEKOVERDRIVE + " POGGERS! YOU WIN THE JACKPOT! +%d " + Icons.KEK + ".", 0.1, (level, bet) ->
        {
            var maxJp = XPRewards.maxJackpot(level, bet);
            var currentJp = GlobalProperties.get(GlobalProperties.JACKPOT);
            var jp = Math.min(currentJp, maxJp);
            GlobalProperties.subtract(GlobalProperties.JACKPOT, jp - GlobalProperties.get(GlobalProperties.JACKPOT_RESET));
            GlobalProperties.set(GlobalProperties.JACKPOT_RESET, 0);
            return jp;
        });

        private final String shortName;
        private final String text;
        private final double weight;
        private double chance;
        private double threshold;
        private BiFunction<Integer, Long, Long> calc;

        GambleOutcome(String shortName, String fullName, double weight, BiFunction<Integer, Long, Long> calc)
        {
            this.shortName = shortName;
            this.text = fullName;
            this.weight = weight;
            this.calc = calc;
        }

        public BiFunction<Integer, Long, Long> getApplier()
        {
            return this.calc;
        }

        public double getChance()
        {
            return this.chance;
        }

        public String getShortName()
        {
            return this.shortName;
        }

        public String getText()
        {
            return this.text;
        }

        public double getThreshold()
        {
            return this.threshold;
        }

        public double getWeight()
        {
            return this.weight;
        }

        public void setChance(double chance)
        {
            this.chance = chance;
        }

        public void setThreshold(double threshold)
        {
            this.threshold = threshold;
        }
    }

    public static class GambleVarMap extends ConcurrentHashMap<Long, Double>
    {
        /**
         * 
         */
        private static final long serialVersionUID = 9102070166284491540L;

        public void addRescaled(long key, double value, double percentage)
        {
            this.compute(key, (mapKey, previousValue) ->
            {
                if (previousValue == null)
                {
                    return value * percentage;
                }

                return previousValue + value * percentage;
            });
        }

        public void setRescaled(long key, double value, double percentage)
        {
            this.compute(key, (mapKey, previousValue) ->
            {
                if (previousValue == null)
                {
                    return value * percentage;
                }

                return previousValue * (1 - percentage) + value * percentage;
            });
        }

        public void multiplyRescaled(long key, double value, double percentage)
        {
            this.compute(key, (mapKey, previousValue) ->
            {
                if (previousValue == null)
                {
                    return 0.0;
                }

                var interpolator = new LinearInterpolator();
                var function = interpolator.interpolate(new double[] { 0, 1 }, new double[] { 1, value });

                return previousValue * function.value(percentage);
            });
        }
    }

    static
    {
        var scale = 0.0;
        var vals = GambleOutcome.values();

        for (GambleOutcome val : vals)
        {
            scale += val.getWeight();
        }

        var rescale = 100.0 / scale;

        var threshold = 0.0;

        for (GambleOutcome val : vals)
        {
            var chance = val.getWeight() * rescale;

            val.setChance(chance);
            threshold += chance;
            val.setThreshold(threshold);
        }
    }

    private static final GambleOutcome[] outcomesSorted = { GambleOutcome.LOSE_EVERYTHING,
            GambleOutcome.LOSE_THREE_QUARTERS, GambleOutcome.LOSE_HALF, GambleOutcome.LOSE_QUARTER,
            GambleOutcome.WIN_QUARTER, GambleOutcome.WIN_HALF, GambleOutcome.WIN_DOUBLE, GambleOutcome.WIN_TRIPLE,
            GambleOutcome.WIN_QUADRUPLE, GambleOutcome.JACKPOT };

    private static final GambleVarMap generosity = new GambleVarMap();
    private static final GambleVarMap greediness = new GambleVarMap();
    private static final GambleVarMap badLuckProtection = new GambleVarMap();

    private static double genLBound(long id)
    {
        return badLuckProtection.getOrDefault(id, 0.0) / 50.0;
    }

    private static double genRBound(long id)
    {
        return 100.0 / (1 + greediness.getOrDefault(id, 0.0) / 1500.0) + generosity.getOrDefault(id, 0.0) / 200.0;
    }

    public static void printChances(CallObj co)
    {
        var id = co.caller.getIdLong();

        var lbound = genLBound(id);
        var rbound = genRBound(id);

        var eb = new EmbedBuilder();
        eb.setTitle("Your gambling chances");
        eb.setDescription(co.caller.getAsMention() + "'s odds for gambling.");
        eb.setThumbnail(co.caller.getEffectiveAvatarUrl());
        eb.setColor(0x008080);

        var total = 0.0;

        for (GambleOutcome go : outcomesSorted)
        {
            var chance = Math.max(Math.min(go.chance, go.threshold - lbound), 0);

            chance = Math.max(Math.min(chance, rbound - go.threshold), 0);

            total += chance;
        }

        var mul = 100 / total;

        for (GambleOutcome go : outcomesSorted)
        {
            var chance = Math.max(Math.min(go.chance, go.threshold - lbound), 0);

            chance = Math.max(Math.min(chance, rbound - go.threshold), 0);

            eb.addField(go.shortName, String.format("%.3f%%", chance * mul), false);
        }

        MR.send(co.textChannel, eb.build());
    }

    public static GambleOutcome roll(CallObj co, double percentage)
    {
        var result = GambleOutcome.KEEP_BET;

        var id = co.caller.getIdLong();

        var lbound = genLBound(id);
        var rbound = genRBound(id);

        var rnd = Botdiril.RDG.nextUniform(lbound, rbound);

        do
        {
            for (GambleOutcome element : outcomesSorted)
            {
                result = element;

                if (element.getThreshold() > rnd)
                {
                    break;
                }
            }
        }
        while (Curser.isCursed(co, EnumCurse.CANT_WIN_JACKPOT) && result == GambleOutcome.JACKPOT);

        switch (result)
        {
            case LOSE_EVERYTHING:
                greediness.setRescaled(id, 0.0, percentage);
                generosity.addRescaled(id, 100.0, percentage);
                badLuckProtection.addRescaled(id, 100.0, percentage);
                break;
            case LOSE_THREE_QUARTERS:
                generosity.addRescaled(id, 50.0, percentage);
                greediness.multiplyRescaled(id, 0.05, percentage);
                badLuckProtection.addRescaled(id, 50.0, percentage);
                break;
            case LOSE_HALF:
                generosity.addRescaled(id, 20.0, percentage);
                greediness.multiplyRescaled(id, 0.1, percentage);
                badLuckProtection.addRescaled(id, 20.0, percentage);
                break;
            case LOSE_QUARTER:
                generosity.addRescaled(id, 5.0, percentage);
                greediness.multiplyRescaled(id, 0.25, percentage);
                badLuckProtection.addRescaled(id, 5.0, percentage);
            case KEEP_BET:
                break;
            case WIN_QUARTER:
                generosity.multiplyRescaled(id, 0.8, percentage);
                badLuckProtection.multiplyRescaled(id, 0.5, percentage);
                greediness.addRescaled(id, 2.0, percentage);
                break;
            case WIN_HALF:
                generosity.multiplyRescaled(id, 0.75, percentage);
                badLuckProtection.multiplyRescaled(id, 0.25, percentage);
                greediness.addRescaled(id, 5.0, percentage);
                break;
            case WIN_DOUBLE:
                generosity.multiplyRescaled(id, 0.5, percentage);
                badLuckProtection.multiplyRescaled(id, 0.125, percentage);
                greediness.addRescaled(id, 50.0, percentage);
                break;
            case WIN_TRIPLE:
                generosity.multiplyRescaled(id, 0.25, percentage);
                badLuckProtection.multiplyRescaled(id, 0.0625, percentage);
                greediness.addRescaled(id, 150.0, percentage);
                break;
            case WIN_QUADRUPLE:
                generosity.multiplyRescaled(id, 0.125, percentage);
                badLuckProtection.multiplyRescaled(id, 0.03125, percentage);
                greediness.addRescaled(id, 300.0, percentage);
                break;
            case JACKPOT:
                generosity.setRescaled(id, 0.0, percentage);
                badLuckProtection.setRescaled(id, 0.0, percentage);
                greediness.addRescaled(id, 1000.0, percentage);
                break;
        }

        return result;
    }
}
