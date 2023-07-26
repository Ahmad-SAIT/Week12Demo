var notesApp = notesApp || {}  // namespace to prevent global variables
// if no seleted note = 0
notesApp.selectedNoteId = 0;  // keeps track of which note we are editing

notesApp.getAll = async () => { 
    const response = await fetch('http://localhost:8084/notes?action=getAll')
    const notes = await response.json() // convert data to javascript objet
    console.log(notes) // until here
    
    // clear out all existing li elements
    const ul = document.querySelector("#notes")
    while (ul.firstChild)
        ul.removeChild(ul.firstChild)
    
    // loop through all notes and create list items with links and append to ul element
    for(let note of notes) {
        // create an li 
        let li = document.createElement('li');
        // data 
        li.innerHTML = note.title
        // callback
        li.addEventListener('click', () => notesApp.get(note.noteId))
        
        // append to the current HTML
        document.querySelector("#notes").appendChild(li)
    }
}

// js wait intil we finish loading the html 
// populate the ul tag when the DOM has been created from the HTML
document.addEventListener("DOMContentLoaded", notesApp.getAll)






notesApp.get = async (noteId) => {
    const response = await fetch('http://localhost:8084/notes?action=get&noteId=' + noteId)
    const note = await response.json() // get javascript
    
    console.log(note) // until here
    
    document.querySelector("#title").value = note.title
    document.querySelector("#contents").value = note.contents
    
    // keep track of the selected noted using this variable
    notesApp.selectedNoteId = note.noteId
    document.querySelector('#saveHeading').innerHTML = 'Edit Note ' + note.noteId
}






// save new note
// update new note
notesApp.save = async (e, form) => {
    e.preventDefault()  // prevent the usual form submission that the browser does
    console.log(notesApp.selectedNoteId) // 1
    let formData = new URLSearchParams()
    if (notesApp.selectedNoteId === 0) {
        formData.append('action', 'create')
    } else {
        formData.append('action', 'update')
        formData.append('noteId', notesApp.selectedNoteId)
    }
    
    formData.append('title', document.querySelector("#title").value)
    formData.append('contents', document.querySelector('#contents').value)
    
    const response = await fetch(form.action, {
        body: formData,
        method: 'post'
    })
    
    alert('Note saved.')
    
    notesApp.clearNote()
    notesApp.getAll()
}

notesApp.clearNote = () => {
    document.querySelector('#saveHeading').innerHTML = 'Create a New Note'
    document.querySelector("#title").value = ''
    document.querySelector('#contents').value = ''
    notesApp.selectedNoteId = 0 // no notes selected
}




