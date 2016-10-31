package co.kodr.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.log4j.Logger;

import co.kodr.objects.Note;

public class DBManager {
	private final static Logger logger = Logger.getLogger(DBManager.class);
	private String driver;
	private String url;
	private String database;
	private String user;
	private String password;
	private String note_table;
	private final String DB_PROPERTIES = "db.properties";

	// Templates for SQL queries

	// INSERT SQL query
	private String INSERT_NOTE = "INSERT INTO {0} VALUES (DEFAULT,?)";

	// UPDATE SQL query
	private String UPDATE_NOTE = "UPDATE {0} SET `note`=? WHERE `id`=?";

	// SELECT ALL SQL query
	private String GET_ALL_NOTES = "SELECT * FROM {0}";

	/**
	 * One-Stop shop to get DB connection
	 * 
	 * @return DB Connection
	 * 
	 */
	public Connection getDBConnection() {
		initializeDBProperties();
		Connection connection = null;
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url + database, user, password);

		} catch (SQLException e) {
			logger.error("SQL exception in connecting to DB:", e);
		} catch (ClassNotFoundException e) {
			logger.error("Exception in initializing DB driver class", e);
		} finally {

		}
		if (connection != null) {
			logger.debug("Successfully connected to Database!");
			return connection;
		}
		return null;
	}

	/**
	 * Initialize DB properties from properties file
	 */
	private void initializeDBProperties() {
		Properties config = new Properties();
		try {
			config.load(getClass().getClassLoader().getResourceAsStream(DB_PROPERTIES));
			driver = config.getProperty("driver");
			url = config.getProperty("url");
			database = config.getProperty("database");
			user = config.getProperty("user");
			password = config.getProperty("password");
			note_table = config.getProperty("note_table");

			INSERT_NOTE = MessageFormat.format(INSERT_NOTE, note_table);
			UPDATE_NOTE = MessageFormat.format(UPDATE_NOTE, note_table);
			GET_ALL_NOTES = MessageFormat.format(GET_ALL_NOTES, note_table);
		} catch (Exception e) {
			logger.error("Error in initializing DB properties:", e);
		} finally {

		}

	}

	/**
	 * Inserts new Note to database
	 * 
	 * @param note
	 *            object with text
	 * @return note object with id
	 */
	public Note addNote(Note note) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Note newNote = null;
		try {
			connection = getDBConnection();
			preparedStatement = connection.prepareStatement(INSERT_NOTE);
			preparedStatement.setString(1, note.getText());
			int result = preparedStatement.executeUpdate();

			// if inserted successfully, result count will be 1
			if (result == 1) {
				newNote = new Note();
				newNote.setText(note.getText());
				statement = connection.createStatement();
				// SELECT ALL from db again
				resultSet = statement.executeQuery(GET_ALL_NOTES);
				if (resultSet != null) {
					// GOTO last row of db. That will be the latest record.
					resultSet.last();
					newNote.setId(resultSet.getInt(1));
				}
			} else {
				throw new Exception("More than 1 row updated while inserting new note!");
			}

		} catch (Exception e) {
			logger.error("SQL error in adding Note:" + note.toString(), e);
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("Error in closing connection:", e);
			}
		}
		logger.debug("Successfully added Note:" + newNote.toString());
		return newNote;
	}

	/**
	 * Update text of existing Note in database
	 * 
	 * @param note
	 * @param text
	 *            to be updated
	 * @return True, if update successful
	 */
	public boolean updateNote(Note note, String text) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;

		try {
			connection = getDBConnection();
			preparedStatement = connection.prepareStatement(UPDATE_NOTE);
			preparedStatement.setString(1, text);
			preparedStatement.setInt(2, note.getId());
			int result = preparedStatement.executeUpdate();
			if (result == 1) {
				logger.debug("Successfully updated Note:" + note.toString() + " with text:" + text);
				return true;
			} else {
				throw new Exception("More than 1 row updated while updating the note!");
			}
		} catch (Exception e) {
			logger.error("SQL error in updating Note:" + note.toString(), e);
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("Error in closing connection:", e);
			}
		}
		return false;
	}

}
