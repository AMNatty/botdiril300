package cz.tefek.botdiril.serverdata;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.entities.Role;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.permission.PowerLevel;
import cz.tefek.botdiril.framework.sql.SqlFoundation;

public class RolePreferences
{
    static final String TABLE_ROLEPOWERS = "rolepowers";

    public static EnumSet<PowerLevel> getAllPowerLevels(final List<Role> roles)
    {
        var rolesIDsStringed = roles.stream().mapToLong(Role::getIdLong).mapToObj(l -> "rp_role=" + l).collect(Collectors.joining(" OR "));

        final var powerLevels = EnumSet.noneOf(PowerLevel.class);

        BotMain.sql.exec(c ->
        {
            var stat = c.prepareStatement("SELECT DISTINCT rp_power FROM " + TABLE_ROLEPOWERS + " WHERE " + rolesIDsStringed);
            var rs = stat.executeQuery();

            while (rs.next())
            {
                int id = rs.getInt("rp_power");

                try
                {
                    powerLevels.add(PowerLevel.getByID(id));
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

}
