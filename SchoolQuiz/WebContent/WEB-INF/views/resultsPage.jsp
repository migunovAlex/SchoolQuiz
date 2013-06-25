<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
<title>Results Page</title>
	<script type="text/javascript">
	
		$(document).ready(function(){
			$("li").mouseover(function(){
				$(this).stop().animate({height:'100px'},{queue:false, duration:300, easing:'easeOutBounce'});
			});
			$("li").mouseout(function(){
				$(this).stop().animate({height:'70px'},{queue:false, duration:300, easing:'easeOutBounce'});
			});
			createDateComponent();
			createGrid();
		});
		
		function createDateComponent(){
			$( "#datepicker" ).datepicker();
		}
		
		function createGrid(){
			$("#grid").jqGrid({
				url:"",
				datatype:'local',
				colNames:["Дата", "Адрес IP", "Тестируемый","Время", "Вопрос","Ответ", "Правильный"],
				colModel:[
				{name:'dateOfAnswer', index:'dateOfAnswer', width:40, editable:false},
				{name:'computerIP', index:'computerIP', width:40, editable:false},
				{name:'answerer', index:'answerer', width:60, editable:false},
				{name:'timeOfAnswer', index:'timeOfAnswer', width:40, editable:false},
				{name:'questionAnswer', index:'questionAnswer', width:190, editable:false},
				{name:'userAnswer', index:'userAnswer', width:110, editable:false},
				{name:'right', index:'right', width:40, editable:false}
				],
				height:500,
				autowidth:true,
				rownumbers: true,
				sortname:'answerer',
				viewrecords:true,
				sortorder:"asc",
				caption:"Список результатов",
				emptyrecords:"Пустое поле",
				loadonce:false,
				 grouping: true, groupingView : 
				 { 	groupField : ['computerIP', 'answerer','timeOfAnswer'], 
					groupColumnShow : [true, true, true], 
					groupText : ['<b>{0}</b>'], 
					groupCollapse : true, 
					groupOrder: ['asc', 'asc'], 
					groupSummary : [false, false, false] }
			});
		}
		
		function loadResultItems(){
			 var selectedDate =$("#datepicker").val();
			 if(selectedDate.length!=10){
				 alert("Введите корректную дату");
			 }else
				 {
				 	var userSession = $.cookie("ADMIN_SESSION");
				 	var dataToSend = new Object();
					dataToSend.userSession = userSession;
					dataToSend.resultDate = selectedDate;
					
					var jsonData = JSON.stringify(dataToSend);
					$.ajax({
						type:"POST",
						url:$.cookie("SERVER_HOST")+"json/getUserResults",
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
									if(data.userResults!=null){
										$("#grid").jqGrid('setGridParam',{ 
									            data:data.userResults
									        }).trigger("reloadGrid");
									}
									
								}
						},
						failure: function(errMsg){alert(errMsg);}
					});
				 }
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
		
		function getQuestionsInGroups(){
				var userSession = $.cookie("ADMIN_SESSION");
				var loc = $.cookie("SERVER_HOST")+"pages/questionsInGroups?userSession="+userSession;
				location=loc;
			}
		
		function getResults(){
			var userSession = $.cookie("ADMIN_SESSION");
			var loc = $.cookie("SERVER_HOST")+"pages/resultsPage?userSession="+userSession;
			location=loc;
		}
		
		function getMain(){
			var userSession = $.cookie("ADMIN_SESSION");
			var loc = $.cookie("SERVER_HOST")+"pages/main?userSession="+userSession;
			location=loc;
		}
		
		$(window).bind('resize',function(){
			var width = $(window).width()-200;
			if(width==null || width <1){
				width = $(window).width() - 200;
			}
			width = width-30;
			if(width > 200){
				$("#grid").setGridWidth(width);
			}
		}
		).trigger('resize');
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
			
			<td width="100% - 230px">
				<table>
					<tr>
						<td>
							<p>Выберите дату для вывода результатов: <input type="text" id="datepicker" /></p>
						</td>
						<td>
							<input type="button" id="loadData" onclick="loadResultItems()" value="Загрузить данные"/>
						</td>
					</tr>
				</table>
				<div id="jqgrid">
					<table id="grid"></table>
				</div>
			</td>
			
		</tr>
</table>
	
</body>
</html>