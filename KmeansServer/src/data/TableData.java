package data;

import java.sql.*;
import java.util.*;

public class TableData {

	DbAccess db;

	public TableData(DbAccess db) {
		this.db=db;
	}

	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException {
		List<Example> distinctTransazioni = new ArrayList<>();
		try (Connection con = db.getConnection(); Statement cmd = con.createStatement();
			 ResultSet rs = cmd.executeQuery("SELECT DISTINCT * FROM " + table)) {
			if (rs == null) throw new EmptySetException();
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				Example ex = new Example();
				for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
					if (Objects.equals(rsmd.getColumnClassName(i), "java.lang.String")) {
						ex.add(rs.getString(i));
					} else if (Objects.equals(rsmd.getColumnClassName(i), "java.lang.Float")) {
						ex.add((double)rs.getFloat(i));
					}
				}
				distinctTransazioni.add(ex);
			}
		} catch (SQLException | EmptySetException ex) {
			ex.printStackTrace();
		}
		return distinctTransazioni;
	}


	public Set<Object> getDistinctColumnValues(String table, TableSchema.Column column) throws SQLException {
		Set<Object> distinctColumnValues = new TreeSet<>();
		try (Connection con = db.getConnection(); Statement cmd = con.createStatement();
			 ResultSet rs = cmd.executeQuery("SELECT DISTINCT * FROM " + table)) {
			while (rs.next()) {
				distinctColumnValues.add(rs.getObject(column.getColumnName()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return distinctColumnValues;
	}

	public Object getAggregateColumnValue(String table, TableSchema.Column column,
										  QUERY_TYPE aggregate) throws
			SQLException, NoValueException {
		Object o = null;
		try (Connection con = db.getConnection(); Statement cmd = con.createStatement();
			 ResultSet rs = cmd.executeQuery("SELECT " + aggregate + "("
					 + column.getColumnName() + ") FROM " + table)) {
			if (rs == null) throw new NoValueException();
			rs.next();
			o = rs.getObject(1);
			rs.close();
			if (o == null) throw new NoValueException();
		} catch (SQLException | NoValueException e) {
			e.printStackTrace();
		}
		return o;
	}

	

	

}
