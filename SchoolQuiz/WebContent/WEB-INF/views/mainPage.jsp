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
		
		function createGrid(){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			dataToSend.numberFrom = "0";
			dataToSend.numberOfItems = "20";
			var jsonData = JSON.stringify(dataToSend);
			$("#grid").jqGrid({
				url:$.cookie("SERVER_HOST")+"pages/json/getGroupList",
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
					onClickButton: addRow,
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
			
			/*$("#grid").jqGrid('filterToolbar',{stringResult:true, searchOnEnter:true, defaultSearch:'cn'});*/
			
		}
		
		function addRow(){
			$("#grid").jqGrid('editGridRow','new',
				{url:$.cookie("SERVER_HOST")+"pages/json/addGroup",
					editData:{},
					recreateForm:true,
					beforeShowForm: function(form){},
					closeAfterAdd:true,
					reloadAfterSubmit:false,
					afterSubmit: function(response, postdata){
						alert("addingResult - "+response.errorData.errorCode);
					}
				}
			);
		}
		
		function editRow(){
			
		}
		
		function deleteRow(){
			
		}
		
		function closeForm(){
			var userSession = $.cookie("ADMIN_SESSION");
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			var jsonData = JSON.stringify(dataToSend);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"pages/finishUserSession",
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
											<p><a href="#">Вопросы в группе</a></p>
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

</body>
</html>