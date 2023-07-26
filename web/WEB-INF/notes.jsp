<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My Notes</title>
        <script src="js/notes.js"></script>
    </head>
    <body>
        <h1>Notes for ${email}</h1>
        <p>
            <a href="login">Log out</a>
        </p>
        
        <h2>Your notes</h2>
        
        <ul id="notes">
            
        </ul>

        <h2 id="saveHeading">Create a New Note</h2>
        
        <form action="notes" method="post" onsubmit="notesApp.save(event, this)">
            Title: <input type="text" name="title" id="title"><br>
            Contents:<br>
            <textarea name="contents" rows="10" cols="40" id="contents"></textarea><br>
            <input type="submit" value="Save">
        </form>
        
    </body>
</html>