
var crutchID;

function employeeSelect2(id) {
    crutchID = id;
	//get the selected id
	//var employeeId = $('#employeeIdSelect').val(); // Получаю значение дива, но не использую

  /*  id_form = id.slice(-1);
    var form = document.getElementById('form' + id_form);
    form.classList.add("dnone");

    id_input = id.slice(-1);
    const selectedFile = document.getElementById('input' + id_input).files[0];
    var formNm = $('#form1')[0];
    var formData = new FormData();
    formData.append('file', selectedFile);
*/
    var outdata  = new FormData(jQuery('form')[0]);

    	$.ajax({
        			type: "POST",
        			url: '/upload_source',
        			cache: false,
        			contentType: false,
        			processData: false,
        			data: outdata,
        			dataType : 'json',
        			success: function(msg){
        				console.log('Fffff')
        			}
        		});


	//get the url for the ajax call
	var url = "/source";

	//do the ajax call
	$.get(url, populateEmployeeInfo2);
}


function populateEmployeeInfo2(data) {
	var status = data.status;

	//check the response to make sure it's ok
	//if (status == "Ok") {
		var response = data[0][0];


    id = crutchID;

    var bth = document.getElementById(String(id))
    bth.disabled = true

    id = id.slice(-1);
    arr_table_add.push(id);

    var value = '#tbody' + String(id)
    var table = document.querySelector(value);
    fillTable(table, data, id);
    //Сомнительная конструкция))
    if (arr_table_add[arr_table_add.length - 1] == 1) addOnClick('title1');
    if (arr_table_add[arr_table_add.length - 1] == 2) addOnClick('title2');

    if (arr_table_add.length == 2) disabled_main_bth(false);


    helper.innerHTML = 'Выберите действие, которое хотите произвести';

    //set the input field values
    $('#employeeIdSelect').html(response);
	//}
	crutchID = null;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

// Код на заполнение таблицы
// arr - переменная, которую получаем с сервера



//var arr = [['Заголовок 1', 'Заголовок 2', 'Заголовок 3', 'Заголовок 4', 'Заголовок 5', 'Заголовок 6', 'Заголовок 7', 'Заголовок 8', 'Заголовок 9'],
//[1, 2, 3, 4, 5, 6, 7, 8, 9], [1, 2, 3, 4, 5, 6, 7, 8, 9], [1, 2, 3, 4, 5, 6, 7, 8, 9]];

var helper = document.getElementById('helper'); //
var arr_join = [];
var arr_selected = [];
var output;
var action_selected = false;
var action_eq = false;
var arr_table_add = [];
var select_char = false;
var arr_select_char = [];
var char;
var input_bool = false;
var arr_actions = [];

function bthMethod(id) {

	var bth = document.getElementById(String(id))
	bth.disabled = true

	id = id.slice(-1);
	arr_table_add.push(id);

	var value = '#tbody' + String(id)
	var table = document.querySelector(value);
	fillTable(table, arr, id);
	//Сомнительная конструкция))
	if (arr_table_add[arr_table_add.length - 1] == 1) addOnClick('title1');
	if (arr_table_add[arr_table_add.length - 1] == 2) addOnClick('title2');

	if (arr_table_add.length == 2) disabled_main_bth(false);


	helper.innerHTML = 'Выберите действие, которое хотите произвести';
}


function main_bth(id) {

	action_selected = true;
	output = String(id);

	disabled_main_bth(true);

	var this_bth = document.getElementById(id);

	this_bth.classList.add('width_meta');
	parent = this_bth.parentElement;

	back_bth = creat_back_bth(id);
	parent.prepend(back_bth);

	parent = parent.parentElement;

	var container = document.createElement('div');
	container.classList.add('flex');
	container.id = 'cont_for_bth'

	arr_bth = create_bth(2, ['_', ','], true);

	parent.appendChild(container);
	for (var i = 0; i < arr_bth.length; i++) {
		container.appendChild(arr_bth[i]);
	}


	var inp = createInput();
	container.appendChild(inp);


	compile_bth = createCompile_bth();
	parent.appendChild(compile_bth);

	helperT(id);
}

function createInput() {
	var inp = document.createElement('input');

	inp.classList.add('main_menu-bth'); // возможно сделать через массив
	inp.classList.add('bth-reset');
	inp.classList.add('addit_bth')
	inp.classList.add('cust_input');
	inp.disabled = true;
	inp.placeholder = 'Ввод';

	inp.onclick = function () { // Один из двух почти одинаковых методов, хз, стоит ли сливать в один
		if (!select_char) {
			char = this.value;
			document.getElementById('compile').disabled = false;

			this.classList.add('outline');
			arr_select_char.push(this);
			select_char = true;
			input_bool = true;
		}

		else {
			arrr = document.querySelectorAll('.addit_bth');
			for (var i = 0; i < arrr.length; i++) {
				arrr[i].classList.remove('outline');
			}
			char = this.value;
			this.classList.add('outline');
			input_bool = true;
		}

	}
	return inp;
}

function creator_bth(inner, value) {
	var new_bth = document.createElement('button');

	new_bth.innerHTML = inner;

	new_bth.classList.add('main_menu-bth'); // возможно сделать через массив
	new_bth.classList.add('bth-reset');
	new_bth.classList.add('addit_bth')

	new_bth.onclick = function () { // Один из двух почти одинаковых методов, хз, стоит ли сливать в один
		if (!select_char) {
			char = this.innerHTML;
			document.getElementById('compile').disabled = false;

			this.classList.add('outline');
			arr_select_char.push(this);
			select_char = true;
			input_bool = false;
		}

		else {
			arrr = document.querySelectorAll('.addit_bth');
			for (var i = 0; i < arrr.length; i++) {
				arrr[i].classList.remove('outline');
			}
			char = this.innerHTML;
			this.classList.add('outline');
			input_bool = false;
		}

	}

	new_bth.disabled = value;

	return new_bth;
}

function createCompile_bth() {
	compile_bth = create_bth(1, ['Выполнить'], true);
	compile_bth.classList.remove('addit_bth');
	compile_bth.id = 'compile';
	compile_bth.onclick = function () {

		for (var i = 0; i < arr_selected.length; i++) {
			output += ' ' + arr_selected[i];
		};

		if (input_bool) {
			char = document.querySelector('.cust_input').value;
		}

		output += ' ' + '"' + char + '"';

		console.log(output);




		document.getElementById('cont_for_bth').remove();
		document.querySelector('.width_meta').classList.remove('width_meta');
		document.getElementById('back_bth').remove();
		document.getElementById('compile').remove();

		helperT('back');

		arr_selected = [];
		action_selected = false;

		var elements = document.querySelectorAll('.select_column');

		for (var i = 0; i < elements.length; i++) {
			elements[i].classList.remove('select_column');
		}


		disabled_main_bth(false);
		select_char = false;
		input_bool = false;

		//Нужно оправить output на сервер
		var url = "/cmd?cmd=" + output;

		crutchID = 'add_table_1'

        //do the ajax call
        /*
        $.post(url, {cmd: output}, function(data){
            alert(data);
        });
        */

        $.ajax({
            type: "POST",
            url: "/cmd",
            method: 'POST',
            dataType: 'text',
            data: {cmd: output},
            success: function(data){
        	    alert(data); // прописать метод перезаполнения таблиц

        	    document.getElementById("tbody1").replaceChildren();

        	    employeeSelect2('add_table_1');
            }
        });

        arr_actions.push(output);

        output = '';



        var cancel_bth = document.getElementById('cancel'); //активируем кнопку отмены
        cancel_bth.disabled = false;
	}

	return compile_bth;
}

function helperT(value) {
	if (value == 'combine') {
		helper.innerHTML = 'Выберите не менее двух столбцов исходной таблицы';
	}
	if (value == 'divide') {
		helper.innerHTML = 'Выберите один столбец исходной таблицы';
	}
	if (value == 'back') {
		helper.innerHTML = 'Выберите действие, которое хотите произвести';
	}
}

function disabled_main_bth(value) {
	var arr_main_bth = document.querySelectorAll('[name="main-bth-action"]');
	for (var i = 0; i < arr_main_bth.length; i++) {
		arr_main_bth[i].disabled = value;
	}

}

function create_bth(count, arr_values, value) {

	if (count == 1) {
		return creator_bth(arr_values[0], value);
	}

	else {
		arr_bth = [];
		for (var i = 0; i < count; i++) {
			new_bth = creator_bth(arr_values[i], value);
			arr_bth.push(new_bth);
		}


	}

	return arr_bth;
}

function creat_back_bth(id) {
	var back_bth = create_bth(1, ['Назад'], false);

	back_bth.id = 'back_bth';

	back_bth.onclick = function () {
		cont = parent.parentElement;
		document.getElementById('cont_for_bth').remove();
		document.getElementById(id).classList.remove('width_meta');
		document.getElementById('back_bth').remove();
		document.getElementById('compile').remove();

		helperT('back');

		arr_selected = [];
		action_selected = false;
		output = '';
		var elements = document.querySelectorAll('.select_column');

		for (var i = 0; i < elements.length; i++) {
			elements[i].classList.remove('select_column');
		}


		disabled_main_bth(false);
	};

	return back_bth;
}

// Код на заполнение таблицы
// arr - переменная, которую получаем с сервера

function fillTable(table, arr, id) {

	var tr = document.createElement('tr')

	for (var j = 0; j < arr[0].length; j++) {
		var th = document.createElement('th');
		var span = document.createElement('span');
		span.id = j + 1;
		value = 'title' + String(id)
		span.setAttribute('name', value)
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

	div_input = document.querySelectorAll('.main_field-input')
	for (var i = 0; i < div_input.length; i++) {
		div_input[i].classList.add('no_padding')
	}
}

// // // //



function addOnClick(name) {
	var value = '[name="' + name + '"]'
	var elements = document.querySelectorAll(value);


	for (var i = 0; i < elements.length; i++) {

		elements[i].addEventListener("click", function (e) {
			console.log(e.target.id);
			if(name == 'title1') action_bool = action_selected;
			if(name == 'title2') action_bool = action_eq;


			if (action_bool) {

				if (!arr_selected.includes(e.target.id)) {
					e.target.classList.add('select_column');
					arr_selected.push(e.target.id);
					regulator(output);
				}

				else {
					e.target.classList.remove('select_column');
					var index = arr_selected.indexOf(e.target.id);
					if (index > -1) { // only splice array when item is found
						arr_selected.splice(index, 1); // 2nd parameter means remove one item only
					}

					regulator(output);
				}
			}

			else if  ((arr_selected.includes(e.target.id))) {
                            e.target.classList.remove('select_column');
                            var index = arr_selected.indexOf(e.target.id);
                            if (index > -1) { // only splice array when item is found
                                arr_selected.splice(index, 1); // 2nd parameter means remove one item only
                            }

                       regulator(output);

            			}
		}, false);
	}
}

function regulator(action) {
	if (action == 'combine') {
		if (arr_selected.length >= 2) {

			helper.innerHTML = 'Выберите символ, по которому произойдёт объединение';

			var addit_bth = document.querySelectorAll('.addit_bth');
			for (var i = 0; i < addit_bth.length; i++) {
				addit_bth[i].disabled = false;
			}
		}

		else {
			helper.innerHTML = 'Выберите не менее двух столбцов исходной таблицы';

			var addit_bth = document.querySelectorAll('.addit_bth');
			for (var i = 0; i < addit_bth.length; i++) {
				addit_bth[i].disabled = true;
			}
			document.getElementById('back_bth').disabled = false;
		}
	}

	if (action == 'divide') {
		if (arr_selected.length == 1) {

			helper.innerHTML = 'Выберите символ, по которому произойдёт разделение';

			var addit_bth = document.querySelectorAll('.addit_bth');
			for (var i = 0; i < addit_bth.length; i++) {
				addit_bth[i].disabled = false;
			}

			action_selected = false;
		}

		else if (arr_selected.length < 1) {

             			helper.innerHTML = 'Выберите один столбец исходной таблицы';

             			var addit_bth = document.querySelectorAll('.addit_bth');
             			for (var i = 0; i < addit_bth.length; i++) {
             				addit_bth[i].disabled = true;
             			}

             			action_selected = true;
             		}

	}
	if (action == 'delete') {


		helper.innerHTML = 'Пока не работает';
	}
}


function cancelOnClick() {
    if (arr_actions.length > 0) {

        $.ajax({
                    type: "POST",
                    url: "/cmd",
                    method: 'POST',
                    dataType: 'text',
                    data: {cmd: 'cancel'},
                    success: function(data){
                	    alert(data); // прописать метод перезаполнения таблиц

                	    document.getElementById("tbody1").replaceChildren();

                	    employeeSelect2('add_table_1');
                    }
                });

        arr_actions.pop();


    }

    if (arr_actions.length == 0) {
         var cancel_bth = document.getElementById('cancel'); //диактивируем кнопку отмены
        cancel_bth.disabled = true;
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////