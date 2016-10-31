/* Upon add task button is clicked, addRowDynamically() function is called */
$("#add_task_button").click(function(){ 
	addRowDynamically(); 
});

function addRowDynamically() {
	var task_value = $("#task_name").val(); // Retreive value of text box
	var tasklist = $(".task_lists"); // Retreive DOM of task_list class (Used for table)
	$(tasklist).append('<tr><td>'+task_value+'</td><td><a class="btn btn-info" href="#" role="button">Modify</a></td><td><a class="btn btn-danger" href="#" role="button">Delete</a></td></tr>');//Append with value retrieve from text box to DOM of task list
}