<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>


	 <link rel="stylesheet" type="text/css" href="../../css/mainMenuStyles.css" />
	 <link rel="stylesheet" type="text/css" href="../../css/jquery-ui-1.9.0.custom.css" />
 	 <link rel="stylesheet" type="text/css" href="../../css/ui.jqgrid.css" />
 	 <link rel="stylesheet" type="text/css" href="../../css/easyui.css" />
 	 <link rel="stylesheet" type="text/css" href="../../css/icon.css" />
 	 
 	 <script type="text/javascript" src="../../js/jquery-1.8.2.js"></script>
	 <script type="text/javascript" src="../../js/jquery.cookie.js"></script>
	 <script type="text/javascript" src="../../js/jquery.easing.1.3.js"></script>
	 <script type="text/javascript" src="../../js/grid.locale-ru.js"></script>
	 <script type="text/javascript" src="../../js/jquery.jqGrid.min.js"></script>
	 <script type="text/javascript" src="../../js/jquery.easyui.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Список Вопросов</title>
<script type="text/javascript">
		$(document).ready(function(){
			$("li").mouseover(function(){
				$(this).stop().animate({height:'100px'},{queue:false, duration:300, easing:'easeOutBounce'});
			});
			$("li").mouseout(function(){
				$(this).stop().animate({height:'70px'},{queue:false, duration:300, easing:'easeOutBounce'});
			});
			loadQuestionGroups();
			createGrid();
			
		});
		
		function loadQuestionGroups(){
		    $('#list').combobox({  
		        url:null, 
		        mode:'local',
		        valueField:'id',  
		        textField:'text',
		        onSelect: function(param){
		    		loadGridData(param.id);
		    	}
		    });
		    
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
		            	$('#list').combobox('loadData',jsonObj.dictItems);
		            	if(jsonObj.dictItems.length>0){
		            		$('#list').combobox('setValue',jsonObj.dictItems[0].text);
		            		loadGridData(jsonObj.dictItems[0].id);
		            	}
		            	
					}
		        }
		    });
		    
		}
		
		function loadGridData(groupId){
			alert("Start to load data for group with id - "+groupId);
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
		}
		
		function editRow(){
			
		}
		
		function deleteRow(){
			
		}
		
		function showQuestionsList(){
					
		}
		
		function getListGroup(){
			alert("get list group!");
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
/*		
		$(function(){
			
			$('#cc').combobox({  
			    url:'combobox_data.json',  
			    valueField:'id',  
			    textField:'text'  
			});
			
		});
*/		
			
		

</script>
</head>
<body>
	<div>
		<p>Выберите группу вопросов:</p>
		<select id="list" class="easyui-combobox" name="list" style="width:200px;" onClick="getListGroup"></select>
	</div>
	<br>
	<table width="100%" id="positionTable">

			<td width="100% - 200px">
				<div id="jqgrid">
					<table id="grid"></table>
					<div id="pager"></div>
				</div>
		
			</td>
		</tr>
	</table>
</body>
</html>