package cz.tefek.botdiril.userdata.achievement;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.util.MR;

public class AchievementActivator
{
    public static void fire(CallObj co, Achievement achievement)
    {
        if (!co.ui.hasAchievement(achievement))
        {
            co.ui.fireAchievement(achievement);

            MR.send(co.textChannel, "You've obtained the **" + achievement.inlineDescription() + "** achievement - " + achievement.getDescription());
        }
    }
}
