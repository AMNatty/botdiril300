package cz.tefek.botdiril.serverdata;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.Role;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.sql.SqlFoundation;

public class RolePreferences
{
    static final String TABLE_ROLEPOWERS = "rolepowers";

    public static final int ADDED = 1;
    public static final int ALREADY_EXISTS = 2;

    public static final int REMOVED = 1;
    public static final int NOT_PRESENT = 2;

    public static EnumSet<EnumPowerLevel> getAllPowerLevels(final List<Role> roles)
    {
        final var powerLevels = EnumSet.noneOf(EnumPowerLevel.class);

        if (roles.isEmpty())
        {
            return powerLevels;
        }

        var rolesIDsStringed = roles.stream().mapToLong(Role::getIdLong).mapToObj(l -> "rp_role=" + l).collect(Collectors.joining(" OR "));

        BotMain.sql.exec(c ->
        {
            var stat = c.prepareStatement("SELECT DISTINCT rp_power FROM " + TABLE_ROLEPOWERS + " WHERE " + rolesIDsStringed);
            var rs = stat.executeQuery();

            while (rs.next())
            {
                int id = rs.getInt("rp_power");

                try
                {
                    powerLevels.add(EnumPowerLevel.getByID(id));
                }
                catch (NullPointerException e)
                {
                    BotMain.logger.error("A power level id was mapped to null: " + id, e);
                }
            }

            rs.close();
            stat.close();

            return true;
        });

        return powerLevels;
    }

    public static void load()
    {
        var tabExists = SqlFoundation.checkTableExists(BotMain.sql, TABLE_ROLEPOWERS);

        if (!tabExists)
        {
            BotMain.sql.exec("CREATE TABLE " + TABLE_ROLEPOWERS + " ( rp_role BIGINT NOT NULL, rp_power INT NOT NULL );", stat ->
            {
                return stat.execute();
            });

            return;
        }
    }

    public static int add(Role role, EnumPowerLevel powerLevel)
    {
        final var pID = powerLevel.getID();
        final var roleID = role.getIdLong();

        return BotMain.sql.exec("SELECT * FROM " + TABLE_ROLEPOWERS + " WHERE rp_role=(?) AND rp_power=(?)", stat ->
        {
            var res = stat.executeQuery();

            if (res.next())
            {
                return ALREADY_EXISTS;
            }
            else
            {
                BotMain.sql.exec("INSERT INTO " + TABLE_ROLEPOWERS + " (rp_role, rp_power)  VALUES (?, ?)", stmt ->
                {
                    return stmt.executeUpdate();
                }, roleID, pID);

                return ADDED;
            }
        }, roleID, pID);
    }

    public static int unbind(Role role, EnumPowerLevel powerLevel)
    {
        final var pID = powerLevel.getID();
        final var roleID = role.getIdLong();

        return BotMain.sql.exec("DELETE FROM " + TABLE_ROLEPOWERS + " WHERE rp_role=? AND rp_power=?", stmt ->
        {
            return stmt.executeUpdate() > 0 ? REMOVED : NOT_PRESENT;
        }, roleID, pID);
    }
}
