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

		$(document).ready(function(){
			$("li").mouseover(function(){
				$(this).stop().animate({height:'100px'},{queue:false, duration:300, easing:'easeOutBounce'});
			});
			$("li").mouseout(function(){
				$(this).stop().animate({height:'70px'},{queue:false, duration:300, easing:'easeOutBounce'});
			});
			
			loadQuestionGroups();
			createGrid(indexIdGroup);
		});
		
			
		
		
		
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
		
		function loadQuestionGroups(){
		    /*$('#list').combobox({  
		        url:null, 
		        mode:'local',
		        valueField:'id',  
		        textField:'text',
		        onSelect: function(param){
		    		loadGridData(param.id);
		    		indexIdGroup = param.id;
		   	}
		    
		    });*/
		    
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
		
		function loadGridData(groupId){
			
			//createGrid(groupId);
			
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
					/*getNextPage();*/
				},
				failure: function(errMsg){alert(errMsg);}
			});
		};
		
		function createGrid(groupId){
			//alert("URL: " + $.cookie("SERVER_HOST")+"json/getGroupList");
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.groupId = groupId;
			//dataToSend.numberFrom = "0";
			//dataToSend.numberOfItems = "20";
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
					{name:'enabled', index:'enabled', width:40, editable:false, editrules:{required:true}, editoptions:{size:10},hidden:true},
					],
					postData: jsonData,
					rowNum:20,
					//rowList:[20,40,60],
					height:500,
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
			alert("Add new ROW!");
			//$( "#dialog-form" ).dialog( "open" );
		}
		
		function editRow(){
			var s = $("#grid").jqGrid('getGridParam','selrow');
			var dataFromTheRow = $('#grid').jqGrid ('getRowData', s);
			/*alert("1 - "+dataFromTheRow.groupDescription+"; 2 - "+dataFromTheRow.id+"; 3 - "+dataFromTheRow.groupName);*/
			if(dataFromTheRow.id==null || dataFromTheRow.answerText == null){
				alert("Пожалуйста, выберите ответ для редактирования");
				return;
			}
			$("#edit_id").val(dataFromTheRow.id);
			$("#edit_name").val(dataFromTheRow.answerText);
			
			/*$( "#dialog-formEdit" ).dialog( "open" );*/
		}
		
		function deleteRow(){
			
		}
		
		function showQuestionsList(){
					
		}
		
		function getListGroup(){
			var e = document.getElementById("list");
    		var groupId = e.options[e.selectedIndex].value;
    		
			loadGridData(groupId);
		}
		
		function getGroupOfQuestions(groupId) {
		    $.ajax({
		        type: "POST",
		        url:$.cookie("SERVER_HOST")+"json/getGroupList",
		        data: "{'countryId':" + (countryId) + "}",
		        contentType: "application/json; charset=utf-8",
		        global: false,
		        async: false,
		        dataType: "json",
		        success: function(jsonObj) {
		            alert(jsonObj.d);
		        }
		    });
		    return false;
		}
		
		function createQuestion(questionText, responseType, questionGroup, questionParentId, enabled){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.questionText = questionText;
			dataToSend.responseType = responseType;
			dataToSend.questionGroup = questionGroup;
			
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
		};
		
	
		
</script>
</head>
<body>
	<div>
		<p>Выберите группу вопросов:</p>
		<!-- <select id="list" class="easyui-combobox" name="list" style="width:200px;" onClick="getListGroup"></select>-->
		<select id="list" onchange="getListGroup()">
	   	 <option disabled>Выберите группу вопросов</option>
	    </select>
	</div>
	</br>
	<table width="100%" id="positionTable">
		<tr>
			<td width="100% - 200px">
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
				<input type="text" name="answerText" id="answerText" width="250" class="text ui-widget-content ui-corner-all" />
				</br>
				<input type="checkbox" name="option1" value="a1" checked>enable<br>
				</br>
			</fieldset>
		</form>
	</div>
	
</body>
</html>