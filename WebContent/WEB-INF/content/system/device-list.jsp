<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/system/devicelist_.js"></script>
		<script type="text/javascript" charset="UTF-8" src="<%=basePath %>scripts/common/jquery-1.2.6.js" ></script>
		<style>
			*{margin:0;padding:0;}
			.divBackGround {
				display: none;
				filter: alpha(opacity =   70);	/* 修改IE浏览器的透明度 */
				-moz-opacity: 0.7;	/* 修改MOZ浏览器的透明度 */
				opacity: 0.7;	/* 修改MOZ浏览器的透明度 */
				background: #ccc;
				position: absolute; /* 绝对路径 */
				z-index: 99;
				text-align: center;
			}
			.mainInputTreeDiv {
				display: none;	
				width: 747px;
				height:200px;
				z-index: 100;
				position: absolute;
			}
			.innerInputTreeDiv {
				filter: alpha(opacity = 100);
				-moz-opacity: 1.0; /* Moz + FF */
				opacity: 1.0;
				float:left;
				width: 300px;
				height:120px;
				overflow: hidden;
				border-color:#2383C0; 
				border-style:solid; 
				border-width:1px 1px 1px 1px;
				background-color: #FFFFFF;
				position: absolute; /* 绝对路径 */
			}
			.welcomediv {
			    background: url("../images/temp_32.gif") repeat scroll 0 0 rgba(0, 0, 0, 0);
			    height: 29px;
			}
	
			.innerInputTreeDiv table {
			    background: none repeat scroll 0 0 #FFFFFF;
			    border-top: 1px solid #C0C9CF;
			    width: 100%;
			}
			.innerInputTreeDiv td {
			    border-bottom: 1px solid #C0C9CF;
			    border-right: 1px solid #C0C9CF;
			    padding: 1px;
			}
		</style>
		<script type="text/javascript">
			function opentable() {
				$("#backGroundDiv")
					.addClass("divBackGround")
					.css("left",0)
					.css("top",0)
					.width($(window).width())
					.height($(window).height())
					.show();
				$("#mainInputTreeDiv")
					.css("left",(($(window).width()-300)/2))
					.css("top",getTopSize())
					.addClass("mainInputTreeDiv").show("quick");
				$("#inputTreeDiv")
					.addClass("innerInputTreeDiv")
					.show("quick");
				$("#inputTreeDiv").focus();
			}
			
			function getTopSize() {
				var topSize = ($(window).height()-400)/2;
				return topSize < 0 ? 0 : topSize;
			}
			
			function close2() {
				$("#inputTreeDiv").hide();
				$("#backGroundDiv").hide();
				$("#mainInputTreeDiv").hide();
			}

			function selectAll(aa) {
		    	$("input[id='boxs']").attr("checked",aa);
			}

		  	function bedit() {
		        var box=$("input[id='boxs']");
		        var temp=false;
		        for (var boxi = 1; boxi < box.length; boxi ++) {
					if(box[boxi].checked) {		
						temp=true;
						break;
					}
				}
		        if(!temp) {
					alert("Please select batch modify vehicles");
					return; 
				} else {
					opentable();
				}
			}
		  	
		  	function bsub(){
				if($("#ips").val() == null || $.trim($("#ips").val()) == ""){
					alert("Please enter the ip");
					return;
				}
				if($("#ports").val() == null || $.trim($("#ports").val()) == ""){
					alert("Please enter the port");
				  	return;
			  	}
			  	var ports = $.trim($("#ports").val());
			  	var ips = $.trim($("#ips").val());
			  	var s = new Array();
				s = $("input[id='boxs']");
				var ary = new Array();
				for ( var boxi = 0; boxi < s.length; boxi++) {
					if(s[boxi].checked){
						ary.push(s[boxi].value);
				  	}
				}
			
				$.ajax({
				    type: "POST",
				    async: false,
				    url : "<%=basePath%>system/device-list!vbatch",
				 	 dataType : "text",
				 	 data:{
				 		   'serverip':ips,
				 		  'serverport':ports,
				 		    "boxids":ary} ,
					success : function(data) {		 
						alert("Save successfully!");
						close2();
						location.reload();
				    
				    } 
				});
			}
		  	
		  	function excel(){
		     	if(0 == "${fn:length(page.result)}") {
		         	alert("No derived data!");
		         	return;
		     	}
		     	document.forms['queryResult'].action = basePath + 'system/device-list!exportDetail';
				document.forms['queryResult'].submit();
				document.forms['queryResult'].action = basePath + 'system/device-list!index';
		     }
		</script>
	</head>
	<body>
		<!--------------------------------------------------------- 透明层 -------------------------------------------------------------------->
		<div id="backGroundDiv" class="divBackGround"></div>
		<div id="mainInputTreeDiv" class="mainInputTreeDiv">
			<div id="inputTreeDiv"  class="innerInputTreeDiv">
				<div class='welcomediv'></div>
				<table style="width: 100%;">
					<thead>
						<tr>
							<td class="text_bg" width="10%">Server Ip:</td>
							<td width="10%"><input id="ips"></td>
						</tr>
						<tr>
							<td class="text_bg">Server Port:</td>
							<td><input id="ports"></td>
						</tr>
					</thead>
				</table>
				<div style="width: 100%;bottom: 5px;position: absolute;" >
					<div style="float: right;width: 80px;">
						<img src="<%=basePath%>images/button_sure.jpg" style="cursor: pointer;" alt="Confirm" onclick="bsub()"/>
					</div>
					<div style="float: right;width: 80px;">
						<img src="<%=basePath%>images/button_cancel.jpg" style="cursor: pointer;" alt="Cancel" onclick="close2();"/>
					</div>
				</div>
			</div>
		</div>
		<!------------------------------------------------ 透明层 -------------------------------------------------------->	
		<custom:navigation father="Organization Management" model="Device" excelMethod="excel()" addPath="system/device-list" addMethod="add">
			<%--
			
			<li style="width: 160px;padding-right: 10px">
				<a href="#" onclick="bedit();" style="width: 160px;"><img src="<%=basePath%>images/dtree/ico_checkin.gif" align="absmiddle" border="0" />&nbsp;Set Ip and Port</a>
			</li>
			 --%>
		</custom:navigation>
		<form name="queryResult" action="<%=basePath%>system/device-list!index" method="post">
			<input type="hidden" name="editble" value="${editble}"/>
			<custom:searchBody>
				<custom:searchItem title="Department" side="first">
					<custom:selCom name="deVo.deptID" value="${deVo.deptID}"/>
				</custom:searchItem>
				<custom:searchItem title="Device Name">
					<input type="text" name="deVo.name" value="${deVo.name}" maxlength="25" />
				</custom:searchItem>
				<custom:searchItem title="Device Type">
					<s:select id="deviceType" list="#{'1':'BNT5000', '2':'A5', '3':'Police'}" name="deVo.deviceType" velue="%{deVo.deviceType}" listKey="key" listValue="value" theme="simple" headerKey="-1" headerValue="All" />
				</custom:searchItem>
				<custom:searchItem title="Vehicle/Police">
					<input type="text" name="deVo.vname" value="${deVo.vname}" maxlength="25" />
				</custom:searchItem>
				<custom:searchItem title="IP">
					<input type="text" name="deVo.host" value="${deVo.host}" maxlength="25" />
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<thead>
					<tr class="Reprot_Head">
						<!-- <td width="3%">
							<input type="checkbox" id="scheck"  onclick="selectAll(this.checked)">
						</td> -->
						<td width="3%" rowspan="1"><label>No.</label></td>
						<custom:sort property="o_devicename" page="${page}" width="15%">Device Name</custom:sort>
						<custom:sort property="o_busname" page="${page}" width="15%">Department</custom:sort>
						<custom:sort property="o_unittype" page="${page}" width="13%">Device Type</custom:sort>
						<custom:sort property="o_busname" page="${page}" width="15%">Vehicle/Police</custom:sort>
						<custom:sort property="o_loginhost" page="${page}" width="15%">IP</custom:sort>
						<custom:sort property="o_channelcount" page="${page}" width="12%">Channel Count</custom:sort>
						<td width="8%" rowspan="1"><label>Operate</label></td>
					</tr>
				</thead>
				<s:iterator value="page.result" var="u" status="i">
				<tr class="Reprot_td" id="tr${u. o_deviceno}">
					<%-- <td><input type="checkbox" id="boxs" value="${u. o_deviceno}"></td> --%>
					<td>${i.index + 1 + (page.currentPage - 1) * page.pageSize}</td>
					<custom:tableItem>${u.o_devicename}</custom:tableItem>
					<custom:tableItem>${u.deptname}</custom:tableItem>
					<custom:tableItem list="{'1':'BNT5000','2':'A5','3':'Police'}">${u.o_unittype}</custom:tableItem>
					<custom:tableItem>${u.o_busname}</custom:tableItem>
					<custom:tableItem>${u.o_loginhost}</custom:tableItem>
					<custom:tableItem>${u.o_channelcount}</custom:tableItem>
					<custom:tableItem>
						<custom:show path="system/device-list" id="${u.o_deviceno}"/>
						<s:if test="%{o_unittype == 2 || o_unittype == 3}">
							<s:if test="%{o_busname == null || o_busname == ''}">
								<custom:edit path="system/device-list" id="${u.o_deviceno}"/>
							</s:if>
						</s:if>
						<s:else>
							<custom:edit path="system/device-list" id="${u.o_deviceno}"/>
						</s:else>
						<custom:del path="system/device-list" id="${u.o_deviceno}" jsmethod="del"/>
					</custom:tableItem>
				</tr>
				</s:iterator>
			</custom:table>
			<s:include value="/common/page.jsp">
				<s:param name="pageName" value="'page'"/>
				<s:param name="formName" value="'queryResult'"/>
				<s:param name="linkCount" value="5"/>
		    </s:include>
		</form>
	</body>
</html>