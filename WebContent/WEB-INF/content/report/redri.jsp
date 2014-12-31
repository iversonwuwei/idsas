<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/excanvas.min.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.min.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.pie.min.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.resize.min.js"></script>
	    <script type="text/JavaScript">
			function timeTypeChange(t){
		    	$("#date1,#date2,#date3,#date4").hide().val("");//隐藏那四个日期框
		    	$("#date"+t).show();
			}
			
			$(document).ready(function() {
		    	 $("#behCont").change();
		    	 if (1 == "${dvo.behCont}") {// 然后赋值
			 			$("#date1").val("${dvo.date1}")
			 		}
			 		if (2 == "${dvo.behCont}") {
			 			$("#date2").val("${dvo.date2}")
			 		}
			 		if (3 == "${dvo.behCont}") {
			 			$("#date3").val("${dvo.date3}")
			 		}
			 		if (4 == "${dvo.behCont}") {
			 			$("#date4").val("${dvo.date4}")
			 		}
		    	initTableWidth('maintable5422');
		    	 if(0 < "${fn:length(page.result)}"){
			        	var mysuperTable = new superTable("maintable5422",{headerRows :1,fixedCols : 0});
			        }
		     });
		
		     function initTableWidth(tableID) {
				var eventCount = "${fn:length(listDict)}";	//事件个数
	    		var tWidth = (605 + eventCount *130);	//计算总宽度245位前面固定列宽
	    		if(tWidth < $(".main").width()) {
	    			tWidth = $(".main").width() * 0.98;	//最小宽度位父div宽度
	    		}
	    		var h = $(window).height()
	    		-( null == $(".welcome").height() ? 0 :  $(".welcome").height())
	    		- (null == $(".search_g").height() ? 0 :  $(".search_g").height())
	    		- (null == $(".page").height() ? 0 :  $(".page").height())-175;  
	    		$(".main").height("130px");
	    		$("#pie_chart").height(h+"px");
	    		$("#data_irea").height(h+"px");
	    		tWidth = tWidth.toString() + "px";
	    		$("#" + tableID).width(tWidth);	//设置宽度
	    	}
	     
		    function excel(){
			    if(0 == "${fn:length(page.result)}"){
		    	   alert("No data to export!");
		    	   return;
			    }
			    document.forms['kindForm'].action = basePath + 'report/redri!exportDetail';
		        document.forms['kindForm'].submit();
		        document.forms['kindForm'].action = basePath + 'report/redri!index';
		     }
		    
		    function doSearch() {
		    	if(select_company.getSelectedValue() == '-1' || select_company.getSelectedValue() == '') {
			    	$('#departmentName').val('');
			    } else {
				    $('#departmentName').val(select_company.getComboText());
			    }
		    	if(select_driver.getSelectedValue() == '-1' || select_driver.getSelectedValue() == '') {
			    	$('#driverName').val('');
			    } else {
				    $('#driverName').val(select_driver.getComboText());
			    }
		    	beforeSubmit();
		    	document.queryResult.submit();
		    }
	    </script>
    </head>
	<body>
		<custom:navigation father="Chart Report" model="Report Driver" excelMethod="excel()"/>
		<form id="kindForm" name="queryResult" action="<%=basePath%>report/redri!index" method="post">
			 <s:hidden id="departmentName" name="dvo.orgName" value="%{dvo.orgName}" />
			 <s:hidden id="driverName" name="dvo.driverName" value="%{dvo.driverName}" />
			 <custom:searchBody>
				<custom:searchItem title="Department" side="first">
					<custom:selCom name="dvo.orgId" value="${dvo.orgId }"/>
				</custom:searchItem>
				<custom:searchItem title="Driver" >
					<custom:selDriver name="dvo.driverId" value="${dvo.driverId}" />
				</custom:searchItem>
				<custom:searchItem title="Stat Type" >
					<s:select list="#{1:'Daily',2:'Weekly',3:'Monthly',4:'Yearly'}" id="behCont" name="dvo.behCont" theme="simple" onchange="timeTypeChange(this.value)"/>
				</custom:searchItem>
				<custom:searchItem title="Date">
					<custom:date dateName="dvo.date1" dateValue="${dvo.date1}" id="date1" />
					<custom:date dateName="dvo.date2" dateValue="${dvo.date2}" id="date2" isShowWeek="true" />
					<custom:date dateName="dvo.date3" dateValue="${dvo.date3}" id="date3" dateFmt="yyyy-MM" />
					<custom:date dateName="dvo.date4" dateValue="${dvo.date4}" id="date4" dateFmt="yyyy" />
			</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="doSearch();" />
				</custom:searchItem>
			</custom:searchBody>
			<div class="main" >
				<div style="height:130px;overflow: auto;">
					<table id="maintable5422" border="0" class='zhzd'>
						<thead>
					       <tr class="Reprot_Head">
	        					<td width="50px"><label>No.</label></td>
						        <td width="100px"><label>Department</label></td>
						        <td width="100px"><label>Driver</label></td>
						        <td width="100px"><label>Plate Number</label></td>
						        <td width="150px"><label>Start Time</label></td>
						        <td width="150px"><label>End Time</label></td>
						        <td width="55px">Total</td>
						        <td width="55px">Score</td>
						        <s:iterator value="listDict" var="b" >
							        <td width="130px">
							        	<label>${b.dictName} </label>
							        </td>
						        </s:iterator>
	 						</tr>
						</thead>
						<c:if test="${count!=null}">
						<tr class="Reprot_td" style="font-weight : bolder; color : #990000;" onclick="getAll();">
							<td colspan="6">SUM</td>
							<s:iterator value="count" var="u" status="s">
							<td>
								<s:if test="#u!=null">
								 	<fmt:formatNumber value="${u}" pattern="#.##" />
								</s:if><s:else>0</s:else>
							</td>
							</s:iterator>
							<td></td>
						</tr>
						</c:if>
						<s:iterator value="page.result" var="u" status="s">
							<tr class="Reprot_td" onclick="getPie(this,${s.index});" 
								<c:if test="${dvo.behCont == 1}">
							    <c:if test="${100 - u[6] < 50}">style="color : #FF0000;" </c:if> 
							    </c:if> >
								 <td>
								 	${u[0]}
								 </td>
								 <td>
								 	${u[1]}
								 </td>
								 <td>
								 	${u[2]}
								 </td>
								 <td>
								 	${u[3]}
								 </td>
								 <c:if test = "${flag}">
								 	<td>
									 	${ges}&nbsp;00:00:00
								 	</td>
								 	<td>
									 	${les}&nbsp;23:59:59
								 	</td>
								 </c:if>
								 <c:if test = "${flag==false}">
								 	<td>
									 	${u[4]}&nbsp;00:00:00
								 	</td>
								 	<td>
									 	${u[4]}&nbsp;23:59:59
								 	</td>
								 </c:if>
								 <td>
									 	 <s:if test="#u[5]!=null">
								 			${u[5]}
								 		</s:if>
								 		<s:else>0</s:else>
								 	</td>
								 	<td>
									 <c:if test="${dvo.behCont==1}">
								 		<s:if test="(100-#u[6])>0">
								 			<fmt:formatNumber value="${100-u[6]}" pattern="#.##" />
								 		</s:if>
								 		<s:else>0</s:else>
								 	</c:if>
								 	<c:if test="${dvo.behCont==2}">
								 		<s:if test="(100-#u[6]/7)>0">
								 			<fmt:formatNumber value="${100-u[6]/7}" pattern="#.##" />
								 		</s:if>
								 		<s:else>0</s:else>
								 	</c:if>
								 	<c:if test="${dvo.behCont==3}">
								 		<s:if test="(100-#u[6]/30)>0">
								 			<fmt:formatNumber value="${100-u[6]/30}" pattern="#.##" />
								 		</s:if><s:else>0</s:else>
								 	</c:if>
								 	<c:if test="${dvo.behCont==4}">
								 		<s:if test="(100-#u[6]/365)>0">
								 			<fmt:formatNumber value="${100-u[6]/365}" pattern="#.##" />
								 		</s:if>
								 		<s:else>0</s:else>
								 	</c:if>
								 	</td>
								 <s:iterator value="u" id="coun" status="t"  begin="8">
							       <td>
							        <s:if test="#coun==null">0</s:if>
							        <s:else><s:property value="#coun" /></s:else>
							        <input type="hidden" id="did${s.index}" value="${u[7]}" />
							       </td>
				                </s:iterator>
							</tr>
							<input type="hidden" id="clickid" value="-1" />
						</s:iterator>
				   </table>
				</div>
				<s:hidden id="dataList" value="%{dates}"></s:hidden>
				<script type="text/javascript">
					$(function(){
						var mapDate = [];
						var distination = $("#line_chart");	//目标载体div
						//存在数据集则处理
						var datastr = $("#dataList").val();
						if(datastr != null && datastr != "" && datastr != "[]") {
							var dataList = eval($("#dataList").val());	//获得数据,并转成对象
							for (var i = 0, j = dataList.length; i < j; i++) {	//生成原始数据格式，因为图标组件要求每条线数据源为二维数组格式，所以总数据需要3维数组格式
								mapDate.push({ label: dataList[i].driver,data: dataList[i].count, color: chartColorArr[i]});	//数据 label：右上角标注
							}
						}
						
						$.plot('#pie_chart', mapDate, {
						    series: {
						        pie: { show: true }
						    },
						    legend: { show: false }
						});
					});
					
					function clickChart(typestr){
						var driid = $("#clickid").val();
						$.ajax({
			        	    type : "POST",
			        	    async : false,
			        	    dataType : 'json',
			        	    data : {
			        	    	'driid' : driid,
			        	    	'typestr':typestr
			        	    },
			        	    url : basePath + "report/redri!getDridet",
			        	    success : function(dataList) {
			        	    	$("#data_irea").html("");
			        	    	$("#tp").html(typestr);
			        	    	$("#tp").append("<img onclick='clearall();' src='"+basePath+"images/dtree/ico_cancel3.gif' title='clear' style='float:right;margin-right:20px; ' align='absmiddle' />");
		        	    		var strhtml = "<table class='zhzd' style='width:97%;border: solid 1px #d4dde7;' border='0' cellpadding='0' cellspacing='0'>";
		        	    		strhtml+="<tr class='Reprot_Head'>"
			        	    	if(1 == "${dvo.behCont}"){
			        	    		strhtml+="<td width='30%'>Start Time</td>"
			        	    		strhtml+="<td width='30%'>End Time</td>"
			        	    		strhtml+="<td width='20%'>Continuance</td>"
			        	    		strhtml+="<td width='20%'>Speed</td>"
			        	    	}else{
			        	    		strhtml+="<td width='30%'>Date</td>"
			        	    		strhtml+="<td width='20%'>Total</td>"
			        	    		strhtml+="<td width='25%'>Score</td>"
			        	    		strhtml+="<td width='25%'>Full Score</td>"
			        	    	}
		        	    		strhtml+="</tr>"
			        	    	for (var i = 0, j = dataList.length; i < j; i++) {
			        	    		strhtml+="<tr class='Reprot_td'>";
			        	    		strhtml+="<td>"+dataList[i].key+"</td>";
			        	    		strhtml+="<td>"+dataList[i].value+"</td>";
			        	    		strhtml+="<td>"+dataList[i].value1+"</td>";
			        	    		strhtml+="<td>"+dataList[i].value2+"</td>";
			        	    		strhtml+="</tr>";
			        	    	}
		        	    		strhtml+="</table>";
		        	    		$("#data_irea").append(strhtml);
			        	    },
			        	    error : function(d) {
			        			alert("Exception occurs!");
			        		}
			        	});
					}
					
					function clearall(){
						$("#data_irea").html("");
		    	    	$("#tp").html("");
					}
					
					function getAll(){
						$.ajax({
			        	    type : "POST",
			        	    async : false,
			        	    dataType : 'json',
			        	    data : {
			        	    },
			        	    url : basePath + "report/redri!getAll",
			        	    success : function(dataList) {
			        	    	$("#data_irea").html("");
			        	    	$("#tp").html("");
			        	    	var mapDate = [];
			    				var distination = $("#line_chart");	//目标载体div
			    				//存在数据集则处理
		    					for (var i = 0, j = dataList.length; i < j; i++) {	//生成原始数据格式，因为图标组件要求每条线数据源为二维数组格式，所以总数据需要3维数组格式
		    						mapDate.push({ label: dataList[i].key, data: dataList[i].value, color: chartColorArr[i]});	//数据 label：右上角标注
		    					}
			    				$.plot('#pie_chart', mapDate, {
			    				    series: {
			    				        pie: { show: true }
			    				    },
			    				    legend: { show: false }
			    				});
			    				$("#dri").html("SUM");
			        	    },
			        	    error : function(d) {
			        			alert("Exception occurs!");
			        		}
			        	});
					}
				
					function getPie(t,ind) {
						var n1 = t.cells[2].innerHTML;
						var d1 = t.cells[3].innerHTML;
						var d2 = t.cells[4].innerHTML;
						var driidstr = "#did"+ind;
						$("#clickid").val($(driidstr).val());
						var n = $("#clickid").val();
						$.ajax({
			        	    type : "POST",
			        	    async : false,
			        	    dataType : 'json',
			        	    data : {
			        	    	'driname' : n,
			        	    	'ajaxd1' : d1,
			        	    	'ajaxd2' : d2
			        	    },
			        	    url : basePath + "report/redri!getPie",
			        	    success : function(dataList) {
			        	    	$("#data_irea").html("");
			        	    	$("#tp").html("");
			        	    	var mapDate = [];
			    				var distination = $("#line_chart");	//目标载体div
			    				//存在数据集则处理
		    					for (var i = 0, j = dataList.length; i < j; i++) {	//生成原始数据格式，因为图标组件要求每条线数据源为二维数组格式，所以总数据需要3维数组格式
		    						mapDate.push({ label: dataList[i].key, data: dataList[i].value, color: chartColorArr[i]});	//数据 label：右上角标注
		    					}
			    				$.plot('#pie_chart', mapDate, {
			    				    series: {
			    				        pie: { show: true }
			    				    },
			    				    legend: { show: false },
			    				    grid: {
			    						hoverable: true,
			    						clickable: true
			    					}
			    				});
			    				$("#pie_chart").bind("plothover", function(event, pos, obj) {
			    					if (!obj) {
			    						return;
			    					}
			    					percent = parseFloat(obj.series.percent).toFixed(2);
			    					clickChart(obj.series.label);
			    				});
			    				$("#dri").html(n1);
			        	    },
			        	    error : function(d) {
			        			alert("Exception occurs!");
			        		}
			        	});
					}
				</script>
			</div> 
			<div class="page">
	 				<s:include value="/common/page.jsp">
					<s:param name="pageName" value="'page'"/>
					<s:param name="formName" value="'queryResult'"/>
					<s:param name="linkCount" value="5"/>
				</s:include>
			</div>
			<div id="dri" style="width:45%;height: 25px;text-align: right;font-weight:bold;font-size: 15px;float: left;">
				<c:if test="${count!=null}">
					SUM
				</c:if>
			</div>
			<div id="tp" style="width:33%;height: 25px;text-align: left;font-weight:bold;font-size: 13px;float: right;line-height:25px"></div>
			<div id="pie_chart" style="width:50%;float: left;"></div>
			<div id="data_irea" style="width:48%;float: left;overflow:auto;"></div>
		</form>
	</body>
</html>