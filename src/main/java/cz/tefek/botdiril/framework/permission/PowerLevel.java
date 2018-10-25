package cz.tefek.botdiril.framework.permission;

import java.util.function.BiFunction;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.Botdiril;

public enum PowerLevel
{
    EVERYONE("Everyone", null, (member, channel) -> true, false, 0),
    DJ("DJ", EVERYONE, (member, channel) -> false, true, 5),
    ELEVATED("Elevated User", DJ, (member, channel) -> member.hasPermission(channel, Permission.MANAGE_PERMISSIONS) || member.hasPermission(channel, Permission.MANAGE_CHANNEL), true, 1),
    SUPERUSER("SuperUser", ELEVATED, (member, channel) -> member.hasPermission(Permission.ADMINISTRATOR), true, 2),
    SUPERUSER_OVERRIDE("Executive SuperUser", SUPERUSER, (member, channel) -> BotMain.config.getSuperuserOverrideIDs().contains(member.getUser().getIdLong()), false, 3),
    SUPERUSER_OWNER("Executive Bot Owner", SUPERUSER_OVERRIDE, (member, channel) -> member.getUser().getIdLong() == Botdiril.AUTHOR_ID, false, 4),
    VIP("VIP", EVERYONE, (member, channel) -> false, true, 6);

    public static PowerLevel getByID(int id)
    {
        for (var pl : values())
        {
            if (pl.id == id)
                return pl;
        }

        return null;
    }

    private PowerLevel basis;
    private String formalName;
    private BiFunction<Member, TextChannel, Boolean> predicate;
    private boolean assignable;

    private int id;

    PowerLevel(String formalName, PowerLevel basis, BiFunction<Member, TextChannel, Boolean> preconditions, boolean assignable, int id)
    {
        this.basis = basis;
        this.formalName = formalName;
        this.predicate = preconditions;
        this.assignable = assignable;
        this.id = id;
    }

    public int getID()
    {
        return this.id;
    }

    public boolean check(Member member, TextChannel tc)
    {
        member.getRoles().stream().mapToLong(Role::getIdLong);

        if (this.basis != null)
            return this.predicate.apply(member, tc) || this.basis.predicate.apply(member, tc);
        else
            return this.predicate.apply(member, tc);
    }

    public boolean isAssignable()
    {
        return this.assignable;
    }

    public boolean satisfies(PowerLevel permLevel)
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
