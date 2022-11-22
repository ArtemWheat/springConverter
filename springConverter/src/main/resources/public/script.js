


var arr = [['Заголовок 1', 'Заголовок 2', 'Заголовок 3'], [1, 2, 3], [4, 5, 6], [7, 8, 9]];


function employeeSelect() {

	//get the selected id
	var employeeId = $('#employeeIdSelect').val(); // Получаю значение дива, но не использую

	//get the url for the ajax call
	var url = "/source";

	//do the ajax call
	$.get(url, populateEmployeeInfo);
}


function populateEmployeeInfo(data) {
	var status = data.status;

	//check the response to make sure it's ok
	//if (status == "Ok") {
		var response = data[0][0];


		var table = document.querySelector('#tbody');

        fillTable(table, data);
        addOnClick();

		//set the input field values
		$('#employeeIdSelect').html(response);
	//}
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

// Код на заполнение таблицы
// arr - переменная, которую получаем с сервера



/* var table = document.querySelector('#tbody');

fillTable(table, arr);

*/

function fillTable(table, arr) {

	var tr = document.createElement('tr')

	for (var j = 0; j < arr[0].length; j++) {
		var th = document.createElement('th');
		var span = document.createElement('span');
		span.id = j + 1;
		span.setAttribute('name', 'title')
		span.innerHTML = arr[0][j];

		th.appendChild(span)
		tr.appendChild(th);
	}

	table.appendChild(tr);

	for (var i = 1; i < arr.length; i++) {
		var tr = document.createElement('tr');

		for (var j = 0; j < arr[i].length; j++) {
			var td = document.createElement('td');
			td.innerHTML = arr[i][j];
			tr.appendChild(td);
		}

		table.appendChild(tr);
	}
}


// // // //



function addOnClick() {
    var elements = document.querySelectorAll('[name="title"]');

    for (var i = 0; i < elements.length; i++) {
	    elements[i].addEventListener("click", function (e) {
		    console.log(e.target.id);
	    }, false);
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////