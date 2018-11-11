package cz.tefek.botdiril.framework.sql;

import java.sql.SQLException;

@FunctionalInterface
public interface SqlFunc<T, R>
{
    R exec(T c) throws SQLException;
}
