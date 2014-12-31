<%
	String path = request.getContextPath() + "/";
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
	<link rel="icon" href="<%=basePath%>images/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="<%=basePath%>images/favicon.ico" type="image/x-icon" />
		<title>iDSAS</title>
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Cache-Control" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
		<script type="text/javascript">
		var date=new Date(); 
		var expiresDays=10; 
		//将date设置为10天以后的时间 
		date.setTime(date.getTime()+10*24*3600*1000); 
		//将userId和userName两个cookie设置为10天后过期 
		document.cookie="localhost_sso_id=login${mainUser.userID}; expires="+date.toGMTString(); 
		//alert(document.cookie);
		
		 window.onbeforeunload = onbeforeunload_handler; 
		 function onbeforeunload_handler(){   
			 document.cookie="localhost_sso_id=2222; expires="+date.toGMTString(); 
		        return ;
		    }   
		
		</script>
	</head>
	<frameset id="allFrame" rows="61,*,23" cols="*" frameborder="no" border="0" framespacing="0" >
		<frame src="<%=basePath%>layout/top" id="topFrame" title="topFrame" name="topFrame" scrolling="No" noresize="noresize" />
		<frameset cols="212,*" frameborder="no" border="0" framespacing="0" >
    		<c:if test="${cardid==null || cardid=='' }">
    		<frame src="<%=basePath%>layout/left" id="leftFrame" name="leftFrame" title="leftFrame" noresize="noresize" />
    		<frame src="<%=basePath%>monitor/real-time-new" id="mainFrame" name="mainFrame" title="mainFrame" scrolling="No"/>
    		</c:if>
    		<c:if test="${cardid!=null && cardid!='' }">
    		<frame src="<%=basePath%>layout/left/${cardid }/index" id="leftFrame" name="leftFrame" title="leftFrame" noresize="noresize" />
    		<frame src="<%=basePath%>task/schedule?editble=1&cardid=${cardid }" id="mainFrame" name="mainFrame" title="mainFrame" scrolling="No"/>
    		</c:if>
    		
		</frameset>
		<frame src="../frame/bottom.html" id="bottomFrame" name="bottomFrame" title="bottomFrame" scrolling="No" noresize="noresize" />
	</frameset>
	<noframes>
		<body>
		</body>
	</noframes>
	
</html>