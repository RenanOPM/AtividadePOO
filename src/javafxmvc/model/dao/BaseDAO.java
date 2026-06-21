package javafxmvc.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;

public abstract class BaseDAO {
    protected Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    protected Date toSqlDate(LocalDate data) {
        return data == null ? null : Date.valueOf(data);
    }

    protected LocalDate toLocalDate(Date data) {
        return data == null ? null : data.toLocalDate();
    }
}
