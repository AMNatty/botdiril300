package cz.tefek.botdiril.userdata.random;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import net.dv8tion.jda.core.EmbedBuilder;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;
import cz.tefek.botdiril.internal.GlobalProperties;
import cz.tefek.botdiril.userdata.items.Icons;
import cz.tefek.botdiril.userdata.xp.XPRewards;

public class GambleEngine
{
    public enum GambleOutcome
    {
        LOSE_EVERYTHING("Lose everything", "You lost everything - %d " + Icons.KEK + "... Better luck next time.", 20, (level, bet) -> -bet),
        LOSE_THREE_QUARTERS("Lose 75%", "You lost 75%% of your bet. You lost %d " + Icons.KEK + ". Pretty unlucky.", 50, (level, bet) -> Math.round(-bet * 0.75)),
        LOSE_HALF("Lose half", "You lost half of your bet. You lose %d " + Icons.KEK + ".", 120, (level, bet) -> Math.round(-bet * 0.5)),
        KEEP_BET("Win nothing, lose nothing", "You keep your bet this time.", 20, (level, bet) -> 0L),
        WIN_QUARTER("Win 125%", "You win %d " + Icons.KEK + ". (+25%%)", 100, (level, bet) -> Math.round(bet * 0.25)),
        WIN_HALF("Win 150%", "You win %d " + Icons.KEK + ". (+50%%)", 40, (level, bet) -> Math.round(bet * 0.5)),
        WIN_DOUBLE("Win double", "You doubled your bet! +%d " + Icons.KEK, 20, (level, bet) -> bet),
        WIN_TRIPLE("Win triple", "POGKEK. You win %d " + Icons.KEK + ". (+200%%)", 10, (level, bet) -> bet * 2),
        WIN_QUADRUPLE("Win quadruple!", Icons.OTHER_KEKOVERDRIVE + " You win four times your original bet! " + Icons.OTHER_THEFAST + " +%d " + Icons.KEK + " (+300%%).", 5, (level, bet) -> bet * 3),
        JACKPOT("JACKPOT!!!", Icons.OTHER_KEKOVERDRIVE + " POGGERS! YOU WIN THE JACKPOT! +%d " + Icons.KEK + ".", 0.2, (level, bet) ->
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
            GambleOutcome.LOSE_THREE_QUARTERS, GambleOutcome.LOSE_HALF, GambleOutcome.KEEP_BET,
            GambleOutcome.WIN_QUARTER, GambleOutcome.WIN_HALF, GambleOutcome.WIN_DOUBLE, GambleOutcome.WIN_TRIPLE,
            GambleOutcome.WIN_QUADRUPLE, GambleOutcome.JACKPOT };

    private static final ConcurrentHashMap<Long, Double> generosity = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Double> greediness = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Double> badLuckProtection = new ConcurrentHashMap<>();

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

    public static GambleOutcome roll(long id)
    {
        var result = GambleOutcome.KEEP_BET;

        var lbound = genLBound(id);
        var rbound = genRBound(id);

        var rnd = Botdiril.RDG.nextUniform(lbound, rbound);

        for (GambleOutcome element : outcomesSorted)
        {
            result = element;

            if (element.getThreshold() > rnd)
            {
                break;
            }
        }

        switch (result)
        {
            case JACKPOT:
                generosity.put(id, 0.0);
                badLuckProtection.put(id, 0.0);
                greediness.put(id, greediness.getOrDefault(id, 0.0) + 1000);
                break;
            case KEEP_BET:
                greediness.put(id, greediness.getOrDefault(id, 0.0) + 1);
                break;
            case LOSE_EVERYTHING:
                greediness.put(id, 0.0);
                generosity.put(id, generosity.getOrDefault(id, 0.0) + 100);
                badLuckProtection.put(id, badLuckProtection.getOrDefault(id, 0.0) + 100);
                break;
            case LOSE_HALF:
                generosity.put(id, generosity.getOrDefault(id, 0.0) + 20);
                greediness.put(id, greediness.getOrDefault(id, 0.0) / 4 * 3);
                badLuckProtection.put(id, badLuckProtection.getOrDefault(id, 0.0) + 20);
                break;
            case LOSE_THREE_QUARTERS:
                generosity.put(id, generosity.getOrDefault(id, 0.0) + 50);
                greediness.put(id, greediness.getOrDefault(id, 0.0) / 2);
                badLuckProtection.put(id, badLuckProtection.getOrDefault(id, 0.0) + 50);
                break;
            case WIN_DOUBLE:
                generosity.put(id, generosity.getOrDefault(id, 0.0) / 2);
                badLuckProtection.put(id, badLuckProtection.getOrDefault(id, 0.0) / 8);
                greediness.put(id, greediness.getOrDefault(id, 0.0) + 50);
                break;
            case WIN_TRIPLE:
                generosity.put(id, generosity.getOrDefault(id, 0.0) / 4);
                badLuckProtection.put(id, badLuckProtection.getOrDefault(id, 0.0) / 16);
                greediness.put(id, greediness.getOrDefault(id, 0.0) + 150);
                break;
            case WIN_QUADRUPLE:
                generosity.put(id, generosity.getOrDefault(id, 0.0) / 8);
                badLuckProtection.put(id, badLuckProtection.getOrDefault(id, 0.0) / 32);
                greediness.put(id, greediness.getOrDefault(id, 0.0) + 300);
                break;
            case WIN_HALF:
                generosity.put(id, generosity.getOrDefault(id, 0.0) / 4 * 3);
                badLuckProtection.put(id, badLuckProtection.getOrDefault(id, 0.0) / 4);
                greediness.put(id, greediness.getOrDefault(id, 0.0) + 5);
                break;
            case WIN_QUARTER:
                generosity.put(id, generosity.getOrDefault(id, 0.0) / 5 * 4);
                badLuckProtection.put(id, badLuckProtection.getOrDefault(id, 0.0) / 2);
                greediness.put(id, greediness.getOrDefault(id, 0.0) + 2);
                break;
        }

        return result;
    }
}
