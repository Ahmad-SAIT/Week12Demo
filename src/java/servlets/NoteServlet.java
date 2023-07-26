package servlets;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Note;
import services.NoteService;

public class NoteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // if there is no AJAX action, just display the page
        String action = request.getParameter("action");
        if (action == null) {
            getServletContext().getRequestDispatcher("/WEB-INF/notes.jsp").forward(request, response);
            return;
        }

        // all AJAXy stuff here
        NoteService ns = new NoteService();
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        try {
            switch (action) {
                case "getAll": // get all notes
                    List<Note> notes = ns.getAll(email);
                    String notesJson = getNotesJson(notes);
                    response.getWriter().write(notesJson);
                    break;

                case "get": // get one note based on the noteId
                    int id = Integer.parseInt(request.getParameter("noteId"));
                    Note note = ns.get(id); // getting data from DB
                    String noteJson = getNoteJson(note);
                    response.getWriter().write(noteJson);
                    break;
            }
        } catch (Exception e) {
            Logger.getLogger(NoteServlet.class.getName()).log(Level.SEVERE, null, e);
            response.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // only AJAXy stuff now!
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        NoteService ns = new NoteService();

        // action must be one of: create, update, delete
        String action = request.getParameter("action");
        String noteId = request.getParameter("noteId");
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");

        try {
            switch (action) {
                case "create":
                    ns.insert(title, contents, email);
                    break;
                case "update":
                    ns.update(Integer.parseInt(noteId), title, contents, email);
                    break;
                case "delete":
                    ns.delete(Integer.parseInt(noteId));
            }
            // status
            response.setStatus(200);
            response.getWriter().write("");
        } catch (Exception ex) {
            Logger.getLogger(NoteServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(500);
        }
    }

    // add two methods
    private String getNotesJson(List<Note> notes) {
        String json = "[ ";
        for (int i = 0; i < notes.size(); i++) {
            if (i > 0) {
                json += ", \n";
            }
            json += getNoteJson(notes.get(i));
        }

        json += " ]";
        return json;
    }

    private String getNoteJson(Note note) {
        String json = "{\n";
        json += "\"noteId\" : " + note.getNoteId() + ", \n";
        json += "\"title\" : \"" + note.getTitle() + "\", \n";
        json += "\"contents\" : \"" + note.getContents() + "\"\n";
        json += "}";
        return json;
    }
}
