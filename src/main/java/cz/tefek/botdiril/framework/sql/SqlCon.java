package cz.tefek.botdiril.framework.sql;

import java.util.concurrent.locks.ReentrantLock;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.impl.NewProxyPreparedStatement;
import com.mysql.cj.jdbc.ClientPreparedStatement;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cz.tefek.botdiril.BotMain;

public class SqlCon
{
    private ReentrantLock lock;
    private ComboPooledDataSource dataSource;

    public SqlCon() throws PropertyVetoException
    {
        this.dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql:// " + BotMain.config.getSqlHost() + "/" + SqlFoundation.SCHEMA + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        dataSource.setUser(BotMain.config.getSqlKey());
        dataSource.setPassword(BotMain.config.getSqlPass());
        dataSource.setAutoCommitOnClose(true);

        lock = new ReentrantLock();
    }

    public void lock()
    {
        lock.lock();
    }

    public void unlock()
    {
        lock.unlock();
    }

    public <R> R exec(SqlCall<R> statement)
    {
        Connection c = null;

        try
        {
            c = dataSource.getConnection();
            lock.lock();
            return statement.exec(c);
        }
        catch (SQLException e)
        {
            BotMain.logger.error("An error has occured while executing an SQL statement.", e);
            return null;
        }
        finally
        {
            try
            {
                c.close();
            }
            catch (SQLException e)
            {
                BotMain.logger.error("An error has occured while closing the SQL connection.", e);
            }

            lock.unlock();
        }
    }

    public <R> R exec(String statement, SqlFunc<PreparedStatement, R> callback, Object... params)
    {
        int i = 0;

        lock.lock();

        Connection c = null;

        try
        {
            c = dataSource.getConnection();

            try (var stat = c.prepareStatement(statement))
            {
                for (; i < params.length; i++)
                {
                    var param = params[i];

                    if (param == null)
                    {
                        throw new IllegalStateException("Parameter can't be raw null!");
                    }

                    var clazz = param.getClass();

                    if (clazz == ParamNull.class)
                    {
                        stat.setNull(i + 1, ((ParamNull) param).getType());
                    }
                    else if (clazz == Integer.class)
                    {
                        stat.setInt(i + 1, (Integer) param);
                    }
                    else if (clazz == Long.class)
                    {
                        stat.setLong(i + 1, (Long) param);
                    }
                    else if (clazz == String.class)
                    {
                        stat.setString(i + 1, (String) param);
                    }
                    else
                    {
                        System.err.printf("I don't support that: %d\n", param);

                        return null;
                    }
                }

                BotMain.logger.debug("Executing SQL: " + ((ClientPreparedStatement) ((NewProxyPreparedStatement) stat).unwrap(ClientPreparedStatement.class)).asSql());

                var r = callback.exec(stat);

                stat.close();

                return r;
            }
        }
        catch (Exception e)
        {

            BotMain.logger.error("An error has occured while executing an SQL statement.", e);
            return null;
        }
        finally
        {
            try
            {
                c.close();
            }
            catch (SQLException e)
            {
                BotMain.logger.error("Error while closing the SQL connection.", e);
            }

            lock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public <R extends Object> R executeValue(String statement, String columnName, Class<R> valueType, Object... params)
    {
        int i = 0;

        lock.lock();

        Connection c = null;

        try
        {
            c = dataSource.getConnection();

            try (var stat = c.prepareStatement(statement))
            {
                for (; i < params.length; i++)
                {
                    var param = params[i];

                    if (param == null)
                    {
                        throw new IllegalStateException("Parameter can't be raw null!");
                    }

                    var clazz = param.getClass();

                    if (clazz == ParamNull.class)
                    {
                        stat.setNull(i + 1, ((ParamNull) param).getType());
                    }
                    else if (clazz == Integer.class)
                    {
                        stat.setInt(i + 1, (Integer) param);
                    }
                    else if (clazz == Long.class)
                    {
                        stat.setLong(i + 1, (Long) param);
                    }
                    else if (clazz == String.class)
                    {
                        stat.setString(i + 1, (String) param);
                    }
                    else
                    {
                        System.err.printf("I don't support that: %d\n", param);

                        return null;
                    }
                }

                try (var rs = stat.executeQuery())
                {
                    if (!rs.next())
                        return null;

                    return (R) rs.getObject(columnName);
                }
            }
        }
        catch (Exception e)
        {
            BotMain.logger.error("An error has occured while executing an SQL statement.", e);
            return null;
        }
        finally
        {
            try
            {
                c.close();
            }
            catch (SQLException e)
            {
                BotMain.logger.error("An error has occured while closing the SQL connection.", e);
            }

            lock.unlock();
        }
    }
}
