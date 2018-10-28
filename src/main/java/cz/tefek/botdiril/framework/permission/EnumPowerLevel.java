package cz.tefek.botdiril.framework.permission;

import java.util.EnumSet;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.serverdata.RolePreferences;

public enum EnumPowerLevel
{
    EVERYONE("Default", "Default execution level. Everyone has this power level.", null, (member, channel) -> true, false, 0),
    DJ("DJ", "Grants full control of the music bot. Assignable by a role.", EVERYONE, (member, channel) -> false, true, 5),
    ELEVATED("Elevated User", "Grants elevated permissions like using the bot in a disabled channel. Automatically granted by having a manage channel/permission role for the text channel. Assignable by a role.", DJ, (member, channel) -> member.hasPermission(channel, Permission.MANAGE_PERMISSIONS) || member.hasPermission(channel, Permission.MANAGE_CHANNEL), true, 1),
    SUPERUSER("SuperUser", "Grants access to some administrator commands. All administrators have this execution level by default. Assignable by a role.", ELEVATED, (member, channel) -> member.hasPermission(Permission.ADMINISTRATOR), true, 2),
    SUPERUSER_OVERRIDE("Executive SuperUser", "Grants (almost) full control of the bot. Executive SuperUsers can be only defined by the bot developer.", SUPERUSER, (member, channel) -> BotMain.config.getSuperuserOverrideIDs().contains(member.getUser().getIdLong()), false, 3),
    SUPERUSER_OWNER("Executive Bot Owner", "Full control of the bot.", SUPERUSER_OVERRIDE, (member, channel) -> member.getUser().getIdLong() == Botdiril.AUTHOR_ID, false, 4),
    VIP("VIP", "Grants special perks. Assignable by a role.", EVERYONE, (member, channel) -> false, true, 6);

    public static EnumPowerLevel getByID(int id)
    {
        for (var pl : values())
        {
            if (pl.id == id)
                return pl;
        }

        return null;
    }

    private EnumPowerLevel basis;
    private String formalName;
    private BiFunction<Member, TextChannel, Boolean> predicate;
    private boolean assignable;

    private int id;
    private String description;

    EnumPowerLevel(String formalName, String description, EnumPowerLevel basis, BiFunction<Member, TextChannel, Boolean> preconditions, boolean assignable, int id)
    {
        this.basis = basis;
        this.description = description;
        this.formalName = formalName;
        this.predicate = preconditions;
        this.assignable = assignable;
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public int getID()
    {
        return this.id;
    }

    public static EnumSet<EnumPowerLevel> getCumulativePowers(Member member, TextChannel tc)
    {
        final var roles = member.getRoles();

        var pl = RolePreferences.getAllPowerLevels(roles);

        for (var powerLevel : values())
        {
            if (powerLevel.predicate.apply(member, tc))
            {
                pl.add(powerLevel);
            }
        }

        var implicitlyGranted = EnumSet.noneOf(EnumPowerLevel.class);

        for (var powerLevel : pl)
        {
            while (powerLevel.basis != null)
            {
                powerLevel = powerLevel.basis;
                implicitlyGranted.add(powerLevel);
            }
        }

        pl.addAll(implicitlyGranted);

        return pl;
    }

    public static EnumSet<EnumPowerLevel> getManageablePowers(Member member, TextChannel tc)
    {
        var manageable = getCumulativePowers(member, tc);

        for (var powerLevel : values())
        {
            if (!powerLevel.satisfies(ELEVATED))
            {
                manageable.add(powerLevel);
            }
        }

        var allowed = EnumSet.copyOf(manageable.stream().map(p ->
        {
            if (p.satisfies(ELEVATED))
            {
                return p.basis;
            }
            else
            {
                return p;
            }

        }).filter(c -> c != null).filter(EnumPowerLevel::isAssignable).distinct().collect(Collectors.toSet()));

        return allowed;
    }

    public boolean check(Member member, TextChannel tc)
    {
        final var roles = member.getRoles();

        var pl = RolePreferences.getAllPowerLevels(roles);

        for (var powerLevel : values())
        {
            if (powerLevel.predicate.apply(member, tc))
            {
                pl.add(powerLevel);
            }
        }

        return pl.stream().map(this::isSatisfiedBy).filter(Boolean::valueOf).findAny().isPresent();
    }

    public boolean isAssignable()
    {
        return this.assignable;
    }

    /**
     * Reversed satisfies.
     */
    private boolean isSatisfiedBy(EnumPowerLevel permLevel)
    {
        return permLevel.satisfies(this);
    }

    public boolean satisfies(EnumPowerLevel permLevel)
    {
        if (permLevel == this)
            return true;

        if (this.basis != null)
            return this.basis.satisfies(permLevel);

        return false;
    }

    @Override
    public String toString()
    {
        return this.formalName;
    }

}
