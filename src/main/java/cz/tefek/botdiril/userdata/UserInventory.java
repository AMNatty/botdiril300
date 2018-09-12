package cz.tefek.botdiril.userdata;

import java.math.BigInteger;
import java.sql.Statement;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.sql.SqlFoundation;
import cz.tefek.botdiril.userdata.achievement.Achievement;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.items.Item;
import cz.tefek.botdiril.userdata.items.ItemCurrency;
import cz.tefek.botdiril.userdata.timers.Timer;
import cz.tefek.botdiril.userdata.xp.XPAdder;

public class UserInventory
{
    public static final String TABLE_USER = "users";
    public static final String TABLE_INVENTORY = "inventory";
    public static final String TABLE_CARDS = "cards";
    public static final String TABLE_ACHIEVEMENTS = "achievements";
    public static final String TABLE_GIFTCODES = "giftcodes";
    public static final String TABLE_TIMERS = "timers";

    private int fkid;
    private long userid;

    static
    {
        var tabExists = SqlFoundation.checkTableExists(BotMain.sql, TABLE_USER);

        if (!tabExists)
        {
            BotMain.sql
                    .exec("CREATE TABLE " + TABLE_USER + " ( us_id INT PRIMARY KEY AUTO_INCREMENT, us_userid BIGINT NOT NULL UNIQUE, us_coins BIGINT NOT NULL DEFAULT 0, us_keks BIGINT NOT NULL DEFAULT 0, us_tokens BIGINT NOT NULL DEFAULT 0, us_keys BIGINT NOT NULL DEFAULT 0, us_mega VARCHAR(1024) NOT NULL DEFAULT '0', us_dust BIGINT NOT NULL DEFAULT 0, us_level INT NOT NULL DEFAULT '1', us_xp BIGINT NOT NULL DEFAULT 0);", stat -> {
                        return stat.execute();
                    });
        }

        var tabExistsInv = SqlFoundation.checkTableExists(BotMain.sql, TABLE_INVENTORY);

        if (!tabExistsInv)
        {
            BotMain.sql
                    .exec("CREATE TABLE " + TABLE_INVENTORY + " ( fk_us_id INT NOT NULL, fk_il_id INT NOT NULL, it_amount BIGINT NOT NULL DEFAULT 0, FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id), FOREIGN KEY (fk_il_id) REFERENCES " + ItemLookup.TABLE_ITEMLOOKUP + "(il_id) );", stat -> {
                        return stat.execute();
                    });
        }

        var tabExistsCard = SqlFoundation.checkTableExists(BotMain.sql, TABLE_CARDS);

        if (!tabExistsCard)
        {
            BotMain.sql
                    .exec("CREATE TABLE " + TABLE_CARDS + " ( fk_us_id INT NOT NULL, fk_il_id INT NOT NULL, cr_amount BIGINT NOT NULL DEFAULT 0, cr_level INT NOT NULL DEFAULT 0, cr_xp BIGINT NOT NULL DEFAULT 0, FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id), FOREIGN KEY (fk_il_id) REFERENCES " + ItemLookup.TABLE_ITEMLOOKUP + "(il_id) );", stat -> {
                        return stat.execute();
                    });
        }

        var tabExistsAchv = SqlFoundation.checkTableExists(BotMain.sql, TABLE_ACHIEVEMENTS);

        if (!tabExistsAchv)
        {
            BotMain.sql
                    .exec("CREATE TABLE " + TABLE_ACHIEVEMENTS + " ( fk_us_id INT NOT NULL, fk_il_id INT NOT NULL, FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id), FOREIGN KEY (fk_il_id) REFERENCES " + ItemLookup.TABLE_ITEMLOOKUP + "(il_id) );", stat -> {
                        return stat.execute();
                    });
        }

        var tabExistsCodes = SqlFoundation.checkTableExists(BotMain.sql, TABLE_GIFTCODES);

        if (!tabExistsCodes)
        {
            BotMain.sql
                    .exec("CREATE TABLE " + TABLE_GIFTCODES + " ( fk_us_id INT NOT NULL, cd_code VARCHAR(32) NOT NULL, FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id));", stat -> {
                        return stat.execute();
                    });
        }

        var tabExistsTime = SqlFoundation.checkTableExists(BotMain.sql, TABLE_TIMERS);

        if (!tabExistsTime)
        {
            BotMain.sql
                    .exec("CREATE TABLE " + TABLE_TIMERS + " ( fk_us_id INT NOT NULL, fk_il_id INT NOT NULL, tm_time BIGINT NOT NULL, FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id), FOREIGN KEY (fk_il_id) REFERENCES " + ItemLookup.TABLE_ITEMLOOKUP + "(il_id) );", stat -> {
                        return stat.execute();
                    });
        }
    }

    public UserInventory(long userid)
    {
        this.userid = userid;

        BotMain.sql.lock();

        Integer user = BotMain.sql.exec("SELECT us_id FROM " + TABLE_USER + " WHERE us_userid=(?)", stat -> {
            var res = stat.executeQuery();
            if (res.next())
                return res.getInt("us_id");
            else
                return null;
        }, this.userid);

        if (user == null)
        {
            this.fkid = BotMain.sql.exec(c -> {
                var stat = c
                        .prepareStatement("INSERT INTO " + TABLE_USER + "(us_userid) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                stat.setLong(1, this.userid);
                stat.executeUpdate();
                var keys = stat.getGeneratedKeys();
                keys.next();
                return keys.getInt(1);
            });
        }
        else
        {
            this.fkid = user;
        }

        BotMain.sql.unlock();
    }

    public UIObj getUserDataObj()
    {
        return BotMain.sql.exec("SELECT * FROM " + TABLE_USER + " WHERE us_id=(?)", stat -> {
            var rs = stat.executeQuery();
            rs.next();

            var cardCount = BotMain.sql
                    .exec("SELECT SUM(cr_amount) as cardcount FROM " + TABLE_CARDS + " WHERE fk_us_id=(?)", stmt -> {
                        var ress = stmt.executeQuery();
                        ress.next();
                        return ress.getLong("cardcount");
                    }, this.fkid);

            return new UIObj(rs.getInt("us_level"), rs.getLong("us_xp"), rs.getLong("us_coins"), rs
                    .getLong("us_keks"), rs.getLong("us_dust"), new BigInteger(rs.getString("us_mega"), 36), rs
                            .getLong("us_keys"), rs.getLong("us_tokens"), cardCount);
        }, this.fkid);
    }

    public long getCoins()
    {
        return BotMain.sql.exec("SELECT us_coins FROM " + TABLE_USER + " WHERE us_id=(?)", stat -> {
            var rs = stat.executeQuery();
            rs.next();
            return rs.getLong("us_coins");
        }, this.fkid);
    }

    public long getKeks()
    {
        return BotMain.sql.exec("SELECT us_keks FROM " + TABLE_USER + " WHERE us_id=(?)", stat -> {
            var rs = stat.executeQuery();
            rs.next();
            return rs.getLong("us_keks");
        }, this.fkid);
    }

    public BigInteger getMegaKeks()
    {
        return BotMain.sql.exec("SELECT us_mega FROM " + TABLE_USER + " WHERE us_id=(?)", stat -> {
            var rs = stat.executeQuery();
            rs.next();
            return new BigInteger(rs.getString("us_mega"), 36);
        }, this.fkid);
    }

    public long getKekTokens()
    {
        return BotMain.sql.exec("SELECT us_coins FROM " + TABLE_USER + " WHERE us_id=(?)", stat -> {
            var rs = stat.executeQuery();
            rs.next();
            return rs.getLong("us_coins");
        }, this.fkid);
    }

    public long getKeys()
    {
        return BotMain.sql.exec("SELECT us_coins FROM " + TABLE_USER + " WHERE us_id=(?)", stat -> {
            var rs = stat.executeQuery();
            rs.next();
            return rs.getLong("us_coins");
        }, this.fkid);
    }

    public long getDust()
    {
        return BotMain.sql.exec("SELECT us_dust FROM " + TABLE_USER + " WHERE us_userid=(?)", stat -> {
            var rs = stat.executeQuery();
            rs.next();
            return rs.getLong("us_dust");
        }, this.fkid);
    }

    public long getCards()
    {
        return BotMain.sql.exec("SELECT us_dust FROM " + TABLE_CARDS + " WHERE us_userid=(?)", stat -> {
            var rs = stat.executeQuery();
            rs.next();
            return rs.getLong("us_dust");
        }, this.fkid);
    }

    public long howManyOf(Item item)
    {
        return BotMain.sql
                .exec("SELECT it_amount FROM " + TABLE_INVENTORY + " WHERE fk_us_id=(?) AND fk_il_id=(?)", stat -> {
                    var rs = stat.executeQuery();

                    if (rs.next())
                        return rs.getLong("it_amount");
                    else
                        return 0L;
                }, this.fkid, item.getID());
    }

    public void setItem(Item item, long amount)
    {
        if (item instanceof ItemCurrency)
        {
            ItemCurrency curr = (ItemCurrency) item;

            switch (curr.getCurrency())
            {
                case COINS:
                    this.setCoins(amount);
                    return;

                case DUST:
                    this.setDust(amount);
                    return;

                case KEKS:
                    this.setKeks(amount);
                    return;

                case KEYS:
                    this.setKeys(amount);
                    return;

                case MEGAKEKS:
                    this.setMegaKeks(BigInteger.valueOf(amount));
                    return;

                case TOKENS:
                    this.setKekTokens(amount);
                    return;

                case XP:
                    this.setXP(amount);
                    return;
            }
        }

        BotMain.sql.exec("SELECT it_amount FROM " + TABLE_INVENTORY + " WHERE fk_us_id=(?) AND fk_il_id=(?)", stat -> {
            var res = stat.executeQuery();

            if (res.next())
                BotMain.sql
                        .exec("UPDATE " + TABLE_INVENTORY + " SET it_amount=(?) WHERE fk_us_id=(?) AND fk_il_id=(?)", stmt -> {
                            return stmt.executeUpdate();
                        }, amount, this.fkid, item.getID());
            else
                BotMain.sql
                        .exec("INSERT INTO " + TABLE_INVENTORY + " (fk_us_id, fk_il_id, it_amount)  VALUES (?, ?, ?)", stmt -> {
                            return stmt.executeUpdate();
                        }, this.fkid, item.getID(), amount);

            return null;
        }, this.fkid, item.getID());
    }

    public void addItem(Item item, long amount)
    {
        if (item instanceof ItemCurrency)
        {
            ItemCurrency curr = (ItemCurrency) item;

            switch (curr.getCurrency())
            {
                case COINS:
                    this.addCoins(amount);
                    return;

                case DUST:
                    this.addDust(amount);
                    return;

                case KEKS:
                    this.addKeks(amount);
                    return;

                case KEYS:
                    this.addKeys(amount);
                    return;

                case MEGAKEKS:
                    this.addMegaKeks(BigInteger.valueOf(amount));
                    return;

                case TOKENS:
                    this.addKekTokens(amount);
                    return;

                case XP:
                    this.addXP(amount);
                    return;
            }
        }

        BotMain.sql.exec("SELECT it_amount FROM " + TABLE_INVENTORY + " WHERE fk_us_id=(?) AND fk_il_id=(?)", stat -> {
            var res = stat.executeQuery();

            if (res.next())
                BotMain.sql
                        .exec("UPDATE " + TABLE_INVENTORY + " SET it_amount=it_amount+? WHERE fk_us_id=(?) AND fk_il_id=(?)", stmt -> {
                            return stmt.executeUpdate();
                        }, amount, this.fkid, item.getID());
            else
                BotMain.sql
                        .exec("INSERT INTO " + TABLE_INVENTORY + " (fk_us_id, fk_il_id, it_amount)  VALUES (?, ?, ?)", stmt -> {
                            return stmt.executeUpdate();
                        }, this.fkid, item.getID(), amount);

            return null;
        }, this.fkid, item.getID());
    }

    public void addItem(Item item)
    {
        if (item instanceof ItemCurrency)
        {
            ItemCurrency curr = (ItemCurrency) item;

            switch (curr.getCurrency())
            {
                case COINS:
                    this.addCoins(1);
                    break;

                case DUST:
                    this.addDust(1);
                    break;

                case KEKS:
                    this.addKeks(1);
                    break;

                case KEYS:
                    this.addKeys(1);
                    break;

                case MEGAKEKS:
                    this.addMegaKeks(BigInteger.ONE);
                    break;

                case TOKENS:
                    this.addKekTokens(1);
                    break;

                case XP:
                    this.addXP(1);
                    break;
            }
        }

        BotMain.sql.exec("SELECT it_amount FROM " + TABLE_INVENTORY + " WHERE fk_us_id=(?) AND fk_il_id=(?)", stat -> {
            var res = stat.executeQuery();

            if (res.next())
                BotMain.sql
                        .exec("UPDATE " + TABLE_INVENTORY + " SET it_amount=it_amount+1 WHERE fk_us_id=(?) AND fk_il_id=(?)", stmt -> {
                            return stmt.executeUpdate();
                        }, this.fkid, item.getID());
            else
                BotMain.sql
                        .exec("INSERT INTO " + TABLE_INVENTORY + " (fk_us_id, fk_il_id, it_amount)  VALUES (?, ?, 1)", stmt -> {
                            return stmt.executeUpdate();
                        }, this.fkid, item.getID());

            return null;
        }, this.fkid, item.getID());
    }

    public long howManyOf(Card card)
    {
        return BotMain.sql
                .exec("SELECT cr_amount FROM " + TABLE_CARDS + " WHERE fk_us_id=(?) AND fk_il_id=(?)", stat -> {
                    var rs = stat.executeQuery();

                    if (rs.next())
                        return rs.getLong("cr_amount");
                    else
                        return 0L;
                }, this.fkid, card.getID());
    }

    public void setCard(Card item, long amount)
    {
        BotMain.sql.exec("SELECT cr_amount FROM " + TABLE_CARDS + " WHERE fk_us_id=(?) AND fk_il_id=(?)", stat -> {
            var res = stat.executeQuery();

            if (res.next())
                BotMain.sql
                        .exec("UPDATE " + TABLE_CARDS + " SET cr_amount=(?) WHERE fk_us_id=(?) AND fk_il_id=(?)", stmt -> {
                            return stmt.executeUpdate();
                        }, amount, this.fkid, item.getID());
            else
                BotMain.sql
                        .exec("INSERT INTO " + TABLE_CARDS + " (fk_us_id, fk_il_id, cr_amount)  VALUES (?, ?, ?)", stmt -> {
                            return stmt.executeUpdate();
                        }, this.fkid, item.getID(), amount);

            return null;
        }, this.fkid, item.getID());
    }

    public void addCard(Card item, long amount)
    {
        BotMain.sql.exec("SELECT cr_amount FROM " + TABLE_CARDS + " WHERE fk_us_id=(?) AND fk_il_id=(?)", stat -> {
            var res = stat.executeQuery();

            if (res.next())
                BotMain.sql
                        .exec("UPDATE " + TABLE_CARDS + " SET cr_amount=cr_amount+? WHERE fk_us_id=(?) AND fk_il_id=(?)", stmt -> {
                            return stmt.executeUpdate();
                        }, amount, this.fkid, item.getID());
            else
                BotMain.sql
                        .exec("INSERT INTO " + TABLE_CARDS + " (fk_us_id, fk_il_id, cr_amount)  VALUES (?, ?, ?)", stmt -> {
                            return stmt.executeUpdate();
                        }, this.fkid, item.getID(), amount);

            return null;
        }, this.fkid, item.getID());
    }

    public void addCard(Card item)
    {
        this.addCard(item, 1);
    }

    public long getXP()
    {
        return BotMain.sql.exec("SELECT us_xp FROM " + TABLE_USER + " WHERE us_id=(?)", stat -> {
            var rs = stat.executeQuery();
            rs.next();
            return rs.getLong("us_xp");
        }, this.fkid);
    }

    public int getLevel()
    {
        return BotMain.sql.exec("SELECT us_level FROM " + TABLE_USER + " WHERE us_id=(?)", stat -> {
            var rs = stat.executeQuery();
            rs.next();
            return rs.getInt("us_level");
        }, this.fkid);
    }

    public long getCardXP(Card card)
    {
        return BotMain.sql.exec("SELECT cr_xp FROM " + TABLE_CARDS + " WHERE fk_us_id=(?) AND fk_il_id=(?)", stat -> {
            var rs = stat.executeQuery();

            if (rs.next())
                return rs.getLong("cr_xp");
            else
                return 0L;
        }, this.fkid, card.getID());
    }

    public int getCardLevel(Card card)
    {
        return BotMain.sql
                .exec("SELECT cr_level FROM " + TABLE_CARDS + " WHERE fk_us_id=(?) AND fk_il_id=(?)", stat -> {
                    var rs = stat.executeQuery();

                    if (rs.next())
                        return rs.getInt("cr_level");
                    else
                        return 0;
                }, this.fkid, card.getID());
    }

    public boolean hasAchievement(Achievement achievement)
    {
        return BotMain.sql
                .exec("SELECT * FROM " + TABLE_ACHIEVEMENTS + " WHERE fk_us_id=(?) AND fk_il_id=(?)", stat -> {
                    var rs = stat.executeQuery();

                    return rs.next();
                }, this.fkid, achievement.getID());
    }

    public void fireAchievement(Achievement achievement)
    {
        BotMain.sql.exec("SELECT * FROM " + TABLE_ACHIEVEMENTS + " WHERE fk_us_id=(?) AND fk_il_id=(?)", stat -> {
            var rs = stat.executeQuery();
            if (!rs.next())
            {
                BotMain.sql.exec("INSERT INTO " + TABLE_ACHIEVEMENTS + " (fk_us_id, fk_il_id) VALUES (?, ?)", stmt -> {
                    stmt.executeUpdate();
                    return null;
                }, this.fkid, achievement.getID());
            }
            return null;
        }, this.fkid, achievement.getID());
    }

    public void setCoins(long coins)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_coins=(?) WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, coins, this.fkid);
    }

    public void setKeks(long keks)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_keks=(?) WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, keks, this.fkid);
    }

    public void setMegaKeks(BigInteger megas)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_mega=(?) WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, megas.toString(36), this.fkid);
    }

    public void setKekTokens(long tokens)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_tokens=(?) WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, tokens, this.fkid);
    }

    public void setKeys(long keys)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_keys=(?) WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, keys, this.fkid);
    }

    public void setDust(long dust)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_dust=(?) WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, dust, this.fkid);
    }

    public void setXP(long xp)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_xp=(?) WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, xp, this.fkid);
    }

    public void setLevel(int level)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_level=(?) WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, level, this.fkid);
    }

    public void addCoins(long coins)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_coins=us_coins+? WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, coins, this.fkid);
    }

    public void addKeks(long keks)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_keks=us_keks+? WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, keks, this.fkid);
    }

    public void addMegaKeks(BigInteger megas)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_mega=(?) WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, getMegaKeks().add(megas).toString(36), this.fkid);
    }

    public void addKekTokens(long tokens)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_tokens=us_tokens+? WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, tokens, this.fkid);
    }

    public void addKeys(long keys)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_keys=us_keys+? WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, keys, this.fkid);
    }

    public void addDust(long dust)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_dust=us_dust+? WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, dust, this.fkid);
    }

    /**
     * <b>DO NOT USE THIS DIRECTLY!</b> Use {@link XPAdder}, which checks for level
     * advancements.
     */
    @Deprecated
    public void addXP(long xp)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_xp=us_xp+? WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, xp, this.fkid);
    }

    public void addLevel(int level)
    {
        BotMain.sql.exec("UPDATE " + TABLE_USER + " SET us_level=us_level+? WHERE us_id=(?)", stat -> {
            return stat.executeUpdate();
        }, level, this.fkid);
    }

    public long getTimer(Timer timer)
    {
        return BotMain.sql
                .exec("SELECT tm_time FROM " + TABLE_TIMERS + " WHERE fk_us_id=(?) AND fk_il_id=(?)", stat -> {
                    var res = stat.executeQuery();

                    if (res.next())
                        return res.getLong("tm_time");
                    else
                        return 0L;
                }, this.fkid, timer.getID());
    }

    public void setTimer(Timer timer, long timestamp)
    {
        BotMain.sql.exec("SELECT tm_time FROM " + TABLE_TIMERS + " WHERE fk_us_id=(?) AND fk_il_id=(?)", stat -> {
            var res = stat.executeQuery();

            if (res.next())
                BotMain.sql
                        .exec("UPDATE " + TABLE_TIMERS + " SET tm_time=(?) WHERE fk_us_id=(?) AND fk_il_id=(?)", stmt -> {
                            return stmt.executeUpdate();
                        }, timestamp, this.fkid, timer.getID());
            else
                BotMain.sql
                        .exec("INSERT INTO " + TABLE_TIMERS + " (fk_us_id, fk_il_id, tm_time)  VALUES (?, ?, ?)", stmt -> {
                            return stmt.executeUpdate();
                        }, this.fkid, timer.getID(), timestamp);

            return null;
        }, this.fkid, timer.getID());
    }

    public long useTimer(Timer timer)
    {
        var tt = getTimer(timer);
        if (System.currentTimeMillis() > tt)
        {
            setTimer(timer, System.currentTimeMillis() + timer.getTimeOffset());
            return -1;
        }
        return tt - System.currentTimeMillis();
    }

    public long checkTimer(Timer timer)
    {
        var tt = getTimer(timer);
        if (System.currentTimeMillis() > tt)
        {
            return -1;
        }
        return tt - System.currentTimeMillis();
    }

    // This differentiates in the fact that this overrides the time even when
    // waiting
    public long useTimerOverride(Timer timer)
    {
        var tt = getTimer(timer);
        if (System.currentTimeMillis() > tt)
        {
            setTimer(timer, System.currentTimeMillis() + timer.getTimeOffset());
            return -1;
        }
        setTimer(timer, System.currentTimeMillis() + timer.getTimeOffset());
        return tt - System.currentTimeMillis();
    }

    public void resetTimer(Timer timer)
    {
        setTimer(timer, 0);
    }

    public int getFID()
    {
        return this.fkid;
    }
}
