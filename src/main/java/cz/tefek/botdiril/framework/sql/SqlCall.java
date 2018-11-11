package cz.tefek.botdiril.framework.sql;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlCall<R>
{
    R exec(Connection c) throws SQLException;
}
