<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<link rel="stylesheet" type="text/css" href="../../css/jquery-ui-1.9.0.custom.css" />
 	<link rel="stylesheet" type="text/css" href="../../css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" href="../../css/mainMenuStyles.css" />

 	 <script type="text/javascript" src="../../js/jquery-1.8.2.js"></script>
	 <script type="text/javascript" src="../../js/jquery.cookie.js"></script>
	 <script type="text/javascript" src="../../js/jquery.easing.1.3.js"></script>
	 <script type="text/javascript" src="../../js/grid.locale-ru.js"></script>
	 <script type="text/javascript" src="../../js/jquery.jqGrid.min.js"></script>
	 <script type="text/javascript" src="../../js/jquery-ui.js"></script>	 
	 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Список Вопросов</title>
<script type="text/javascript">

		var indexIdGroup = 0;
		currentGroupId = 0;

		$(document).ready(function(){
			$("li").mouseover(function(){
				$(this).stop().animate({height:'100px'},{queue:false, duration:300, easing:'easeOutBounce'});
			});
			$("li").mouseout(function(){
				$(this).stop().animate({height:'70px'},{queue:false, duration:300, easing:'easeOutBounce'});
			});
			
			createGrid(indexIdGroup);
			loadQuestionGroups();
		});
		
		$(function(){
			
			var questionText = $("#questionText"),
			enabledQuestion = $("enabledQuestion"),
			deleteId = $("delete_id"),
			deleteName = $("delete_name"),
			allFields = $([]).add(questionText).add(enabledQuestion).add(delete_id).add(delete_name);
						
			$( "#dialog-form" ).dialog({
				
				 autoOpen: false,
				 height: 320,
				 width: 350,
				 modal: true,
				 buttons: {
				 "Сохранить": function() {
					var bValid = true;
					var rightFlag = false;
					allFields.removeClass( "ui-state-error" );
					bValid = bValid && checkLength(questionText, "questionText", 3, 150);
				 
					if ( bValid ) {
						 $( "#users tbody" ).append( "<tr>" +
						 "<td>" + questionText.val() + "</td>" + "</tr>" );
						 alert("Create group");
						 

						if ($('#enabledQuestion').is(':checked')) {
							rightFlag = true;
						} else {
							return;
						} 
						
						createQuestion(questionText.val(), rightFlag, currentGroupId);
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
				 height:300,
				 width:350,
				 modal: true,
				 buttons: {
					 "Удалить": function() {
						 deleteQuestion($("#delete_id").val());
						 $( this ).dialog( "close" );
					 },
					 "Отмена": function() {
						 $( this ).dialog( "close" );
					 }
				 }
			});
			
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
			
		});
		
		function createQuestion(questionText, enabledQuestion, groupId){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.questionText = questionText;
			dataToSend.responseType = 1;
			dataToSend.questionGroup = groupId;
			dataToSend.questionParentId = null;
			dataToSend.enabled = enabledQuestion;
			var jsonData = JSON.stringify(dataToSend);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"json/addQuestion",
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
							$('#grid').trigger( 'reloadGrid' );
						}
					/*getNextPage();*/
				},
				failure: function(errMsg){alert(errMsg);}
			});
		}
		
		function editQuestion(questionId, questionText, questionEnabled, groupId){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.questionId = questionText;
			dataToSend.questionText = 1;
			dataToSend.responseType = questionId;
			dataToSend.questionGroup = groupId ;
			dataToSend.questionParentId = null;;
			dataToSend.enabled = true;
			var jsonData = JSON.stringify(dataToSend);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"json/editQuestion",
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
							$('#grid').trigger( 'reloadGrid' );
						}
					/*getNextPage();*/
				},
				failure: function(errMsg){alert(errMsg);}
			});
		};
		
		function deleteQuestion(questionId){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.questionId = questionId;
			var jsonData = JSON.stringify(dataToSend);
			alert("delete question questionId: " + questionId);
			alert("delete question json data: " + jsonData);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"json/deleteQuestion",
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
							$('#grid').trigger( 'reloadGrid' );
						}
					/*getNextPage();*/
				},
				failure: function(errMsg){alert(errMsg);}
			});
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
		};
		
	function loadGridData(groupId){
			
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.groupId = groupId;
			var jsonData = JSON.stringify(dataToSend);

			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"json/getQuestionListForGroup",
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
						 	$("#grid").jqGrid('setGridParam', { postData: jsonData });
							$('#grid').trigger( 'reloadGrid' );
						}
				},
				failure: function(errMsg){alert(errMsg);}
			});
		};
		
		
		function loadQuestionGroups(){
		   	    
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			var jsonData = JSON.stringify(dataToSend);
		    
		    $.ajax({
		    	
		        type: "POST",
		        url:$.cookie("SERVER_HOST")+"json/getGroupDict",
		        data: jsonData,
		        contentType: "application/json; charset=utf-8",
		        global: false,
		        async: false,
		        dataType: "json",
		        success: function(jsonObj) {
		        	var errorCode = jsonObj.errorData.errorCode;
					if(errorCode!=200){
						alert(jsonObj.errorData.errorDescription);

						return;
					}else
					{
						var sel = $("#list");
					    sel.empty();
		            	if(jsonObj.dictItems.length>0){
		            		for (var i=0; i<jsonObj.dictItems.length; i++) {
		            		      sel.append('<option value="' + jsonObj.dictItems[i].id + '">' + jsonObj.dictItems[i].text + '</option>');
		            		    }
		            		var e = document.getElementById("list");
		            		var groupId = e.options[e.selectedIndex].value;
		            		loadGridData(groupId);
		            	}
		            	
					}
		        }
		    });
		    
		};
		
		
		
		function createGrid(groupId){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.groupId = groupId;
			var jsonData = JSON.stringify(dataToSend);
			$("#grid").jqGrid({
				url:$.cookie("SERVER_HOST")+"json/getQuestionListForGroup",
				datatype:'json',
				mtype:"POST",
				loadBeforeSend: function(xhr)
				{
				   xhr.setRequestHeader("Content-Type", "application/json");
				   return xhr;
				},       
				colNames:["ID", "Текст вопроса", "", ""],
				colModel:[
					{name:'id', index:'id',width:20, editable:false, editoptions:{readonly:true, size:10},hidden:true},
					{name:'questionText', index:'questionText', width:400, editable:true, editrules:{required:true}, editoptions:{size:10}},
					{name:'parentId', index:'parentId', width:40, editable:false, editrules:{required:true}, editoptions:{size:10},hidden:true},
					{name:'enabled', index:'enabled', width:40, editable:false, editrules:{required:true}, editoptions:{size:10},hidden:false},
					],
					postData: jsonData,
					rowNum:20,
					//rowList:[20,40,60],
					height:440,
					autowidth:true,
					rownumbers: true,
					pager:'#pager',
					sortname:'id',
					viewrecords:true,
					sortorder:"asc",
					caption:"Список вопросов",
					emptyrecords:"Пустое поле",
					loadonce:false,
					loadcomplete:function(){},
					jsonReader:{
						root:"questionGroups",
						//page:"page",
						//total:"total",
						//records:"records",
						repeatitems:false,
						//cell:"cell",
						//id:"id"
					}
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
				{	caption:"Посмотр ответов для вопроса",
					buttonicon:"ui-icon-document",
					onClickButton: showAnswerList,
					position: "last",
					title:"",
					cursor:"pointer"
					
				}
			);
			
			$("#grid").navButtonAdd('#pager',
				{	caption:"Удалить",
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
//			alert("Edit form");
			var s = $("#grid").jqGrid('getGridParam','selrow');
			var dataFromTheRow = $('#grid').jqGrid ('getRowData', s);
			if(dataFromTheRow.id==null || dataFromTheRow.questionText == null){
				alert("Пожалуйста, выберите ответ для редактирования");
				return;
			}
			
			//$("#questionText").val(dataFromTheRow.id);
			$("#questionText").val(dataFromTheRow.questionText);
			$( "#dialog-formEdit" ).dialog( "open" );
			
		}
		
		function deleteRow(){
			alert("Delete Form");
			var s = $("#grid").jqGrid('getGridParam','selrow');
			var dataFromTheRow = $('#grid').jqGrid ('getRowData', s);
			if(dataFromTheRow.id==null || dataFromTheRow.questionText == null){
				alert("Пожалуйста, выберите ответ для удаления");
				return;
			}
			
			$("#delete_id").val(dataFromTheRow.id);
			$("#delete_name").val(dataFromTheRow.questionText);
			$("#delete_name").attr('disabled', 'disabled');
			$( "#dialog-confirm" ).dialog( "open" );
			
		}
		

		
		function getListGroup(){
			var e = document.getElementById("list");
    		var groupId = e.options[e.selectedIndex].value;
    		currentGroupId = groupId;
    		
			loadGridData(groupId);
		}
		
		$(window).bind('resize',function(){
			var width = $(window).width();
			if(width==null || width <1){
				width = $(window).width();
			}
			width = width-20;
			if(width > 200){
				$("#grid").setGridWidth(width);
			}
		}
		).trigger('resize');

		function showAnswerList(){
			alert("Show question List");
			
			createQuestionInGroupsList();
			
			var s = $("#grid").jqGrid('getGridParam','selrow');
			var dataFromTheRow = $('#grid').jqGrid ('getRowData', s);
			if(dataFromTheRow.id==null || dataFromTheRow.groupName == null){
				alert("Пожалуйста, выберите группу для просмотра списка вопросов!");
				return;
			}
			
			$("#showQuestionsForGroup_id").val(dataFromTheRow.id);
			//alert("id Question Group: " + dataFromTheRow.id);
			
			var groupId = dataFromTheRow.id;
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.groupId = groupId;
			//dataToSend.numberFrom = 0;
			//dataToSend.numberOfItems = 1000;
			var jsonData = JSON.stringify(dataToSend);
			
			//alert("data: " + jsonData + " " + userSession + " " + dataToSend.questionGroup);
			
		    $("#questionsForGroupGrid").jqGrid('setGridParam', { postData: jsonData });
		    $("#questionsForGroupGrid").trigger('reloadGrid');
			$( "#dialog-show_questions-for-group" ).dialog( "open" );
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
											<p><a href="#">Группы вопросов</a></p>
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
											<p><a href="#">Просмотр результатов</a></p>
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
			<div>
				<p>Выберите группу вопросов:</p>
					<select id="list" onchange="getListGroup()">
						<option disabled>Выберите группу вопросов</option>
					</select>
		
			</div>
			<td width="100% - 200px">
				<div id="jqgrid">
					<table id="grid"></table>
					<div id="pager"></div>
				</div>
			</td>
			
		</tr>
</table>
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
				<br>
				<input type="checkbox" name="edit_right" id="edit_right" value="true" checked>Правильный ответ<br>
			</fieldset>
		</form>
	</div>
	<div id="dialog-form" title="Создать новый ответ">
		<p class="validateTips">Необходимо ввести все поля</p>
		<form>
			<fieldset>
				<label for="name">Текст ответа</label>
				<input type="text" name="questionText" id="questionText" width="250" class="text ui-widget-content ui-corner-all" />
				</br>
				<input type="checkbox" name="option1" id="enabledQuestion" value="true" checked>Участвует в опросе<br>
				</br>
			</fieldset>
		</form>
	</div>
	<div id="dialog-confirm" title="Вы уверены что хотите удалить группу?">
		<form>
			<fieldset>
				<input type="text" name="delete_id" id="delete_id" style="display: none;" class="text ui-widget-content ui-corner-all" />
				<label for="edit_name">Содержание вопроса</label>
				<input type="text" name="delete_name" id="delete_name" class="text ui-widget-content ui-corner-all" />
			</fieldset>
		</form>
		<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Вопрос будет удален без возможности восстановления</p>
	</div>
	</body>
</html>