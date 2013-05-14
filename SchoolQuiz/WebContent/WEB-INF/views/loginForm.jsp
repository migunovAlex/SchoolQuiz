<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 
<head>
 <link rel="stylesheet" type="text/css" href="../../css/screen.css" />
 
 <script type="text/javascript" src="../../js/jquery-1.8.2.js"></script>
 <script type="text/javascript" src="../../js/jquery.cookie.js"></script>
  
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Login form</title>

	<script type="text/javascript">
	
		$(document).ready(function(){
			var adminSession = $.cookie("ADMIN_SESSION");
			if(adminSession!=null){
				/*alert("Already logined. starting!");*/
				checkSession(adminSession);
			}
			
			var serverHost = $.cookie("SERVER_HOST");
			if(serverHost==null){
				$.cookie("SERVER_HOST","http://http://192.168.0.100:8090/schoolQuiz/quiz/");
			}
			
			$(document).mouseup(function(){
				$("#loginform").mouseup(function(){
					return false;
				});
				$("a.close").click(function(e){
					e.preventDefault();
					$("#loginform").hide();
					$(".lock").fadeIn();
				});
				if($("loginform").is(":hidden")){
					$(".lock").fadeOut();
				}else{
					$(".lock").fadeIn();
				}
				$("#loginform").toggle();
			});
			
			$("input#cancel_submit").click(function(e){
				$("#loginform").hide();
				$(".lock").fadeIn();
			})
		});
		
		function checkSession(userSession){
			var dataToSend = new Object();
			dataToSend.userSession = userSession;
			var jsonData = JSON.stringify(dataToSend);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"pages/checkUserSession",
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
					getNextPage();
				},
				failure: function(errMsg){alert(errMsg);}
			});
			
		}
		
		function sendCredentials(){
			var dataToSend = new Object();
			var login = $("#username").val();
			var pass = $("#password").val();
			dataToSend.username = login;
			dataToSend.password = pass;
			var jsonData = JSON.stringify(dataToSend);
			$.ajax({
				type:"POST",
				url:$.cookie("SERVER_HOST")+"json/login",
				data: jsonData,
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success: function(data){
					var errorCode = data.errorData.errorCode;
					if(errorCode!=200){
						alert(data.errorData.errorDescription);
						return;
					}
					var currentSession = data.adminUserSession.session; 
					if(currentSession!=null){
						$.cookie("ADMIN_SESSION",currentSession);
						getNextPage();
					}
				},
				failure: function(errMsg){alert(errMsg);}
			});
		}
		
		function getNextPage(){
			var userSession = $.cookie("ADMIN_SESSION");
			location=$.cookie("SERVER_HOST")+"pages/main?userSession="+userSession;
		}
		
	</script>

</head>
<body>

	<script type="text/javascript">
		function sendPost(strToSend){
			alert("Start POST");
			var data = new Object();
			data.name="Daniels";
			alert("JSON - "+JSON.stringify(data));
			jq.ajax({
				type: "POST",
				url: $.cookie("SERVER_HOST")+"service/json/createUserResult",
				data: strToSend,
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success: function(data){alert(data);},
				failure: function(errMsg){alert(errMsg);}
			});
		}
		
		
	</script>
	<div id="cont">

		<div class="box lock"></div>
		<div id="loginform" class="box form">
			<h2>Вход на сайт<a href="" class="close">Закрыть</a></h2>
			<div class="formcont">
				<fieldset id="signin_menu">
				<span class="message">Проверьте правильность данных прежде чем продолжить</span>
					<form id="signin" action="" method="post">
						<p>
							<label for="username">Логин</label>
							<input id="username" title="username" value="" class="required" tabindex="4" type="text" name="username" value=""/>
						</p>
						<p>
							<label for="password">Пароль</label>
							<input id="password" title="password" value="" class="required" tabindex="5" type="password" name="password" value=""/>
						</p>
						<p>
							<div id="error"></div>
						</p>
						<p class="clear"></p>
						<p class="remember">
							<input id="signin_submit" tabindex="6" type="button" value="Войти" onclick="sendCredentials()"/>
							<input id="cancel_submit" tabindex="7" type="button" value="Отмена"/>
						</p>
					</form>
				</fieldset>
			</div>
			<div class="formfooter"></div>
		</div>
		</div>
		<div id="bg">
			<div>
				<table cellspacing="0" cellpadding="0">
					<tbody>
						<tr>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

</body>
</html>