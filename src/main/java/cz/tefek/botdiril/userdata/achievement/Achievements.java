package cz.tefek.botdiril.userdata.achievement;

import cz.tefek.botdiril.userdata.items.Icons;

public class Achievements
{
    public static Achievement beginner = new Achievement("beginner", "Beginner", "Use the bot and get level 10.", Icons.ACHIEVEMENT_BOT);
    public static Achievement experienced = new Achievement("experienced", "Experience", "Get level 40.", Icons.ACHIEVEMENT_BOT);
    public static Achievement master = new Achievement("master", "Master", "Get level 150.", Icons.ACHIEVEMENT_BOT);
    public static Achievement ascended = new Achievement("ascended", "Ascended", "Get level 500.", Icons.ACHIEVEMENT_BOT);
    public static Achievement god = new Achievement("god", "Botdiril God", "Get level 2000.", Icons.ACHIEVEMENT_BOT);

    public static Achievement beta = new Achievement("betatester", "Botdiril Beta Tester", "Be a part of the original beta test.", Icons.ACHIEVEMENT_BETA);
}
