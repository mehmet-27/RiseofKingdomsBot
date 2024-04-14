package dev.mehmet27.rokbot.storage;

import com.zaxxer.hikari.HikariDataSource;
import dev.mehmet27.rokbot.Main;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBCore {

	HikariDataSource source = new HikariDataSource();

	default HikariDataSource getSource() {
		return source;
	}

	default void close() {
		try {
			if (getSource() != null && !getSource().isClosed()) {
				getSource().close();
			}
		} catch (Exception e) {
			getLogger().error("Failed to close database connection! " + e.getMessage());
		}
	}

	default ResultSet select(String query) {
		try (Connection connection = getSource().getConnection()) {
			return connection.createStatement().executeQuery(query);
		} catch (SQLException ex) {
			getLogger().error(String.format("Error executing query: %s", query));
			ex.printStackTrace();
		}
		return null;
	}

	default boolean execute(String query) {
		try (Connection connection = getSource().getConnection()) {
			connection.createStatement().execute(query);
			return true;
		} catch (SQLException ex) {
			getLogger().error(String.format("Error executing query: %s", query));
			ex.printStackTrace();
			return false;
		}
	}

	default boolean existsTable(String table) {
		try (Connection connection = getSource().getConnection()) {
			ResultSet tables = connection.getMetaData().getTables(null, null, table, null);
			return tables.next();
		} catch (SQLException ex) {
			getLogger().error(String.format("Error checking if table %s exists", table));
			ex.printStackTrace();
			return false;
		}
	}

	default boolean existsColumn(String table, String column) {
		try (Connection connection = getSource().getConnection()) {
			ResultSet col = connection.getMetaData().getColumns(null, null, table, column);
			return col.next();
		} catch (Exception ex) {
			getLogger().error(String.format("Error checking if column %s exists in table %s", column, table));
			ex.printStackTrace();
			return false;
		}
	}

	default Logger getLogger() {
		return Main.getLogger();
	}
}
