/* Upon add task button is clicked, addRowDynamically() function is called */
$("#add_task_button").click(function(){ 
	addRowDynamically(); 
});

function addRowDynamically() {
	var task_value = $("#task_name").val(); // Retreive value of text box
	
	
	$.ajax({
        url: './api/add',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            text: task_value
        }),
        dataType: 'json',
		success: function(data, statusText, xhr){checkAddData(data, statusText, xhr, task_value)},
		error: function(jqXHR, textStatus, errorThrown) { showError(); }
    });
}

function checkAddData(data, statusText, xhr, task_value) {
	var status = xhr.status;                //200
  	var head = xhr.getAllResponseHeaders(); //Detail header info
  	var tasklist = $(".task_lists"); // Retreive DOM of task_list class (Used for table)
  	
  	if (status == 201) {
  		var id = data["id"]; // Set ID retreived from response
  		$(tasklist).append('<tr><td>'+task_value+'</td><td><a class="btn btn-info" href="#mod'+id+'" role="button">Modify</a></td><td><a class="btn btn-danger" href="#del'+id+'" role="button">Delete</a></td></tr>');//Append with value retrieve from text box to DOM of task list
  		$("#task_name").val('');//And after append clear the text box
  	}
  	else {
  		showError();
  	}
}


function showError(){
	var netalrt =  $(".network-alert"); // Retreive DOM of network alert
	$(netalrt).html('<div class="alert alert-danger" id="network-error-alert"> <button type="button" class="close" data-dismiss="alert">x</button> <strong>Oops! </strong>   Unable to add To-Do.</div>'); // replace the value to network alert DOM
	// Autohide Network Error Alert after 2 seconds.
	$("#network-error-alert").fadeTo(2000, 500).slideUp(500, function(){
	    $("network-error-alert").alert('close');
	});
}