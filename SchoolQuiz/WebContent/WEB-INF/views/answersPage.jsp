<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

 	 <link rel="stylesheet" type="text/css" href="../../css/mainMenuStyles.css" />
	 <link rel="stylesheet" type="text/css" href="../../css/jquery-ui-1.9.0.custom.css" />
 	 <link rel="stylesheet" type="text/css" href="../../css/ui.jqgrid.css" />
 	 
 	 <script type="text/javascript" src="../../js/jquery-1.8.2.js"></script>
	 <script type="text/javascript" src="../../js/jquery.cookie.js"></script>
	 <script type="text/javascript" src="../../js/jquery.easing.1.3.js"></script>
	 <script type="text/javascript" src="../../js/grid.locale-ru.js"></script>
	 <script type="text/javascript" src="../../js/jquery.jqGrid.min.js"></script>
	 <script type="text/javascript" src="../../js/jquery-ui.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Список ответов</title>
<script type="text/javascript">

		var keyWordGlobal = null;
		var dataDelete = null;

		$(document).ready(function(){
			$("li").mouseover(function(){
				$(this).stop().animate({height:'100px'},{queue:false, duration:300, easing:'easeOutBounce'});
			});
			$("li").mouseout(function(){
				$(this).stop().animate({height:'70px'},{queue:false, duration:300, easing:'easeOutBounce'});
			});
		
			createGrid();
		});
		
		
		$(function(){
				
			var name = $("#name"),
			editName = $("#edit_name"),
			editId = $("#edit_id"),
			editEnable = $("#edit_enable"),
			searchText = $("search_text"),
			deleteId = $("delete_id"),
			deleteName = $("delete_name"), 
			allFields = $([]).add(name).add(editName).add(editId).add(editEnable).add(searchText).add(deleteId).add(deleteName);
			
			$( "#dialog-formEdit" ).dialog({
				 autoOpen: false,
				 height: 300,
				 width: 650,
				 modal: true,
				 buttons: {
				 "Сохранить ответ": function() {
				 var bValid = true;
				 allFields.removeClass( "ui-state-error" );
				 bValid = bValid && checkLength( editName, "edit_name", 3, 150);
				// bValid = bValid && checkLength( editDescription, "edit_description", 3, 200 );
				 if ( bValid ) {
					 $( "#users tbody" ).append( "<tr>" +
					 "<td>" + editName.val() + "</td>" +
					 //"<td>" + editDescription.val() + "</td>" +
					 "</tr>" );
					 editAnswer(editId.val(), editName.val(), true);
					 $( this ).dialog( "close" );
				 	
				 }
				 },
				 "Отменить": function() {
				 	$( this ).dialog( "close" );
				 }
				 },
				 close: function() {
				 	allFields.val( "" ).removeClass( "ui-state-error" );
				 	$( this ).dialog( "close" );
				 }
			}
			
			);
			
			$( "#dialog-form" ).dialog({
				
				 autoOpen: false,
				 height: 500,
				 width: 350,
				 modal: true,
				 buttons: {
				 "Сохранить ответ": function() {
				 var bValid = true;
				 allFields.removeClass( "ui-state-error" );
				 bValid = bValid && checkLength(name, "name", 2, 150);
				 if ( bValid ) {
					 var rightFlag;
					 if ($('#useInQuiz').is(':checked')) {
							rightFlag = true;
						} else {rightFlag = false;
						} 
					 $( "#users tbody" ).append( "<tr>" +
					 "<td>" + name.val() + "</td>" + "</tr>" );
					 createAnswer(name.val(), rightFlag);
					 $( this ).dialog( "close" );
				 
				 }
				 },
				 "Отменить": function() {
				 	$( this ).dialog( "close" );
				 }
				 },
				 close: function() {
				 	allFields.val( "" ).removeClass( "ui-state-error" );
				 	$( this ).dialog( "close" );
				 }
			});
			
			$( "#dialog-confirm" ).dialog({
				 autoOpen:false,
				 resizable: false,
				 height:330,
				 width:350,
				 modal: true,
				 buttons: {
					 "Удалить": function() {
						 deleteAnswer(dataDelete);
						 $( this ).dialog( "close" );
					 },
					 "Отмена": function() {
						 $( this ).dialog( "close" );
					 }
				 }
			});
			
			$( "#dialog-show_questions-for-group" ).dialog({
				 autoOpen:false,
				 resizable: false,
				 height:600,
				 width:850,
				 modal: true,
				 buttons: {
					 "Закрыть": function() {
						 
						 $( this ).dialog( "close" );
					 }
				 }
			});
			
		});
		
		function editAnswer(answerId, answerText, answerEnabled){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			
			var enabledFlag = false;
			
			
			if ($('#edit_enable').is(':checked')) {
				enabledFlag = true;
				} else {
					enabledFlag = false;
				} 
			
			
			dataToSend.userSession = userSession;
			dataToSend.id = answerId;
			dataToSend.answerText = answerText;
			dataToSend.enabled = enabledFlag;
			var jsonData = JSON.stringify(dataToSend);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"json/editAnswer",
				data: jsonData,
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success: function(data){
					var errorCode = data.errorData.errorCode;
					if(errorCode!=200){
						alert(data.errorData.errorDescription);
		
						return;
					}else
						{
							alert("Вопрос изменен. Вы можете теперь найти его в поиске");
							onSearch();
						}
					/*getNextPage();*/
				},
				failure: function(errMsg){alert(errMsg);}
			});
		};
		
		function deleteAnswer(answerId){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.answerId = answerId;
			var jsonData = JSON.stringify(dataToSend);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"json/deleteAnswer",
				data: jsonData,
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success: function(data){
					var errorCode = data.errorData.errorCode;
					if(errorCode!=200){
						alert(data.errorData.errorDescription);
		
						return;
					}else
						{
							alert("Вопрос удален");
							onSearch();
						}
				},
				failure: function(errMsg){alert(errMsg);}
			});
		}
		
		function createAnswer(answerText, enabledAnswer){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.answerText = answerText;
			dataToSend.enabled = enabledAnswer;
			var jsonData = JSON.stringify(dataToSend);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"json/addAnswer",
				data: jsonData,
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success: function(data){
					var errorCode = data.errorData.errorCode;
					if(errorCode!=200){
						alert(data.errorData.errorDescription);
		
						return;
					}else
						{
							alert("Вопрос сохранен. Вы можете теперь найти его в поиске");
							onSearch();
						}
					/*getNextPage();*/
				},
				failure: function(errMsg){alert(errMsg);}
			});
		}

	function createGrid(){
			$("#grid").jqGrid({
				url:"",
				datatype:'local',
				colNames:["ID", "Список ответов", "Enabled"],
				colModel:[
				{name:'id', index:'id',width:60, editable:false, editoptions:{readonly:true, size:10},hidden:true},
				{name:'answerText', index:'answerText', width:100, editable:true, editrules:{required:true}, editoptions:{size:10}},
				{name:'enabled', index:'enabled', width:20, editable:true, edittype:"checkbox",editoptions:{value:"Yes:No"}}
				],
				height:430,
				autowidth:true,
				rownumbers: true,
				pager:'#pager',
				sortname:'id',
				viewrecords:true,
				sortorder:"asc",
				caption:"Список ответов",
				emptyrecords:"Пустое поле",
				loadonce:false,
			});
			
			$("#grid").jqGrid('navGrid','#pager',
				{edit:false, add:false, del:false, search:true},
				{},
				{},
				{},
				{
					sopt:['eq', 'ne', 'lt', 'gt','cn','bw','ew'],
					closeOnEscape:true,
					multipleSearch:true,
					closeAfterSearch:true
				}
			);
			
			$("#grid").navButtonAdd('#pager',
				{	caption:"Добавить",
					buttonicon:"ui-icon-plus",
					onClickButton: addRow,
					position: "last",
					title:"",
					cursor:"pointer"
				}
			);
			
			$("#grid").navButtonAdd('#pager',
				{	caption:"Редактировать",
					buttonicon:"ui-icon-pencil",
					onClickButton: editRow,
					position: "last",
					title:"",
					cursor:"pointer"
				}
			);
			
			$("#grid").navButtonAdd('#pager',
				{	caption:"Удалить",
					buttonicon:"ui-icon-trash",
					onClickButton: deleteRow,
					position: "last",
					title:"",
					cursor:"pointer"
				}
			);
			
			$("#btnFilter").click(function(){
				$("#grid").jqGrid('searchGrid',
					{multipleSearch: false, sopt:['eq']}		
				);
			});
		}
	
		function addRow(){
			$( "#dialog-form" ).dialog( "open" );
		}
		
		function editRow(){
			var s = $("#grid").jqGrid('getGridParam','selrow');
			var dataFromTheRow = $('#grid').jqGrid ('getRowData', s);
			if(dataFromTheRow.id==null || dataFromTheRow.answerText == null){
				alert("Пожалуйста, выберите ответ для редактирования");
				return;
			}
			$("#edit_id").val(dataFromTheRow.id);
			$("#edit_name").val(dataFromTheRow.answerText);
			$( "#dialog-formEdit" ).dialog( "open" );
			
		}
		
		function deleteRow(){
			var s = $("#grid").jqGrid('getGridParam','selrow');
			var dataFromTheRow = $('#grid').jqGrid ('getRowData', s);
			if(dataFromTheRow.id==null || dataFromTheRow.answerText == null){
				alert("Пожалуйста, выберите ответ для удаления");
				return;
			}
			$("#delete_id").val(dataFromTheRow.id);
			dataDelete = $("#delete_id").val();
			$("#delete_name").val(dataFromTheRow.answerText);
			$("#delete_name").attr('disabled', 'disabled');
			$( "#dialog-confirm" ).dialog( "open" );
		}
		
		function checkLength( o, n, min, max ) {
			 if ( o.val().length > max || o.val().length < min ) {
				 o.addClass( "ui-state-error" );
				 updateTips( "Length of " + n + " must be between " +
				 min + " and " + max + "." );
				 return false;
			 } else {
			 return true;
			 }
		}
		
		function setSearch(keyWord){
					var userSession = $.cookie("ADMIN_SESSION");
				 	var dataToSend = new Object();
					dataToSend.userSession = userSession;
					dataToSend.keyWord = keyWord;
					
					var jsonData = JSON.stringify(dataToSend);
					$.ajax({
						type:"POST",
						url:$.cookie("SERVER_HOST")+"json/getAnswerSearch",
						data: jsonData,
						contentType: "application/json; charset=utf-8",
						dataType: "json",
						success: function(data){
							var errorCode = data.errorData.errorCode;
							if(errorCode!=200){
								alert(data.errorData.errorDescription);

								return;
							}else
								{
									$('#grid').jqGrid("clearGridData");
									if(data.answers!=null){
										$("#grid").jqGrid('setGridParam',{ 
									            data:data.answers
									        }).trigger("reloadGrid");
									}
									
								}
						},
						failure: function(errMsg){alert(errMsg);}
					});
			//}
		};	
		
		function onSearch(){
			
			var dataFromSearch = $("#search_text").val();
			keyWordGlobal = dataFromSearch;
			setSearch(keyWordGlobal);
		}
		
		function closeForm(){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			var jsonData = JSON.stringify(dataToSend);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"json/finishUserSession",
				data: jsonData,
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success: function(data){
					var errorCode = data.errorData.errorCode;
					if(errorCode!=200){
						/*alert(data.errorData.errorDescription);*/
						$.cookie("ADMIN_SESSION", null);
						return;
					}
					getLoginPage();
				},
				failure: function(errMsg){alert(errMsg);}
			});
		}
		function getLoginPage(){
			location=$.cookie("SERVER_HOST")+"pages/loginForm";
		}
		
		function loadAnswerList(){
			var userSession = $.cookie("ADMIN_SESSION");
			var loc = $.cookie("SERVER_HOST")+"pages/answersPage?userSession="+userSession;
			location=loc;

		}
		
		function getResults(){
			var userSession = $.cookie("ADMIN_SESSION");
			var loc = $.cookie("SERVER_HOST")+"pages/resultsPage?userSession="+userSession;
			location=loc;
		}
		
		function getQuestionsInGroups(){
				var userSession = $.cookie("ADMIN_SESSION");
				var loc = $.cookie("SERVER_HOST")+"pages/questionsInGroups?userSession="+userSession;
				location=loc;
			}
		
		function getMain(){
			var userSession = $.cookie("ADMIN_SESSION");
			var loc = $.cookie("SERVER_HOST")+"pages/main?userSession="+userSession;
			location=loc;
		}
</script>

</head>
<body>
	<table width="100%" id="positionTable">
		<tr>
			<td width="200px">
				<div>
					<p>
						<ul>
							<table>
								<td>
									<tr>
										<li class="yellow">
											<p><a href="#" onClick="getMain()">Группы вопросов</a></p>
										</li>
									</tr>
								</td>
								<td>
									<tr>
										<li class="yellow2">
											<p><a href="#" onClick="getQuestionsInGroups()">Список вопросов</a></p>
										</li>
									</tr>
								</td>
								<td>
									<tr>
										<li class="yellow">
											<p><a href="#" onClick="getResults()">Просмотр результатов</a></p>
										</li>
									</tr>
								</td>
								<td>
									<tr>
										<li class="yellow2">
											<p><a href="#" onclick="loadAnswerList()">Список ответов</a></p>
										</li>
									</tr>
								</td>
								<td>
									<tr>
										<li class="yellow">
											<p><a href="#" onClick="closeForm()">Завершить работу</a></p>
										</li>
									</tr>
								</td>
							</table>
						</ul>
					</p>
				</div>
			</td>
			<td width="100% - 200px">
				<div>
					<p>Введите ответ, который хотите найти: </p>
					<input type="text" size="40" name="search_text" id="search_text">
					<br>
					<form>
			  			<input type="button" name="search_button" id="search_button" onClick="onSearch()" value="ПОИСК"/>
					</form>
				</div>
				<div id="jqgrid">
					<table id="grid"></table>
					<div id="pager"></div>
				</div>
		
			</td>
		</tr>
	</table>
	<div id="dialog-form" title="Создать новый ответ">
	<p class="validateTips">Необходимо ввести все поля</p>
	<form>
		<fieldset>
			<label for="name">Текст ответа</label>
			<input type="text" name="name" id="name" width="250" class="text ui-widget-content ui-corner-all" />
			</br>
			<input type="checkbox" name="useInTest" value="true" checked>Использовать в тесте<br>

		</fieldset>
	</form>
	</div>
	
	<div id="dialog-formEdit" title="Изменить данные в ответе">
	<p class="validateTips">Вы можете внести изменения в поля</p>
	<form>
		<fieldset>
			<label for="edit_id" style="display:none">ID</label>
			<input type="text" name="edit_id" id="edit_id" style="display: none;" class="text ui-widget-content ui-corner-all" />
			<label for="edit_name">Текст ответа</label>
			</br>
			<input type="text" name="edit_name" id="edit_name" class="text ui-widget-content ui-corner-all" />
			<br>
			<input type="checkbox" name="edit_enable" id="edit_enable" value="true" checked>Использовать в тесте<br>
		</fieldset>
	</form>
	</div>
	<div id="dialog-confirm" title="Вы уверены что хотите удалить выбранный ответ?">
	<form>
		<fieldset>
			<input type="text" name="delete_id" id="delete_id" style="display: none;" class="text ui-widget-content ui-corner-all" />
			<label for="edit_name">Текст ответа</label>
			<input type="text" name="delete_name" id="delete_name" class="text ui-widget-content ui-corner-all" />
		</fieldset>
	</form>
	<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Вопрос будет удален без возможности восстановления</p>
</div>
	
</body>
</html>