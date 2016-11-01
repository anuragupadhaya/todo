package co.kodr.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.log4j.Logger;

import co.kodr.objects.Note;
import co.kodr.utils.Utils;
import co.kodr.utils.db.DBUtils;

public class DBManager {
	private final static Logger logger = Logger.getLogger(DBManager.class);

	private String notes_table;

	// Templates for SQL queries

	// INSERT SQL query
	private String INSERT_NOTE = "INSERT INTO {0} VALUES (DEFAULT,?)";

	// UPDATE SQL query
	private String UPDATE_NOTE = "UPDATE {0} SET `note`=? WHERE `id`=?";

	// SELECT ALL SQL query
	private String GET_ALL_NOTES = "SELECT * FROM {0}";

	DBManager() {
		Properties config = Utils.readConfiguration();
		notes_table = config.getProperty("notes_table", "notes");
		INSERT_NOTE = MessageFormat.format(INSERT_NOTE, notes_table);
		UPDATE_NOTE = MessageFormat.format(UPDATE_NOTE, notes_table);
		GET_ALL_NOTES = MessageFormat.format(GET_ALL_NOTES, notes_table);
	}

	/**
	 * Inserts new Note to database
	 * 
	 * @param note
	 *            object with text
	 * @return note object with id
	 */
	public Note addNote(Note note) {
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Note newNote = null;
		try {
			preparedStatement = DBUtils.getConnection().prepareStatement(INSERT_NOTE);
			preparedStatement.setString(1, note.getText());
			int result = preparedStatement.executeUpdate();

			// if inserted successfully, result count will be 1
			if (result == 1) {
				newNote = new Note();
				newNote.setText(note.getText());
				statement = DBUtils.getConnection().createStatement();
				// SELECT ALL from db again
				resultSet = statement.executeQuery(GET_ALL_NOTES);
				if (resultSet != null) {
					// GOTO last row of db. That will be the latest record.
					resultSet.last();
					newNote.setId(resultSet.getInt(1));
					logger.debug("Successfully added Note:" + newNote.toString());
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

				DBUtils.close();

			} catch (SQLException e) {
				logger.error("Error in closing Database resources:", e);
			}
		}
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
		PreparedStatement preparedStatement = null;
		Statement statement = null;

		try {
			preparedStatement = DBUtils.getConnection().prepareStatement(UPDATE_NOTE);
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
				DBUtils.close();
			} catch (SQLException e) {
				logger.error("Error in closing Database resources:", e);
			}
		}
		return false;
	}

}
