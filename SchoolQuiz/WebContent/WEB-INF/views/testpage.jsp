<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 
<head>
 <link rel="stylesheet" type="text/css" media="screen" href="../../css/jquery-ui-1.9.0.custom.css" />
 <link rel="stylesheet" type="text/css" media="screen" href="../../css/ui.jqgrid.css" />
 
 <script type="text/javascript" src="../../js/jquery-1.8.2.js"></script>
 <script type="text/javascript">
     var jq = jQuery.noConflict();
 </script>
 <script type="text/javascript" src="../../js/jquery-ui-1.9.0.custom.min.js"></script>
 <script type="text/javascript" src="../../js/grid.locale-ru.js" ></script>
 <script type="text/javascript" src="../../js/jquery.jqGrid.min.js"></script>
 <script type="text/javascript" src="../../js/jquery.ddslick.min.js"></script>
  
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Insert title here</title>
</head>
<body>

	<script type="text/javascript">
		function sendPost(){
			alert("Start POST");
			var data = new Object();
			data.name="Daniels";
			alert("JSON - "+JSON.stringify(data));
			jq.ajax({
				type: "POST",
				url: "http://127.0.0.1:8080/schoolQuiz/quiz/service/json/createUserResult",
				data: JSON.stringify(data),
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success: function(data){alert(data);},
				failure: function(errMsg){alert(errMsg);}
			});
		}
	</script>

	<div>
		<input type="button" value="Послать запрос" onClick='sendPost()'>
	</div>

</body>
</html>