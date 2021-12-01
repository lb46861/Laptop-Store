/**
 * 
 */

$(document).ready(function() {
	$('.delete-laptop').on('click', function() {

		var path = 'remove';


		var id = $(this).attr('id');

		bootbox.confirm({
			message: "Are you sure to remove this laptop?",
			buttons: {
				cancel: {
					label: '<i class="fa fa-times"></i> Cancel'
				},
				confirm: {
					label: '<i class="fa fa-check"></i> Confirm'
				}
			},
			callback: function(confirmed) {
				if (confirmed) {
					$.post(path, { 'id': id }, function(res) {
						location.reload();
					});
				}
			}
		});
	});


	$('#deleteSelected').click(function() {
		var idList = $('.checkboxLaptop');
		var laptopIdList = [];
		for (var i = 0; i < idList.length; i++) {
			if (idList[i].checked == true) {
				laptopIdList.push(idList[i]['id'])
			}
		}


		var path = 'removeList';


		bootbox.confirm({
			message: "Are you sure to remove all selected laptops ?",
			buttons: {
				cancel: {
					label: '<i class="fa fa-times"></i> Cancel'
				},
				confirm: {
					label: '<i class="fa fa-check"></i> Confirm'
				}
			},
			callback: function(confirmed) {
				if (confirmed) {
					$.ajax({
						type: 'POST',
						url: path,
						data: JSON.stringify(laptopIdList),
						contentType: "application/json",
						success: function(res) {
							console.log(res);
							location.reload()
						},
						error: function(res) {
							console.log(res);
							location.reload();
						}
					});
				}
			}
		});
	});

	$("#selectAllLaptops").click(function() {
		if ($(this).prop("checked") == true) {
			$(".checkboxLaptop").prop("checked", true);
		} else if ($(this).prop("checked") == false) {
			$(".checkboxLaptop").prop("checked", false);
		}
	})

});