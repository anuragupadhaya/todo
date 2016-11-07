package co.kodr.interfaces;

import java.util.List;

import co.kodr.objects.Note;

public interface INotes {

	/**
	 * 
	 * Returns list of all Notes in the DB
	 * 
	 * @Param
	 * 
	 */
	List<Note> getAllNotes();

	/**
	 * Adds a new Note to the database
	 * 
	 * @param note
	 * @return new Note() object with id from DB
	 */
	Note addNote(Note note);

	/**
	 * 
	 * Updates Note() object in the DB with new text
	 * 
	 * @param note
	 * @param text
	 * @return Note() object with updated text
	 */
	boolean updateNote(Note note);
}
