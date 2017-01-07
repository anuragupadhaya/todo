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
  		$(tasklist).append('<tr id="row_'+task_value+'"><td id="data_value_'+task_value+'">'+task_value+'</td><td><a class="btn btn-info todo-data-modify" id="'+id+'" href="#mod'+id+'" role="button">Modify</a></td><td><a class="btn btn-danger" id="todo_data_delete" href="#" value="'+id+'" role="button">Delete</a></td></tr>');//Append with value retrieve from text box to DOM of task list
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


// Fetch data on initial loading of page
$( document ).ready(fetchData());
function fetchData(){
	var netalrt =  $(".network-alert"); // Retreive DOM of network alert
	
	$(netalrt).html('<div class="alert alert-info" id="network-loading-alert"> <button type="button" class="close" data-dismiss="alert">x</button> <strong>Welcome! </strong>   Please wait while To-Do list is getting loaded.</div>'); // replace the value to network alert DOM
	// Get data
	setTimeout(function(){
		$.get('./api/all', function(data){
			
			for (var i = 0; i < data.length; i++){
				renderTable(data[i]);
			}
			
			$("#network-loading-alert").fadeTo(2000, 500).slideUp(500, function(){
			    $("network-error-alert").alert('close');
			});
		}) .fail(function(){
			$(netalrt).html('<div class="alert alert-danger" id="network-loading-alert">  <strong>Oops! </strong>   There was an error while retreiving To-Do list. Please refresh the page again</div>'); // replace the value to network alert DOM
		});

	},2000); 
	
	
	
}

function renderTable(rowData){
	var tasklist = $(".task_lists"); // Retreive DOM of task_list class
	var task_value = rowData.text;
	var id = rowData.id;
	$(tasklist).append('<tr id="row_'+id+'"><td id="data_value_'+id+'">'+task_value+'</td><td><a class="btn btn-info todo-data-modify" id="'+id+'" href="#mod'+id+'" role="button">Modify</a></td><td><a class="btn btn-danger" id="todo_data_delete" href="#" value="'+id+'" role="button">Delete</a></td></tr>');//Append with value retrieve from get method
}

// Delete Function, to be strange, $("#todo_data_delete").click(function(){}); was not working hence this method is used to call. Most probably due to it fact that these rows are ajax genereted.
$(document).on("click", "#todo_data_delete", function() {
	var value = $(this).attr("value");
	var netalrt =  $(".network-alert");
	
	$.ajax({
        url: './api/remove/'+value,
        type: 'DELETE',
		success: function(){
			//Upon success in deleting, notify user
			$(netalrt).html('<div class="alert alert-info" id="network-delete-alert"> <button type="button" class="close" data-dismiss="alert">x</button> <strong>Deleted! </strong> To-Do is deleted.</div>'); // replace the value to network alert DOM
			$("#network-delete-alert").fadeTo(2000, 500).slideUp(500, function(){
			    $("network-error-alert").alert('close');
			});
			// And then delete the table which contain data.
			$("#row_" + value ).remove(); 
		},
		error: function() { 

			$(netalrt).html('<div class="alert alert-danger" id="network-delete-fail-alert"> <button type="button" class="close" data-dismiss="alert">x</button> <strong>Oops! </strong> Unable to delete the To-Do. Please retry.</div>'); // replace the value to network alert DOM
			$("#network-delete-fail-alert").fadeTo(2000, 500).slideUp(500, function(){
			    $("network-error-alert").alert('close');
			});
		
		}
    });
});


// Update Function
$(document).on("click", ".todo-data-modify", function() {
	var value = $(this).attr("id");
	modifyModal(value); 
	
});

// This function creates modal for modifying value
function modifyModal(id){
	
	var netalrt =  $(".network-alert");
	$(netalrt).html('<div class="modal fade update_modal" tabindex="-1" role="dialog"> <div class="modal-dialog" role="document"> <div class="modal-content">  <div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> <h4 class="modal-title">Edit To-Do List</h4></div> <div class="modal-body"> <form id="update-text-form" action="'+id+'"> <div class="form-group"> <input class="update-text form-control" id="update-text" placeholder="Enter New To-Do"></div><br><div class="updateError"></div> </div> <div class="modal-footer"> <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>  <button type="button" class="btn btn-primary save-update-form">Save changes</button></div> </div><!-- /.modal-content --> </div><!-- /.modal-dialog --></div><!-- /.modal --></form> ');
	$('.update_modal').modal('show');
}

//Upon clicking save changes, do the needful
$(document).on("click", ".save-update-form", function() { 
	var id = $("#update-text-form").attr("action");
	var data = $("#update-text").val();
	$.ajax({ 
		url: './api/update',
		type: 'PUT',
		contentType: 'application/json',
        data: JSON.stringify({
        	"id": id,
        	"text": data
        }),
        success: function(){// On success, hide modal and change value on the to do list
        	$('.update_modal').modal('hide');
        	$('#data_value_'+id).html(data);
        },
        error:function(){ // Upon error, show error message for 2 seconds.
        	$(".updateError").html('<div class="alert alert-danger" id="update-error">  <strong>Oops! </strong>   There was an error while updating To-Do list</div>');
        	$(".updateError").fadeTo(2000, 500).slideUp(500, function(){
			    $("#update-alert").alert('close');
			});
        }
		
	});
});
