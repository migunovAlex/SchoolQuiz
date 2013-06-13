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
			
			createGrid(indexIdGroup);
			createLinkedAnswersGrid();
			createAnswersGrid();
			
			validatePopupWindows();
			
			loadQuestionGroups();
			
		});
		
			
		function validatePopupWindows(){
			
			var answerText = $("#answerText"),
			allFields = $([]).add(answerText);
			
			$( "#dialog-form" ).dialog({
				
				 autoOpen: false,
				 height: 320,
				 width: 500,
				 modal: true,
				 buttons: {
				 "Сохранить": function() {
					var bValid = true;
					var rightFlag = false;
					allFields.removeClass( "ui-state-error" );
					bValid = bValid && checkLength(answerText, "answerText", 3, 150);
				 
					if ( bValid ) {
						 $( "#users tbody" ).append( "<tr>" +
						 "<td>" + answerText.val() + "</td>" + "</tr>" );
						 

						if ($('#useInQuiz').is(':checked')) {
							rightFlag = true;
						} else {rightFlag = false;
						} 
						var e = document.getElementById("list");
	            		var currentGroupId = e.options[e.selectedIndex].value;
	            		var questionId = $("#question_id");
	            		//alert(questionId.val().length==0);
	            		if(questionId.val().length==0){
							createQuestion(answerText.val(), rightFlag, currentGroupId);
	            		}else
	            			{
	            				editQuestion(questionId.val(), answerText.val(), rightFlag, currentGroupId);
	            			}
						 $( this ).dialog( "close" );
				 
				 }
				 },
				 "Отменить": function() {
				 	$( this ).dialog( "close" );
				 }
				 },
				 close: function() {
				 	allFields.val( "" ).removeClass( "ui-state-error" );
				 	$("#question_id").val("");
				 	$( this ).dialog( "close" );
				 }
			});
			
			
			$( "#dialog-delete-form" ).dialog({
				 autoOpen:false,
				 resizable: false,
				 height:300,
				 width:500,
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
			
			
			$( "#linkedAnswers-form" ).dialog({
				 autoOpen:false,
				 resizable: false,
				 height:500,
				 width:720,
				 modal: true,
				 buttons: {
					 "Закрыть": function() {
						 $( this ).dialog( "close" );
					 }
				 }
			});
			
			$( "#answerForQuestionEditForm" ).dialog({
				 autoOpen:false,
				 resizable: false,
				 height:350,
				 width:650,
				 modal: true,
				 buttons: {
					 "Сохранить": function() {
							
						 var af_questionId = $("#af_question_id").val();
						 var af_answerId = $("#af_answer_id").val();
						 var rightAnswer = false;
						 if ($('#af_rightInQuestion').is(':checked')) {
							 rightAnswer = true;
						 }
						 
						 var userSession = $.cookie("ADMIN_SESSION");
							var dataToSend = new Object();
							dataToSend.userSession = userSession;
							dataToSend.questionId = af_questionId;
							dataToSend.answerId = af_answerId;
							dataToSend.rightAnswer = rightAnswer;
							
							var jsonDataToSend = JSON.stringify(dataToSend);
							$.ajax({
								type:"POST",
								url:$.cookie("SERVER_HOST")+"json/editAnswersFromQuestion",
								data: jsonDataToSend,
								contentType: "application/json; charset=utf-8",
								dataType: "json",
								success: function(data){
									var errorCode = data.errorData.errorCode;
									if(errorCode!=200){
										alert(data.errorData.errorDescription);

										return;
									}else
										{
										
											if(data.operationResult==true){
												loadLinkedAnswers(af_questionId, false);
												$( "#answerForQuestionEditForm" ).dialog( "close" );
											}
											
										}
									
								},
								failure: function(errMsg){alert(errMsg);}
							});
						 
						 clearEditAnswersForm();
						 $( this ).dialog( "close" );
					 },
					 "Закрыть": function() {
						 clearEditAnswersForm();
						 $( this ).dialog( "close" );
					 }
				 }
			});
			
			
			$( "#answers-form" ).dialog({
				 autoOpen:false,
				 resizable: false,
				 height:520,
				 width:720,
				 modal: true,
				 buttons: {
					 "Добавить": function() {
						 onAddAnswers();
						 $('#answGrid').jqGrid("clearGridData");
						 $( this ).dialog( "close" );
					 },
					 "Закрыть": function() {
						 $( this ).dialog( "close" );
					 }
				 }
			});
			
		}
		
		function clearEditAnswersForm(){
			$("#af_answerText").val();
			$("#af_question_id").val();
			$("#af_answer_id").val();
			$("#af_questionText").val();
			$('#af_rightInQuestion').prop('checked', false);
			
		}
		
		function onAddAnswers(){
			s = $("#answGrid").jqGrid('getGridParam','selarrrow');
			if(s.length==0){
				alert("Выберите ответы которые необходимо добавить ");
				return;
			}
			 var arrayData = new Array();
			 var rightAnswers = new Array();
			 for(var i=0;i<s.length; i++){
				 var myRow = $("#answGrid").jqGrid( 'getRowData', s[i] );
				 arrayData[i] = myRow.id;
				 rightAnswers[i] = false;
			 }
			 
			 var s = $("#grid").jqGrid('getGridParam','selrow');
				var dataFromTheRow = $('#grid').jqGrid ('getRowData', s);
				var questionId = dataFromTheRow.id;
			 
			 var userSession = $.cookie("ADMIN_SESSION");
				var dataToSend = new Object();
				dataToSend.userSession = userSession;
				dataToSend.questionId = questionId;
				dataToSend.answerIds = arrayData;
				dataToSend.rightAnswers = rightAnswers;
				
				var jsonDataToSend = JSON.stringify(dataToSend);
				$.ajax({
					type:"POST",
					url:$.cookie("SERVER_HOST")+"json/addAnswersToQuestion",
					data: jsonDataToSend,
					contentType: "application/json; charset=utf-8",
					dataType: "json",
					success: function(data){
						var errorCode = data.errorData.errorCode;
						if(errorCode!=200){
							alert(data.errorData.errorDescription);

							return;
						}else
							{
							
								if(data.operationResult==true){
									loadLinkedAnswers(questionId, true);
									$( "#answers-form" ).dialog( "close" );
								}
								
							}
						
					},
					failure: function(errMsg){alert(errMsg);}
				});
		}
		
		function onSearch(){
			
			var dataFromSearch = $("#search_text").val();
			keyWordGlobal = dataFromSearch;
			setSearch(keyWordGlobal);
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
							$('#answGrid').jqGrid("clearGridData");
							if(data.answers!=null){
								$("#answGrid").jqGrid('setGridParam',{ 
							            data:data.answers
							        }).trigger("reloadGrid");
							}
						}
					/*getNextPage();*/
				},
				failure: function(errMsg){alert(errMsg);}
			});
		}
		
		function editQuestion(id, questionText, rightFlag, groupId){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.questionId = id;
			dataToSend.questionText = questionText;
			dataToSend.responseType = 1;
			dataToSend.questionGroup = groupId;
			dataToSend.questionParentId = null;
			dataToSend.enabled = rightFlag;
			
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
							loadGridData(groupId);
						}
					/*getNextPage();*/
				},
				failure: function(errMsg){alert(errMsg);}
			});
		}
		
		function deleteQuestion(questionId){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.questionId = questionId;
			var jsonData = JSON.stringify(dataToSend);
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
							var e = document.getElementById("list");
		            		var groupId = e.options[e.selectedIndex].value;
		            		loadGridData(groupId);
						}
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
							$('#grid').jqGrid("clearGridData");
							if(data.questionGroups!=null){
								$("#grid").jqGrid('setGridParam',{ 
							            data:data.questionGroups
							        }).trigger("reloadGrid");
							}
						}
					/*getNextPage();*/
				},
				failure: function(errMsg){alert(errMsg);}
			});
		};
		
		function createAnswersGrid(){
			$("#answGrid").jqGrid({
				url:"",
				datatype:'local',
				colNames:["ID", "Список ответов", "Enabled"],
				colModel:[
				{name:'id', index:'id',width:60, editable:false, editoptions:{readonly:true, size:10},hidden:true},
				{name:'answerText', index:'answerText', width:100, editable:true, editrules:{required:true}, editoptions:{size:10}},
				{name:'enabled', index:'enabled', width:20, editable:true, edittype:"checkbox",editoptions:{value:"Yes:No"}}
				],

					height:210,
					width:680,
					rownumbers: true,
					sortname:'id',
					viewrecords:true,
					pgtext: null,  
					sortorder:"asc",
					multiselect:true,
					caption:"Список ответов",
					emptyrecords:"Пустое поле",
					loadonce:false
			});
		}
		
		function createLinkedAnswersGrid(){
			$("#answersGrid").jqGrid({
				url:"",
				datatype:'local',
				colNames:["ID", "Текст ответа", "Использовать в тесте", "Правильный ответ"],
				colModel:[
					{name:'id', index:'id',width:20, editable:false, editoptions:{readonly:true, size:10},hidden:true},
					{name:'answerText', index:'answerText', width:300, editable:true, editrules:{required:true}, editoptions:{size:10}},
					{name:'enabled', index:'enabled', width:100, editable:false, editrules:{required:true}, editoptions:{size:10}},
					{name:'right', index:'right', width:100, editable:false, editrules:{required:true}, editoptions:{size:10}},
					],
					rowNum:20,
					height:275,
					width:680,
					rownumbers: true,
					pgtext: null,
					pager:'#answersPager',
					sortname:'id',
					viewrecords:true,
					sortorder:"asc",
					multiselect:true,
					caption:"Список ответов для вопроса",
					emptyrecords:"Пустое поле",
					loadonce:false
			});
			
			$("#answersGrid").jqGrid('navGrid','#answersPager',
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
				
				$("#answersGrid").navButtonAdd('#answersPager',
					{	caption:"Добавить",
						buttonicon:"ui-icon-plus",
						onClickButton: addAnswersToQueston,
						position: "last",
						title:"",
						cursor:"pointer"
						
					}
				);
				
				$("#answersGrid").navButtonAdd('#answersPager',
						{	caption:"Удалить",
							buttonicon:"ui-icon-trash",
							onClickButton: answersRemoveFromQueston,
							position: "last",
							title:"",
							cursor:"pointer"
							
						}
					);
				$("#answersGrid").navButtonAdd('#answersPager',
						{	caption:"Редактировать",
							buttonicon:"ui-icon-pen",
							onClickButton: editAnswerInQueston,
							position: "last",
							title:"",
							cursor:"pointer"
							
						}
					);
				
		}
		
		function editAnswerInQueston(){
			s = $("#answersGrid").jqGrid('getGridParam','selarrrow');
			if(s.length==0){
				alert("Выберите ответ который необходимо редактировать");
				return;
			}
			if(s.length==0){
				alert("Выберите ответ для редактирования");
				return;
			}
			if(s.length>1){
				alert("Выберите только один ответ для редактирования");
				return;
			}
			
			var answerRow = $("#answersGrid").jqGrid('getGridParam','selrow');
			var answerData = $('#answersGrid').jqGrid ('getRowData', answerRow);
						 
			 var s1 = $("#grid").jqGrid('getGridParam','selrow');
				var dataFromTheRow = $('#grid').jqGrid ('getRowData', s1);
				if(dataFromTheRow.id==null || dataFromTheRow.questionText == null){
					alert("Пожалуйста, выберите ответ для редактирования");
					return;
				}
			var questionId = dataFromTheRow.id;
			var questionText = dataFromTheRow.questionText;
			var answerId = answerData.id;
			var answerText = answerData.answerText;
			var answerRight = answerData.right;
			
			$("#af_question_id").val(questionId);
			$("#af_questionText").val(questionText);
			$("#af_answer_id").val(answerId);
			$("#af_answerText").val(answerText);
				if(answerRight=="true"){
					$('#af_rightInQuestion').prop('checked', true);
				}else{
					$('#af_rightInQuestion').prop('checked', false);
				}
			
			$("#af_questionText").attr('disabled', 'disabled');
			$("#af_answerText").attr('disabled', 'disabled');
			
			$( "#answerForQuestionEditForm" ).dialog( "open" );
		}
		
		
		function addAnswersToQueston(questionId){
			
			$( "#answers-form" ).dialog( "open" );
		}
		
		function answersRemoveFromQueston(){
			s = $("#answersGrid").jqGrid('getGridParam','selarrrow');
			if(s.length==0){
				alert("Выберите ответы которые необходимо удалить из вопроса ");
				return;
			}
			 var arrayData = new Array();
			 for(var i=0;i<s.length; i++){
				 var myRow = $("#answersGrid").jqGrid( 'getRowData', s[i] );
				 arrayData[i] = myRow.id;
			 }
			 
			 var s = $("#grid").jqGrid('getGridParam','selrow');
				var dataFromTheRow = $('#grid').jqGrid ('getRowData', s);
				if(dataFromTheRow.id==null || dataFromTheRow.questionText == null){
					alert("Пожалуйста, выберите ответ для редактирования");
					return;
				}
				var questionId = dataFromTheRow.id;
			 
			 var userSession = $.cookie("ADMIN_SESSION");
				var dataToSend = new Object();
				dataToSend.userSession = userSession;
				dataToSend.questionId = questionId;
				dataToSend.answerIds = arrayData;
				
				var jsonDataToSend = JSON.stringify(dataToSend);
				$.ajax({
					type:"POST",
					url:$.cookie("SERVER_HOST")+"json/removeAnswersFromQuestion",
					data: jsonDataToSend,
					contentType: "application/json; charset=utf-8",
					dataType: "json",
					success: function(data){
						var errorCode = data.errorData.errorCode;
						if(errorCode!=200){
							alert(data.errorData.errorDescription);

							return;
						}else
							{
								if(data.operationResult==true){
									loadLinkedAnswers(questionId, true);
								}
								
							}
						
					},
					failure: function(errMsg){alert(errMsg);}
				});
			 
		}
		
		function createGrid(groupId){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.groupId = groupId;
			var jsonData = JSON.stringify(dataToSend);
			$("#grid").jqGrid({
				url:"",
				datatype:'local',
				loadBeforeSend: function(xhr)
				{
				   xhr.setRequestHeader("Content-Type", "application/json");
				   return xhr;
				},       
				colNames:["ID", "Текст вопроса", "Использовать в тесте"],
				colModel:[
					{name:'id', index:'id',width:20, editable:false, editoptions:{readonly:true, size:10},hidden:true},
					{name:'questionText', index:'questionText', width:300, editable:true, editrules:{required:true}, editoptions:{size:10}},
					{name:'enabled', index:'enabled', width:100, editable:false, editrules:{required:true}, editoptions:{size:10}},
					],
					postData: jsonData,
					rowNum:20,
					height:450,
					autowidth:true,
					rownumbers: true,
					pgtext: null,
					pager:'#pager',
					sortname:'id',
					viewrecords:true,
					sortorder:"asc",
					caption:"Список вопросов",
					emptyrecords:"Пустое поле",
					loadonce:false
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
				{	caption:"Удалить",
					buttonicon:"ui-icon-trash",
					onClickButton: deleteRow,
					position: "last",
					title:"",
					cursor:"pointer"
					
				}
			);
			
			$("#grid").navButtonAdd('#pager',
				{	caption:"Связанные ответы",
					buttonicon:"ui-icon-document",
					onClickButton: showLinkedAnswerList,
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
			$("#dialog-form").dialog('option', 'title', 'Создание нового вопроса');
			$( "#dialog-form" ).dialog( "open" );
		}
		
		function editRow(){
			var s = $("#grid").jqGrid('getGridParam','selrow');
			var dataFromTheRow = $('#grid').jqGrid ('getRowData', s);
			if(dataFromTheRow.id==null || dataFromTheRow.questionText == null){
				alert("Пожалуйста, выберите ответ для редактирования");
				return;
			}
			getQuestionData(dataFromTheRow.id);
		}
		
		function getQuestionData(questionId){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.questionId = questionId;
			
			var jsonData = JSON.stringify(dataToSend);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"json/getQuestion",
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
							$("#question_id").val(data.id);
							$("#answerText").val(data.questionText);
							if(data.enabled!=null) {
								if(data.enabled==true){
									$('#useInQuiz').prop('checked', true);
								}else{
									$('#useInQuiz').prop('checked', false);
								}
							}
							
							$("#dialog-form").dialog('option', 'title', 'Редактирование вопроса');
							$( "#dialog-form" ).dialog( "open" );
							
						}
					
				},
				failure: function(errMsg){alert(errMsg);}
			});
		}
		
		function deleteRow(){
			var s = $("#grid").jqGrid('getGridParam','selrow');
			var dataFromTheRow = $('#grid').jqGrid ('getRowData', s);
			if(dataFromTheRow.id==null || dataFromTheRow.questionText == null){
				alert("Пожалуйста, выберите вопрос для удаления");
				return;
			}
			$("#delete_id").val(dataFromTheRow.id);
			$("#answerTextDeleteForm").val(dataFromTheRow.questionText);
			
			if(dataFromTheRow.enabled!=null) {
				if(dataFromTheRow.enabled=="true"){
					$('#useInQuizDeleteForm').prop('checked', true);
				}else{
					$('#useInQuizDeleteForm').prop('checked', false);
				}
			}
			$("#answerTextDeleteForm").attr('disabled', 'disabled');
			$("#useInQuizDeleteForm").attr('disabled', 'disabled');
			
			$( "#dialog-delete-form" ).dialog( "open" );
		}
		
		function showLinkedAnswerList(){
			var s = $("#grid").jqGrid('getGridParam','selrow');
			var dataFromTheRow = $('#grid').jqGrid ('getRowData', s);
			if(dataFromTheRow.id==null || dataFromTheRow.questionText == null){
				alert("Пожалуйста, выберите вопрос для просмотра");
				return;
			}
			
			loadLinkedAnswers(dataFromTheRow.id, false);
		}
		
		function loadLinkedAnswers(questionId, openForm){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.questionId = questionId;
			var jsonData = JSON.stringify(dataToSend);

			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"json/getAnswersForQuestion",
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
							$('#answersGrid').jqGrid("clearGridData");
							if(data.answerList!=null){
								$("#answersGrid").jqGrid('setGridParam',{ 
							            data:data.answerList
							        }).trigger("reloadGrid");
								if(openForm == false){
									$( "#linkedAnswers-form" ).dialog( "open" );
								}
							}
						}
				},
				failure: function(errMsg){alert(errMsg);}
			});
			
			
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
			dataToSend.responseType = 1;
			dataToSend.questionGroup = questionGroup;
			dataToSend.questionParentId = null;
			dataToSend.enabled = enabled;
			
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
							loadGridData(questionGroup);
						}
					/*getNextPage();*/
				},
				failure: function(errMsg){alert(errMsg);}
			});
		};
		
		
		$(window).bind('resize',function(){
			var width = $(window).width();
			if(width==null || width <1){
				width = $(window).width();
			}
			width = width-220;
			if(width > 200){
				$("#grid").setGridWidth(width);
			}
		}
		).trigger('resize');
		
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
			
			<td width="100% - 220px">
				<div>
					<p>Выберите группу вопросов:</p>
					<select id="list" onchange="getListGroup()">
						<option disabled>Выберите группу вопросов</option>
					</select>
		
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
				<input type="text" name="question_id" id="question_id" style="display: none;" class="text ui-widget-content ui-corner-all" />
				<label for="name">Текст ответа</label>
				<input type="text" name="answerText" id="answerText" width="500" class="text ui-widget-content ui-corner-all" />
				</br>
				<input type="checkbox" name="option1" id="useInQuiz" value="a1" checked>Использовать в тесте<br>
				</br>
			</fieldset>
		</form>
	</div>
	
	<div id="answerForQuestionEditForm" title="Редактирование ответа для вопроса">
		<form>
			<fieldset>
				<input type="text" name="af_question_id" id="af_question_id" style="display: none;" class="text ui-widget-content ui-corner-all" />
				<input type="text" name="af_answer_id" id="af_answer_id" style="display: none;" class="text ui-widget-content ui-corner-all" />
				<label for="name">Текст вопроса</label>
				<input type="text" name="af_questionText" id="af_questionText" width="500" size="60" class="text ui-widget-content ui-corner-all" />
				<label for="name">Текст ответа</label>
				<input type="text" name="af_answerText" id="af_answerText" width="500" size="60" class="text ui-widget-content ui-corner-all" />
				</br>
				<input type="checkbox" name="af_rightInQuestion" id="af_rightInQuestion" value="a1" checked>Правильный ответ<br>
				</br>
			</fieldset>
		</form>
	</div>
	
	<div id="linkedAnswers-form" title="Связанные ответы">
		<div id="jqAnswersGrid">
			<table id="answersGrid"></table>
			<div id="answersPager"></div>
		</div>
	</div>
	
	<div id="answers-form" title="Список ответов">
		<form>
			<p>Поиск по ответам: </p>
			<input type="text" size="40" name="search_text" id="search_text">
  			<input type="button" name="search_button" id="search_button" onClick="onSearch()" value="ПОИСК"/>
		</form>
		<br>
		<div id="jqAnswGrid">
			<table id="answGrid"></table>
		</div>
	</div>
	
	<div id="dialog-delete-form" title="Удалить ответ">
		<p class="validateTips">Вы уверены что хотите удалить вопрос</p>
		<form>
			<fieldset>
				<table>
					<input type="text" name="delete_id" id="delete_id" style="display: none;" class="text ui-widget-content ui-corner-all" />
					<label for="name">Текст вопроса</label>
					<input type="text" name="answerTextDeleteForm" id="answerTextDeleteForm" width="500" class="text ui-widget-content ui-corner-all" />
					<input type="checkbox" name="option1" id="useInQuizDeleteForm" value="a1">Использовать в тесте</input>
				</table>
			</fieldset>
		</form>
	</div>
	
</body>
</html>