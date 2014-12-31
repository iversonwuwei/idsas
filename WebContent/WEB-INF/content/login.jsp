<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="inc/header.jsp" %>
<html>
	<head>
		<link href="<%=basePath%>css/login.css" rel="stylesheet" type="text/css" />
		<title>iDSAS</title>
		<script type="text/javascript">
			$(function() {
				$("#username").focus();
				document.onkeydown = function(e) {
					e = e ? e : window.event;
					var code = e.which ? e.which : e.keyCode;
					if (code == 13) {
						$("#submit").click();
					}
				};
			});
	
			function login() {
				$("#msg").html("");
				if ("" == $.trim($("#username").val())) {
					$("#msg").html("Please enter your username");
					$("#username").focus();
					return;
				}
				if ("" == $.trim($("#password").val())) {
					$("#msg").html("Please enter your password");
					$("#password").focus();
					return;
				}
				if ("" == $.trim($("#random").val())) {
					$("#msg").html("Please enter the security code");
					$("#random").focus();
					return;
				}
				var checkResult = false;
				$.ajax( {
					type : "POST",
					url : basePath + "login!checkRandom?time=" + new Date().getTime(),
					async : false,
                    data : {
                    	render : $("#random").val()
                    },
                    success : function(data) {
                        if ('true' == data) {
                            checkResult = true;
                        }
                    }
				});
				if(!checkResult) {
					$("#msg").html("Security code is incorrect, please re-enter");
					getNew();
					$("#random").select();
					$("#random").focus();
					return;
				}
				$.ajax( {
					type : "POST",
					async : false,
					data : {
						'username' : $.trim($("#username").val()),
						'password' : $.trim($("#password").val())
					},
					url : "<%=basePath%>login!logon",
					dataType : "text",
					success : function(data) {
						if ("error" == data) {
							$("#msg").html("Username or password is incorrect, please re-enter");
							$("#username").focus();
							return;
						}
						if("${id}"!=null && "${id}"!=""){
							window.parent.location = basePath + "layout/welcome!index?cardid=${id}"; 
						}else{
						window.parent.location = basePath + "layout/welcome";
						}
					}
				});
			}
			if (0 != parent.frames.length) {
				window.parent.location = basePath + "login";
			}

			function getNew() {
				document.getElementById("aCode").src = basePath + "login!code?time="+ new Date().getTime();
				$("#random").focus();
				$("#random").val("");
			}
			
			function clearLogin() {
				$("#username").val("");
				$("#password").val("");
				getNew();
				$("#username").focus();
			}
		</script>
	</head>
	<body>
		<div id="bg">
	    	<div id="login">
				<div id="top_top">
		    		<div id="top_top_left"></div>
		    		<div id="top_top_right">
		    			<img width="243" height="65" src="<%=basePath%>images/SyCloud-ZyVision-GyTrack-logos_02.png">
		    		</div>
				</div>
				<div id="top">
					<div id="top_left"></div>
					<div id="top_center"></div>
				</div>
				<div id="center">
					<div id="center_left"></div>
					<div id="center_middle">
						<div style="position:relative; width:240px; height:200px;">
		                	<div style="position:absolute; top:10px; left:87px; width:132px;height:28px;">
		                		<input id="username" name="username" type="text" />
		                	</div>
		                	<div style="position:absolute; top:50px; left:87px; width:132px;height:28px;">
		                		<input id="password" name="password" type="password" />
		                	</div>
		                	<div style="position:absolute; top:90px; left:87px; width:70px;height:28px;">
		                		<input id="random" name="r1" type="text" style="width:67px;" maxlength="4"/>
		                	</div>
		                	<div style="position:absolute; top:90px; left:160px; width:70px;height:26px;">
		                		<img id="aCode" style="cursor: pointer;" align="middle" alt="Security code" src="<%=basePath%>login!code?" onclick="getNew()" />
		                	</div>
		                	<div style="position:absolute; top:125px; left:20px; width:200px;height:26px; font-size:12px; color:#ff0000;">
		                		<span id="msg"></span>
		                	</div>
		                	<div style="position:absolute; top:160px; left:17px; width:82px;height:28px;">
		                		<a href="#" onclick="clearLogin();">
		                			<img src="<%=basePath%>images/login_button_03.jpg" width="82" height="32" />
		                		</a>
	                		</div>
		                	<div style="position:absolute; top:160px; left:120px; width:101px;height:28px;">
		                		<a href="#" onclick="login();" id="submit">
		                			<img src="<%=basePath%>images/login_button_05.jpg" width="101" height="31" />
		                		</a>
		                	</div>
						</div>
					</div>
					<div id="center_right"></div>		 
				</div>
				<div id="down">
					<div id="down_left">
						<div id="inf"><span class="copyright">Copyright &copy; 2008-2014 KZTech Pte Ltd</span></div>
					</div>
					<div id="down_center">
						<div style="margin-top:72px; margin-left:10px; width:215px; text-align:right; height:20px; color:#999999; font-family:Arial, Helvetica, sans-serif;">Intelligent Driver Safety Analysis System 2014 V2.1.4_140912</div>
					</div>		 
				</div>
			</div>
		</div>
	</body>
</html>