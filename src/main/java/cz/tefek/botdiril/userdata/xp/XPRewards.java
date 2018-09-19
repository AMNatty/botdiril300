package cz.tefek.botdiril.userdata.xp;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import cz.tefek.botdiril.BotMain;

public class XPRewards
{
    private static Map<Integer, LevelData> levels = new HashMap<>();
    private static int maxLevel;

    public static void populate()
    {
        try
        {
            BotMain.logger.info("Loading level data...");

            var reader = new FileReader("assets/levels.json");
            var jtok = new JSONTokener(reader);
            var jar = new JSONArray(jtok);

            jar.forEach(obj ->
            {
                var jobj = (JSONObject) obj;

                var lvl = jobj.getInt("level");

                if (lvl > maxLevel)
                    maxLevel = lvl;

                var xp = jobj.getLong("xp");
                var cumulativeXP = jobj.getLong("cumulative_xp");
                var dailyMin = jobj.getLong("daily_xp_min");
                var dailyMax = jobj.getLong("daily_xp_max");
                var gambleFalloff = jobj.getLong("gamble_falloff");
                var drawCount = jobj.getInt("draw_potency");
                var loot = jobj.getJSONObject("loot");

                var lootStr = loot.keySet().stream().map(key -> String.valueOf(loot.getLong(key)) + "*" + key).collect(Collectors.joining(","));

                levels.put(lvl, new LevelData(xp, cumulativeXP, dailyMin, dailyMax, gambleFalloff, lootStr, drawCount));
            });

            reader.close();
        } catch (Exception e)
        {
            BotMain.logger.fatal("Level data loading failed, terminating!", e);

            System.exit(6);
        }
    }

    public static int getMaxLevel()
    {
        return maxLevel;
    }

    public static long getXPAtLevel(int level)
    {
        if (level == getMaxLevel())
            return Long.MAX_VALUE;

        return levels.get(level).getXP();
    }

    public static long getXPNeededForLvlUp(int level, long has)
    {
        return getXPAtLevel(level) - has;
    }

    public static long getCumulativeXPForLevel(int level)
    {
        return levels.get(level).getCumulativeXP();
    }

    public static String getRewardsForLvl(int lvl)
    {
        return levels.get(lvl).getLoot();
    }

    public static double getProgress(int lvl, long has)
    {
        return (double) has / (double) getXPAtLevel(lvl);
    }

    public static LevelData getLevel(int level)
    {
        return levels.get(level);
    }

    public static long getMaxCardBonusXPCoins(int level)
    {
        if (level >= 5000)
            return Long.MAX_VALUE;

        return Math.round(Math.pow(level, 1.2));
    }
}
