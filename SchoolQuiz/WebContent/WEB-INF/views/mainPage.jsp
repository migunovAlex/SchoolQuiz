<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 
<head>
 <link rel="stylesheet" type="text/css" href="../../css/mainMenuStyles.css" />
 <link rel="stylesheet" type="text/css" href="../../css/jquery-ui-1.9.0.custom.css" />
 <link rel="stylesheet" type="text/css" href="../../css/ui.jqgrid.css" />
 
 <script type="text/javascript" src="../../js/jquery-1.8.2.js"></script>
 <script type="text/javascript" src="../../js/jquery.cookie.js"></script>
 <script type="text/javascript" src="../../js/jquery.easing.1.3.js"></script>
 <script type="text/javascript" src="../../js/jquery-ui-1.9.0.custom.min.js"></script>
 <script type="text/javascript" src="../../js/grid.locale-ru.js"></script>
 <script type="text/javascript" src="../../js/jquery.jqGrid.min.js"></script>
  
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Main Page</title>

	<script type="text/javascript">
		$(document).ready(function(){
			$("li").mouseover(function(){
				$(this).stop().animate({height:'100px'},{queue:false, duration:300, easing:'easeOutBounce'});
			});
			$("li").mouseout(function(){
				$(this).stop().animate({height:'70px'},{queue:false, duration:300, easing:'easeOutBounce'});
			});
			createGrid();
		});
		
		function editRow(){
			var s = $("#grid").jqGrid('getGridParam','selrow');
			var dataFromTheRow = $('#grid').jqGrid ('getRowData', s);
			/*alert("1 - "+dataFromTheRow.groupDescription+"; 2 - "+dataFromTheRow.id+"; 3 - "+dataFromTheRow.groupName);*/
			if(dataFromTheRow.id==null || dataFromTheRow.groupName == null){
				alert("Пожалуйста, выберите группу для редактирования");
				return;
			}
			$("#edit_id").val(dataFromTheRow.id);
			$("#edit_name").val(dataFromTheRow.groupName);
			$("#edit_description").val(dataFromTheRow.groupDescription);
			
			$( "#dialog-formEdit" ).dialog( "open" );
			
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
		
		$(function(){
			
			var name = $("#name"),
			description= $("#description"),
			editName = $("#edit_name"),
			editDescription = $("#edit_description"),
			editId = $("#edit_id"),
			deleteId = $("#delete_id"),
			deleteName = $("#delete_name"),
			allFields = $([]).add(name).add(description).add(editName).add(editDescription).add(editId);
			
			$( "#dialog-formEdit" ).dialog({
				 autoOpen: false,
				 height: 300,
				 width: 450,
				 modal: true,
				 buttons: {
				 "Сохранить группу": function() {
				 var bValid = true;
				 allFields.removeClass( "ui-state-error" );
				 bValid = bValid && checkLength( editName, "edit_name", 3, 16 );
				// bValid = bValid && checkLength( editDescription, "edit_description", 3, 200 );
				 if ( bValid ) {
					 $( "#users tbody" ).append( "<tr>" +
					 "<td>" + editName.val() + "</td>" +
					 //"<td>" + editDescription.val() + "</td>" +
					 "</tr>" );
					 editGroup(editId.val(), editName.val(), true);
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
				 height: 300,
				 width: 350,
				 modal: true,
				 buttons: {
				 "Сохранить группу": function() {
				 var bValid = true;
				 allFields.removeClass( "ui-state-error" );
				 bValid = bValid && checkLength(name, "name", 3, 16);
				 if ( bValid ) {
					 $( "#users tbody" ).append( "<tr>" +
					 "<td>" + name.val() + "</td>" + "</tr>" );
					 alert("name.val(): " + name.val());
					 createGroup(name.val());
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
						 deleteGroup(deleteId.val());
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
		
		function editGroup(groupId, groupName, groupEnabled){
			alert("groupId: " + groupId + " " + "groupNameЖ " + groupName + " " + "groupEnabled " + groupEnabled );
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.groupId = groupId;
			dataToSend.groupName = groupName;
			dataToSend.enabled = groupEnabled;
			var jsonData = JSON.stringify(dataToSend);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"json/editGroup",
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
		
		function deleteGroup(groupId){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.groupId = groupId;
			var jsonData = JSON.stringify(dataToSend);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"json/deleteGroup",
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
		
		function createGroup(groupName){
			alert("groupName: " + groupName);
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.groupName = groupName;
			var jsonData = JSON.stringify(dataToSend);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"json/addGroup",
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
		
		function createGrid(){
			//alert("URL: " + $.cookie("SERVER_HOST")+"json/getGroupList");
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.numberFrom = "0";
			dataToSend.numberOfItems = "20";
			var jsonData = JSON.stringify(dataToSend);
			//alert("jsonData: " + jsonData);
			$("#grid").jqGrid({
				url:$.cookie("SERVER_HOST")+"json/getGroupList",
				datatype:'json',
				mtype:"POST",
				loadBeforeSend: function(xhr)
				{
				   xhr.setRequestHeader("Content-Type", "application/json");
				   return xhr;
				},       
				colNames:["ID", "Название группы", "Использовать в тесте"],
				colModel:[
				{name:'id', index:'id',width:60, editable:false, editoptions:{readonly:true, size:10},hidden:true},
				{name:'groupName', index:'groupName', width:100, editable:true, editrules:{required:true}, editoptions:{size:10}},
				{name:'enabled', index:'enabled', width:20, editable:true, edittype:"checkbox",editoptions:{value:"Yes:No"}}
				],
				postData: jsonData,
				rowNum:20,
				rowList:[20,40,60],
				height:500,
				autowidth:true,
				rownumbers: true,
				pager:'#pager',
				sortname:'id',
				viewrecords:true,
				sortorder:"asc",
				caption:"Группы вопросов",
				emptyrecords:"Пустое поле",
				loadonce:false,
				loadcomplete:function(){},
				jsonReader:{
					root:"groupList",
					page:"page",
					total:"total",
					records:"records",
					repeatitems:false,
					cell:"cell",
					id:"id"
				}
			})
			
			
			
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
				{	caption:"Посмотр вопросов в группе",
					buttonicon:"ui-icon-document",
					onClickButton: showQuestionsList,
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
			
/*			
			$("#grid").jqGrid('editGridRow','new',
				{url:$.cookie("SERVER_HOST")+"quiz/json/addGroup",
					editData:{},
					recreateForm:true,
					beforeShowForm: function(form){},
					closeAfterAdd:true,
					reloadAfterSubmit:false,
					afterSubmit: function(response, postdata){
						alert("addingResult - "+response.errorData.errorCode);
					}
				}
			);*/
		}
		
		function editRow(){
			var s = $("#grid").jqGrid('getGridParam','selrow');
			var dataFromTheRow = $('#grid').jqGrid ('getRowData', s);
			/*alert("1 - "+dataFromTheRow.groupDescription+"; 2 - "+dataFromTheRow.id+"; 3 - "+dataFromTheRow.groupName);*/
			if(dataFromTheRow.id==null || dataFromTheRow.groupName == null){
				alert("Пожалуйста, выберите группу для редактирования");
				return;
			}
			$("#edit_id").val(dataFromTheRow.id);
			$("#edit_name").val(dataFromTheRow.groupName);
			$("#edit_description").val(dataFromTheRow.groupDescription);
			
			$( "#dialog-formEdit" ).dialog( "open" );
		}
		
		function deleteRow(){
			var s = $("#grid").jqGrid('getGridParam','selrow');
			var dataFromTheRow = $('#grid').jqGrid ('getRowData', s);
			/*alert("1 - "+dataFromTheRow.groupDescription+"; 2 - "+dataFromTheRow.id+"; 3 - "+dataFromTheRow.groupName);*/
			if(dataFromTheRow.id==null || dataFromTheRow.groupName == null){
				alert("Пожалуйста, выберите группу для удаления");
				return;
			}
			$("#delete_id").val(dataFromTheRow.id);
			$("#delete_name").val(dataFromTheRow.groupName);
			$("#delete_name").attr('disabled', 'disabled');
			$( "#dialog-confirm" ).dialog( "open" );
						
		}
		
		function createQuestionInGroupsList(){
			var groupId = $("#showQuestionsForGroup_id").val();
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.groupId = groupId;
			//dataToSend.numberFrom = 0;
			//dataToSend.numberOfItems = 1000;
			var jsonData = JSON.stringify(dataToSend);
			/*alert("jsonData: " + jsonData);*/
			$("#questionsForGroupGrid").jqGrid({
				url:$.cookie("SERVER_HOST")+"json/getQuestionListForGroup",
				datatype:'json',
				mtype:"POST",
				loadBeforeSend: function(xhr)
				{
				   xhr.setRequestHeader("Content-Type", "application/json");
				   return xhr;
				},
				colNames:["ID","Текст вопроса","",""],
				colModel:[
				{name:'id', index:'id',width:20, editable:false, editoptions:{readonly:true, size:10},hidden:true},
				{name:'questionText', index:'questionText', width:650, editable:true, editrules:{required:true}, editoptions:{size:10}},
				{name:'parentId', index:'parentId', width:40, editable:false, editrules:{required:true}, editoptions:{size:10},hidden:true},
				{name:'enabled', index:'enabled', width:40, editable:false, editrules:{required:true}, editoptions:{size:10},hidden:true},
				
				],
				postData: jsonData,
				rowNum:20,
				//rowList:[20,40,60],
				height:390,
				autowidth:false,
				rownumbers:true,
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
			/*alert("url: " + $.cookie("SERVER_HOST")+"json/getQuestionListForGroup " + " xhr: ");
			alert("jsonData postData: " + jsonData);*/
		}
		
		function showQuestionsList(){
			
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
		
		function closeForm(){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			var jsonData = JSON.stringify(dataToSend);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"finishUserSession",
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
		
		$(window).bind('resize',function(){
			var width = $(window).width()-200;
			if(width==null || width <1){
				width = $(window).width() - 200;
			}
			width = width-20;
			if(width > 200){
				$("#grid").setGridWidth(width);
			}
		}
		).trigger('resize');
		
		
		function getLoginPage(){
			location=$.cookie("SERVER_HOST")+"pages/loginForm";
		}
		
		function loadAnswerList(){
			var userSession = $.cookie("ADMIN_SESSION");
			var loc = $.cookie("SERVER_HOST")+"pages/answersPage?userSession="+userSession;
			location=loc;

		}
		
		function getQuestionsInGroups(){
				var userSession = $.cookie("ADMIN_SESSION");
				var loc = $.cookie("SERVER_HOST")+"pages/questionsInGroups?userSession="+userSession;
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
			<td width="100% - 200px">
				<div id="jqgrid">
					<table id="grid"></table>
					<div id="pager"></div>
				</div>
		
			</td>
		</tr>
	</table>

<div id="dialog-form" title="Создать новую группу">
	<p class="validateTips">Необходимо ввести все поля</p>
	<form>
		<fieldset>
			<label for="name">Название группы</label>
			<input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" />
			</br>
		</fieldset>
	</form>
</div>
	
<div id="dialog-show_questions-for-group" title="Список вопросов, которые входят в группу">
	
		<div width="100%" height="420px">
			
				<table id="questionsForGroupGrid"></table>
				<div id="questionsForGroupPager"></div>
			
		</div>
</div>

<div id="dialog-formEdit" title="Изменить данные о группе">
	<p class="validateTips">Вы можете внести изменения в поля</p>
	<form>
		<fieldset>
			<label for="edit_id" style="display:none">ID</label>
			<input type="text" name="edit_id" id="edit_id" style="display: none;" class="text ui-widget-content ui-corner-all" />
			<label for="edit_name">Название группы</label>
			</br>
			<input type="text" name="edit_name" id="edit_name" class="text ui-widget-content ui-corner-all" />
			</br>
			
		</fieldset>
	</form>
</div>

<div id="dialog-confirm" title="Вы уверены что хотите удалить группу?">
	<form>
		<fieldset>
			<input type="text" name="delete_id" id="delete_id" style="display: none;" class="text ui-widget-content ui-corner-all" />
			<label for="edit_name">Название группы</label>
			<input type="text" name="delete_name" id="delete_name" class="text ui-widget-content ui-corner-all" />
		</fieldset>
	</form>
	<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Данные о группе будут удалены без возможности восстановления</p>
</div>

</body>

</html>