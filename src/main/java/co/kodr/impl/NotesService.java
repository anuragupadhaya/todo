package co.kodr.impl;

import java.util.List;

import org.apache.log4j.Logger;

import co.kodr.dao.DBManager;
import co.kodr.interfaces.INotes;
import co.kodr.objects.Note;

/**
 * 
 * Implementation of INotes Interface
 * 
 * @author anurag
 *
 */
public class NotesService implements INotes {

	private final static Logger logger = Logger.getLogger(NotesService.class);

	private DBManager dao = new DBManager();;

	@Override
	public List<Note> getAllNotes() {
		return dao.getAllNotes();
	}

	@Override
	public Note addNote(Note note) {
		return dao.addNote(note);
	}

	@Override
	public boolean updateNote(Note note) {
		return dao.updateNote(note);
	}

}
