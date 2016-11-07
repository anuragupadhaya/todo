package co.kodr.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import co.kodr.exception.NoteException;
import co.kodr.impl.NotesService;
import co.kodr.interfaces.INotes;
import co.kodr.objects.Note;

/**
 * 
 * REST API wrapper for NotesService
 * 
 * @author anurag
 *
 */
@Path("/")
public class NotesAPI {
	private final static Logger logger = Logger.getLogger(NotesAPI.class);

	private INotes service = new NotesService();
	private Response response;

	/**
	 * 
	 * Returns all the notes
	 *
	 */
	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllNotes() {
		response = null;
		List<Note> allNotes;

		try {
			allNotes = service.getAllNotes();

			if (allNotes == null || allNotes.isEmpty()) {
				throw new NoteException("No notes available at this time!");
			} else {
				response = Response.status(Response.Status.OK).entity(allNotes).build();
			}

		} catch (NoteException e) {
			response = Response.status(Response.Status.NOT_FOUND).build();
			logger.error(e);
		} catch (Exception e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			logger.error("Error connecting to the Database!", e);
		}

		return response;

	}

	/**
	 * 
	 * Adds a new note
	 * 
	 * @param note
	 * 
	 */
	@POST
	@Path("add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNote(Note note) {
		response = null;
		Note newNote;
		try {
			newNote = service.addNote(note);

			if (newNote == null) {
				throw new NoteException("Note could not be added", note);
			} else {
				response = Response.status(Response.Status.CREATED).entity(newNote).build();
			}
		} catch (NoteException e) {
			response = Response.status(Response.Status.BAD_REQUEST).build();
			logger.error(e);
		} catch (Exception e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			logger.error(e);
		}

		return response;

	}

	/**
	 * 
	 * Updates a note
	 * 
	 * @param note
	 * @param text
	 * 
	 */
	@PUT
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNote(Note note) {
		response = null;
		boolean updated = false;
		try {

			updated = service.updateNote(note);

			if (updated == false) {
				throw new NoteException("Note could not be updated", note);
			} else {
				response = Response.status(Response.Status.NO_CONTENT).build();
			}

		} catch (NoteException e) {
			response = Response.status(Response.Status.BAD_REQUEST).build();
			logger.error(e);
		} catch (Exception e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			logger.error(e);
		}
		return response;
	}
}
