<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
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
		width: 700px;
		height:250px;
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
		
		var xxxaaa=true;
		var xxx=new Array();
		
		function carder(id){
			if($("#backGroundDiv").css("display")=='block'){
				return;
			}
			
			xxx.push(id.split("x")[0]);
			xxx.push(id.split("x")[1]);
			if(xxx.length==4){
				//alert(xxx[0]+","+xxx[2]+";"+(xxx[3]-xxx[1])/1000);	
				if((xxx[3]-xxx[1])/1000>5){
					alert('Credit card expires, please try again');
				}else if(xxx[0]==xxx[2]){
					alert("Do not repeat swipe");
				}else{
					$.ajax({
					    type : "POST",
					    async : false,
					    dataType : 'json',
					    data : {
					    	'code1' : xxx[0],
					    	'code2' : xxx[2],
					    	'stime':xxx[3]
					    },
					    url : basePath + "task/schedule!shua",
					    success : function(d) {
					    	if(d.result=='false'){
							alert("no card ");					    		
					    	}else{
					    	settable(d);
					    	opentable();
					    		
					    	}
					    },
					    error : function(d) {
							alert("Exception occurs!");
						}
					});
				}
				xxx=new Array();
			}
		}
		$(function(){
		if("${cardid!=null}"=="${cardid!=''}"){
			
			carder("${cardid}");
		}
			
		})
		function opentable(){

			$("#backGroundDiv")
			.addClass("divBackGround")
			.css("left",0)
			.css("top",0)
			.width($(window).width())
			.height($(window).height())
			.show();

			
			$("#mainInputTreeDiv")
			.css("left",(($(window).width()-700)/2))
			.css("top",getTopSize())
			.addClass("mainInputTreeDiv").show("quick");
		
			
			$("#inputTreeDiv")
				.addClass("innerInputTreeDiv")
				.show("quick");
		
			$("#inputTreeDiv").focus();
		}
		function getTopSize(){
			var topSize = ($(window).height()-200)/2
			return topSize < 0 ? 0 : topSize;
		}
		
		function close2(){
			$("#inputTreeDiv").hide();
			$("#backGroundDiv").hide();
			$("#mainInputTreeDiv").hide();
			$("#linsdiv").hide();
			clean1();
		}
		function clean1(){
			$("#splveh").text("");
			$("#spldri").text("");
			$("#splst").text("");
			$("#splet").text("");
			$("#sveh").text("");
			$("#sdri").text("");
			$("#sst").text("");
			$("#set").text("");
			$("#sdri").css("color","black");
			$("#sveh").css("color","black");
			$("#shuatype").val("");
			$("#schid").val("");
			$("#vehid").val("");
			$("#driid").val("");
			$("#stime").val("");
			$("#etime").val("");
			$("#redri").text("");
		}
		
		function settable(d){
	
			if(d[0]=='zhengchang'){
				$("#splveh").text(d[1].planvehiclenumber);
				$("#spldri").text(d[1].plandrivername);
				$("#splst").text(d[1].planstartime);
				$("#splet").text(d[1].planendtime);
				$("#sveh").text(d[1].planvehiclenumber);
				$("#sdri").text(d[1].plandrivername);
				$("#sst").text(d[1].startime);
				
				$("#shuatype").val("zhengchang");
				$("#stime").val(d[1].startime);
				$("#schid").val(d[1].scheduleid);
			}else if(d[0]=='huiche'){
				$("#splveh").text(d[1].planvehiclenumber);
				$("#spldri").text(d[1].plandrivername);
				$("#splst").text(d[1].planstartime);
				$("#splet").text(d[1].planendtime);
				$("#sveh").text(d[1].vehiclenumber);
				$("#sdri").text(d[1].drivername);
				$("#sst").text(d[1].startime);
				$("#set").text(d[1].endtime);
				$("#redri").text(d[1].redrivername);//回车司机
				$("#driid").val(d[1].redriverid);//回车司机id
				
				$("#shuatype").val("huiche");
				$("#etime").val(d[1].endtime);
				$("#schid").val(d[1].scheduleid);
			}else if(d[0]=='linshika'){
				$("#splveh").text(d[1].planvehiclenumber);
				$("#spldri").text(d[1].plandrivername);
				$("#splst").text(d[1].planstartime);
				$("#splet").text(d[1].planendtime);
				$("#sveh").text(d[1].planvehiclenumber);
				$("#sdri").text(d[1].drivername);
				$("#sdri").css("color","red");
				$("#sst").text(d[1].startime);
				$("#shuatype").val("linshika");
				$("#stime").val(d[1].startime);
				$("#schid").val(d[1].scheduleid);
				$("#driid").val(d[1].driverid);
				$("#linsdiv").show();
				
			}else if(d[0]=='linshiche'){
				$("#splveh").text(d[1].planvehiclenumber);
				$("#spldri").text(d[1].plandrivername);
				$("#splst").text(d[1].planstartime);
				$("#splet").text(d[1].planendtime);
				$("#sveh").text(d[1].vehiclenumber);
				$("#sveh").css("color","red");
				$("#sdri").text(d[1].plandrivername);
				$("#sst").text(d[1].startime);
				$("#shuatype").val("linshiche");
				
				$("#stime").val(d[1].startime);
				$("#schid").val(d[1].scheduleid);
				$("#vehid").val(d[1].vehicleid);
				$("#linsdiv").show();
			}else if(d[0]=='linrenwu'){
				$("#sveh").text(d[1].vehiclenumber);
				$("#sdri").text(d[1].drivername);
				$("#sst").text(d[1].startime);
				$("#shuatype").val("linrenwu");
				
				$("#stime").val(d[1].startime);
				$("#vehid").val(d[1].vehicleid);
				$("#driid").val(d[1].driverid);
				
			}
		}
		
		function subcard(){
			var str="";
			if($("#shuatype").val()=="linshika" || $("#shuatype").val()=="linshiche"){
				if("undefined"==checkr()){
					alert("Please select the reason");
					return;
				}
				var x=checkr();
				if(x==1){
					str='Forgot card';
				}else if(x==2){
					str='Card is damaged ';
				}else if(x==3){
					str='Temporary Substitution';
				}else if(x==4){
					str='Temporary change vehicle ';
				}else if(x==5){
					str=$("#otherin").val();
				}
				
				}
			
			
			$.ajax({
			    type : "POST",
			    async : false,
			    dataType : 'json',
			    data : {
			    	'code1' : $("#shuatype").val(),
			    	'sch.scheduleid' : $("#schid").val(),
			    	'sch.startime':$("#stime").val(),
			    	'sch.endtime':$("#etime").val(),
			    	'sch.driverid':$("#driid").val(),
			    	'sch.vehicleid':$("#vehid").val(),
			    	'sch.duty':str
			    },
			    url : basePath + "task/schedule!subcard",
			    success : function(d) {
			    	
			   close2();
			   if(d.result=='ok'){
		    		location="<%=basePath%>task/schedule?editble=1";
		    	}
			    },
			    error : function(d) {
					alert("Exception occurs!");
				}
			});
		}
		
		function checkr(){

		var yuan;
			var yuanyin=new Array();
			yuanyin=$("input[name='yuanyin']");
			
				for (var ii = 0; ii < yuanyin.length; ii++) {
					if(yuanyin[ii].checked){
						yuan=yuanyin[ii].value;
					}
					
				}
				return yuan;
		}
		
		 function excel(){
	        	if(0 == "${fn:length(page.result)}"){
	            	alert("No derived data!");
	            	return;
	        	}
	        	document.forms['queryResult'].action='<%=basePath%>task/schedule!exportDetail';
	            document.forms['queryResult'].submit();
	            document.forms['queryResult'].action='<%=basePath%>task/schedule!index';
	        }
		</script>
	</head>
	<body>
	
	
	<!-- 透明层 -->
<div id="backGroundDiv" class="divBackGround"></div>
<div id="mainInputTreeDiv" class="mainInputTreeDiv">
	<div id="inputTreeDiv"  class="innerInputTreeDiv" >
	<div class='welcomediv'></div>
	<table style="width: 100%;">
	<thead>
	<tr >
	<td class="text_bg" width="10%">Plan Vehicle:</td>
	<td width="10%">
	<span id="splveh">&nbsp;</span>
	<input type="hidden" id="shuatype">
	<input type="hidden" id="schid">
	<input type="hidden" id="vehid">
	<input type="hidden" id="driid">
	<input type="hidden" id="stime">
	<input type="hidden" id="etime">
	</td>
	<td class="text_bg" width="10%">Plan Driver:</td>
	<td width="10%">
	<span id="spldri">&nbsp;</span>
	</td>
	<td class="text_bg" width="10%">Plan Start time:</td>
	<td width="20%">
	<span id="splst">&nbsp;</span>
	</td>
	<td class="text_bg" width="10%">Plan End time:</td>
	<td width="20%">
	<span id="splet">&nbsp;</span>
	</td>
				
	</tr>
	
	<tr >
	<td class="text_bg">Vehicle:</td>
	<td>
	<span id="sveh">&nbsp;</span>
	</td>
	<td class="text_bg">Driver:</td>
	<td>
	<span id="sdri">&nbsp;</span>
	</td>
	<td class="text_bg">Start time:</td>
	<td>
	<span id="sst">&nbsp;</span>
	</td>
	<td class="text_bg">End time:</td>
	<td>
	<span id="set">&nbsp;</span>
	</td>
	</tr>
	<tr >
	<td class="text_bg">Return Driver:</td>
	<td>
	<span id="redri">&nbsp;</span>
	</td>
	<td class="text_bg">&nbsp;</td>
	<td>&nbsp;</td>
	<td class="text_bg">&nbsp;</td>
	<td>&nbsp;</td>
	<td class="text_bg">&nbsp;</td>
	<td>&nbsp;</td>
	</tr>
	</thead>
	
	
	</table>
	<div id="linsdiv" style="display: none;">
	
	<div class='welcomediv' style="height: 20px;text-align: center;">
	The reason does not match
	</div>
	<table style="width: 100%">
	<tr>
	<td>
	<input type="radio" name="yuanyin" value="1"/>
Forgot card
	<input  type="radio" name="yuanyin" value="2"/>Card is damaged
	<input type="radio" name="yuanyin" value="3"/>Temporary Substitution
	<input type="radio" name="yuanyin" value="4"/>Temporary change vehicle <br/>
	<input type="radio" name="yuanyin" value="5"/>Other:<input id="otherin">
	
	</td>
	</tr>
	</table>
	</div>
	
	<div style="height: 40px;width: 100%;bottom: 5px;position: absolute;" >
	<div style="float: right;width: 80px;">
	<img src="<%=basePath%>images/button_sure.jpg" style="cursor: pointer;" alt="Confirm" onclick="subcard()"/>
	</div>
	<div style="float: right;width: 80px;">
	<img src="<%=basePath%>images/button_cancel.jpg" style="cursor: pointer;" alt="Cancel" onclick="close2();"/>
	</div>
	</div>
	
	
	</div>
	</div>
	
		<!------------------------------------------------------------------------- 透明层 ------------------------------------------------->
		<c:if test="${editble == 1}">
			<custom:navigation father="Task Management" model="Schedules-RFID" addPath="task/schedule" addMethod="add" excelMethod="excel()"/>
		</c:if>
	<%-- 	<c:if test="${editble == 0}">
			<custom:navigation father="User Management" model="User Query" addPath="authority/user" />
		</c:if> --%>
		<form name="queryResult" id="queryResult" action="<%=basePath%>task/schedule!index" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<custom:searchBody row="2">
				<custom:searchItem title="Driver" side="first">
					<input type="text" name="schVo.driverid" value="${schVo.driverid}"/>
				</custom:searchItem>
				<custom:searchItem title="Vehicle Name" >
					<custom:selVehName name="schVo.vehicleid" value="${schVo.vehicleid}" />
				</custom:searchItem>
				<custom:searchItem title="Route" side="last">
					<s:select list="routeList" name="schVo.routeid" id="routeid" listKey="key" listValue="value" headerKey="-1" headerValue="All" theme="simple"></s:select>
				</custom:searchItem>
				<custom:searchItem title="Duty" side="first">
					<input type="text" name="schVo.duty" id="duty" value="${schVo.duty}" maxlength="15" />
				</custom:searchItem>
				<custom:searchItem title="Time" >
					<label class="input_date_l">
						<input type="text" style="width: 140px;" name="schVo.timeMin" value="${schVo.timeMin}" id="startMin" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:&quot;#F{$dp.$D('startMax')}&quot;, readOnly:true});this.blur()" /></label><label class="input_date_r"><input type="text" name="schVo.timeMax" value="${schVo.timeMax}" id="startMax" style="width: 140px;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:&quot;#F{$dp.$D('startMin')}&quot;, readOnly:true});this.blur();"/>
					</label>
				</custom:searchItem>
				<custom:searchItem title="Return" >
<s:select list="#{'1':'Yes','2':'No' }" name="schVo.returntype" id="returntype" listKey="key" listValue="value" headerKey="0" headerValue="All" theme="simple"></s:select>
				</custom:searchItem>
				
				<custom:searchItem side="last">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true">
				<custom:sort property="plandrivername" page="${page}" width="10%">Plan Driver</custom:sort>
					<custom:sort property="planvehiclenumber" page="${page}" width="10%">Plan Vehicle Name</custom:sort>
					<custom:sort property="planstartime" page="${page}" width="11%">Plan Start Time</custom:sort>
					<custom:sort property="planendtime" page="${page}" width="13%">Plan End Time</custom:sort>
					
					<custom:sort property="drivername" page="${page}" width="10%">Driver</custom:sort>
					<custom:sort property="redrivername" page="${page}" width="10%">Return Driver</custom:sort>
					<custom:sort property="vehiclenumber" page="${page}" width="10%">Vehicle Name</custom:sort>
					<custom:sort property="startime" page="${page}" width="13%">Start Time</custom:sort>
					<custom:sort property="endtime" page="${page}" width="13%">End Time</custom:sort>
					
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="scheduleid" index="i" var="u">
					<custom:tableItem>${u.plandrivername}</custom:tableItem>
					<custom:tableItem>${u.planvehiclenumber}</custom:tableItem>
					<custom:tableItem>${u.planstartime}</custom:tableItem>
					<custom:tableItem>${u.planendtime}</custom:tableItem>
					
					<custom:tableItem>${u.drivername}</custom:tableItem>
					<custom:tableItem>${u.redrivername}</custom:tableItem>
					<custom:tableItem>${u.vehiclenumber}</custom:tableItem>
					<custom:tableItem>${u.startime}</custom:tableItem>
					<custom:tableItem>${u.endtime}</custom:tableItem>
					
					
					<c:if test="${editble == 1}">
						<custom:tableItem>
							<custom:show path="task/schedule" id="${u.scheduleid}"/>
							<c:if test="${ u.vehiclenumber!=null }">
							<a href="<%=basePath%>monitor/display!show?id=${u.vehicleid}&scheduleID=${u.scheduleid}"><img src='<%=basePath%>/images/livetou.png' title='View' style='width:16px; height:16px; right:5px; cursor:pointer' alt='View' /></a>
							</c:if>
							<custom:del path="task/schedule" id="${u.scheduleid}" />
							
						</custom:tableItem>	
					</c:if>
					<c:if test="${editble == 0 }">
						<custom:tableItem>
						
							<custom:show path="task/schedule" id="${u.scheduleid}"/>
						</custom:tableItem>
					</c:if>
				</custom:tableBody>
			</custom:table>
			<s:include value="/common/page.jsp">
				<s:param name="pageName" value="'page'"/>
				<s:param name="formName" value="'queryResult'"/>
				<s:param name="linkCount" value="5"/>
		    </s:include>
		</form>
	</body>
</html>