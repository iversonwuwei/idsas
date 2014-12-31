<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/flot.css" />
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/excanvas.min.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.min.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.resize.min.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.categories.min.js"></script>
	    <script type="text/JavaScript">
		    function timeTypeChange(t) {
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
				if(0 < "${fn:length(page.result)}") {
					var mysuperTable = new superTable("maintable5422", {headerRows :1, fixedCols : 0});
				}
		     });
		     
		     function initTableWidth(tableID) {
	    		var eventCount = "${fn:length(listDict)}";	//事件个数
	    		var tWidth = (505 + eventCount *130);	//计算总宽度245位前面固定列宽
	    		if(tWidth < $(".main").width()) {
	    			tWidth = $(".main").width() * 0.98;	//最小宽度位父div宽度
	    		}
	    		tWidth = tWidth.toString() + "px";
	    		$("#" + tableID).width(tWidth);	//设置宽度
	    	}
		     
		    function excel() {
			    if(0 == "${fn:length(page.result)}"){
		    	   alert("No data to export!");
		    	   return;
			    }
			    document.forms['kindForm'].action='<%=basePath%>report/redep!exportDetail';
		        document.forms['kindForm'].submit();
		        document.forms['kindForm'].action='<%=basePath%>report/redep!index';
		     }
		    
		    function doSearch() {
		    	if(select_company.getSelectedValue() == '-1' || select_company.getSelectedValue() == '') {
			    	$('#departmentName').val('');
			    } else {
				    $('#departmentName').val(select_company.getComboText());
			    }
		    	beforeSubmit();
		    	document.queryResult.submit();
		    }
	    </script>
    </head>
	<body>
		<custom:navigation father="Department Report" model="Department Average Score" excelMethod="excel()" />
		<form id="kindForm" name="queryResult" action="<%=basePath%>report/redep!index" method="post">
			 <s:hidden id="departmentName" name="dvo.orgName" value="%{dvo.orgName}" />
			 <custom:searchBody>
				<custom:searchItem title="Department" side="first">
					<custom:selCom name="dvo.orgId" value="${dvo.orgId}" />
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
					<s:iterator value="page.result" var="u" status="s">
						<tr class="Reprot_td" <c:if test="${dvo.behCont == 1}"><c:if test="${100-u[4]*1.00001/u[5] < 50}">style="color : #FF0000;" </c:if></c:if>>
							 <s:iterator value="u" id="coun" status="t" begin="0" end="1">
							 <td>
							 <s:property value="#coun" />
							 </td>
							 </s:iterator>
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
								 	${u[2]}&nbsp;00:00:00
							 	</td>
							 	<td>
								 	${u[2]}&nbsp;23:59:59
							 	</td>
							 </c:if>
							 	<td>
								 	 <s:if test="#u[3]!=null">
							 			${u[3]}
							 		</s:if>
							 		<s:else>0</s:else>
							 	</td>
							 	<td>
							 		<c:if test="${dvo.behCont==1}">
							 		<s:if test="(100-#u[4]/#u[5])>0">
							 		<fmt:formatNumber value="${100-u[4]*1.00001/u[5]}" pattern="##.##" />
							 		</s:if><s:else>0</s:else>
							 	</c:if>
							 	<c:if test="${dvo.behCont==2}">
							 		<s:if test="(100-#u[4]/7/#u[5])>0">
							 			<fmt:formatNumber value="${100-u[4]*1.00001/7/u[5]}" pattern="##.##" />
							 		</s:if><s:else>0</s:else>
							 	</c:if>
							 	<c:if test="${dvo.behCont==3}">
							 		<s:if test="(100-#u[4]/30/#u[5])>0">
							 			<fmt:formatNumber value="${100-u[4]*1.00001/30/u[5]}" pattern="##.##" />
							 		</s:if><s:else>0</s:else>
							 	</c:if>
							 	<c:if test="${dvo.behCont==4}">
							 		<s:if test="(100-#u[4]/365/#u[5])>0">
							 			<fmt:formatNumber value="${100-u[4]*1.00001/365/u[5]}" pattern="##.##" />
							 		</s:if>
							 		<s:else>0</s:else>
							 	</c:if>
							 	</td>
							 <s:iterator value="u" id="coun1" status="t"  begin="6">
						       <td >  
						        <s:if test="#coun1==null">0</s:if>
						        <s:else><s:property value="#coun1" /></s:else>
						       </td>
			                </s:iterator>
						</tr>
					</s:iterator>
			   </table>
			</div>
			<s:hidden id="dataList" value="%{dates}"></s:hidden>
			<script type="text/javascript">
				$(function(){
					var mapDate = [];
					var distination = $("#line_chart");	//目标载体div
					//存在数据集则处理
					if($("#dataList").val() != null && $("#dataList").val() != "" && $("#dataList").val() != "[]") {
						var dataList = eval($("#dataList").val());	//获得数据,并转成对象
						for (var i = 0, j = dataList.length; i < j; i++) {	//生成原始数据格式，因为图标组件要求每条线数据源为二维数组格式，所以总数据需要3维数组格式
							if(dataList[i].count == '' || dataList[i].count == null|| dataList[i].count == 'null'){
								mapDate[i] = [dataList[i].driver, 0];
							}else{
								mapDate[i] = [dataList[i].driver, dataList[i].count];
							}
						}
					}
					//选项设置
				    var options = {
				        series: { 
				            bars: { show: true, barWidth: 0.5, fill: 1, align: "center" }
				        },	//图形样式
				        xaxis: { //横轴设置
				        	mode: "categories",
				        	autoscaleMargin:0.1,
				        	tickLength: 0
				        },
				        yaxis: { //纵轴设置
				        	min: 0,
				        	tickSize: 10,
				        	max: 110
				        },
				        colors: chartColorArr,
				        grid: {// 开启鼠标在上面的效果和事件绑定
							hoverable: true
						}
				    };
				    
				    var plot = $.plot(distination, [mapDate], options);		//生成图形
				    var previousPoint = null;
				  	//绑定mouseover事件
				  	// item有这个属性item.dataIndex，需求变了的话，也许会用到
					$("#line_chart").bind("plothover", function(event, pos, item) {
						if (item) {
							if (previousPoint != item.dataIndex) {
								previousPoint = item.dataIndex;
								$("#tooltip").remove();
								var x = item.datapoint[0].toFixed(2), y = item.datapoint[1].toFixed(2);
								showTooltip(item.pageX, item.pageY, 'Score: ' + y);
							}
						} else {
							$("#tooltip").remove();
							previousPoint = null;
						}
					});
					var container = $("#line_chart");
					var yaxisLabel = $("<div class='axisLabel yaxisLabel'></div>")
					.text("Score")
					.appendTo(container);
					var axisLabel = $("<div class='axisLabel xaxisLabel'></div>")
					.text("Department")
					.appendTo(container);
				  	
				  	
				});
			</script>
			<div id="line_chart" style="width:98%; height:70%; min-height:300px;margin: auto 10px 30px 30px;">
			</div>
			</div> 
			<div class="page">
  				<s:include value="/common/page.jsp">
					<s:param name="pageName" value="'page'"/>
					<s:param name="formName" value="'queryResult'"/>
					<s:param name="linkCount" value="5"/>
				</s:include>
			</div>
			<span style="position: absolute;border: 1px solid black;padding: 2px;display: none;" class="chart_title"></span>
		</form>
	</body>
</html>