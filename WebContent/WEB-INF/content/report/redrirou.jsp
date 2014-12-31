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
		    		var tWidth = (630 + eventCount *130);	//计算总宽度245位前面固定列宽
		    		if(tWidth < $(".main").width()) {
		    			tWidth = $(".main").width() * 0.98;	//最小宽度位父div宽度
		    		}
		    		tWidth = tWidth.toString() + "px";
		    		$("#" + tableID).width(tWidth);	//设置宽度
		    	}
		    function excel(){
			    if(0 == "${fn:length(page.result)}"){
		    	   alert("No derived data!");
		    	   return;
			    }
			    document.forms['kindForm'].action='<%=basePath%>report/redrirou!exportDetail';
		        document.forms['kindForm'].submit();
		        document.forms['kindForm'].action='<%=basePath%>report/redrirou!index';
		     }
		    function doSearch() {
	    		$("#departmentName").val($("#orgId").find("option:selected").text());
		    	beforeSubmit();
		    	document.queryResult.submit();
		    }
	    </script>
    </head>
	<body>
		<custom:navigation father="Chart Report" model="Report Driver By Route" excelMethod="excel()" />
		<form id="kindForm" name="queryResult" action="<%=basePath%>report/redrirou!index" method="post">
			 <s:hidden id="departmentName" name="dvo.orgName" value="%{dvo.orgName}" />
			 <custom:searchBody>
				<custom:searchItem title="Route" side="first">
					<s:select id="orgId" name="dvo.orgId" list="rouList" listKey="key" listValue="value" headerKey="-1" headerValue="All" theme="simple" />
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
					        <td width="70px"><label>Driver</label></td>
					        <td width="100px"><label>Route</label></td>
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
						<tr class="Reprot_td" <c:if test="${dvo.behCont == 1}"><c:if test="${100 - u[5] < 50}">style="color : #FF0000;" </c:if></c:if>>
							 <s:iterator value="u" id="coun" status="t" begin="0" end="2">
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
								 	${u[3]}&nbsp;00:00:00
							 	</td>
							 	<td>
								 	${u[3]}&nbsp;23:59:59
							 	</td>
							 </c:if>
							 	<td>
								 	 <s:if test="#u[4]!=null">
							 			${u[4]}
							 		</s:if>
							 		<s:else>0</s:else>
							 	</td>
							 	<td>
								 <c:if test="${dvo.behCont==1}">
							 		<s:if test="(100-#u[5])>0">
							 		<fmt:formatNumber value="${100-u[5]}" pattern="#.##" />
							 		</s:if><s:else>0</s:else>
							 	</c:if>
							 	<c:if test="${dvo.behCont==2}">
							 		<s:if test="(100-#u[5]/7)>0">
							 			<fmt:formatNumber value="${100-u[5]*1.00001/7}" pattern="#.##" />
							 		</s:if><s:else>0</s:else>
							 	</c:if>
							 	<c:if test="${dvo.behCont==3}">
							 		<s:if test="(100-#u[5]/30)>0">
							 			<fmt:formatNumber value="${100-u[5]*1.00001/30}" pattern="#.##" />
							 		</s:if><s:else>0</s:else>
							 	</c:if>
							 	<c:if test="${dvo.behCont==4}">
							 		<s:if test="(100-#u[5]/365)>0">
							 			<fmt:formatNumber value="${100-u[5]*1.00001/365}" pattern="#.##" />
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
			<s:hidden id="dataList" value="%{dates}"/>
			<script type="text/javascript">
			$(function(){
				var mapDate = [];
				var distination = $("#line_chart");	//目标载体div
				//存在数据集则处理
				if($("#dataList").val() != null && $("#dataList").val() != "" && $("#dataList").val() != "[]") {
					var dataList = eval($("#dataList").val());	//获得数据,并转成对象
					for (var i = 0, j = dataList.length; i < j; i++) {
						if(dataList[i].count==''||dataList[i].count==null||dataList[i].count=='null'){
							mapDate[i] = [dataList[i].driver, 0];
						}else{
							mapDate[i] = [dataList[i].driver, dataList[i].count];
						}
					}
				}

				//选项设置
			    var options = {
			        series: { 
			        	lines: { show: true, align: "center" },
			            points: { show: true,radius: 2}
			        },	//图形样式
			        xaxis: { //横轴设置
			        	mode: "categories",
			        	tickLength: 0
			        },
			        yaxis: { //纵轴设置
			        	min: 0,
			        	tickSize: 10,
			        	max: 110
			        },
			        colors: chartColorArr,
			        grid: { hoverable: true }
			    };
			    var plot = $.plot(distination, [mapDate], options);		//生成图形
			    var previousPoint = null;
				distination.bind("plothover", function (event, pos, item) {
				    if (item) {
				        if (previousPoint != item.dataIndex) {
				            previousPoint = item.dataIndex;
				            $("#tooltip").remove();
				            var y = item.datapoint[1];
				            //标点详细信息
				            var details =  'Score: ' + y;
				            showTooltip(item.pageX, item.pageY, details);
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
				.text("Driver")
				.appendTo(container);
			});
			function showTooltip(x, y, contents) {	
				   $('<div id="tooltip">' + contents + '</div>').css( {
				       position: 'absolute',
				       display: 'none',
				       top: y + 5,
				       left: x + 5,
				       border: '1px solid #fdd',
				       padding: '2px',
				       'background-color': '#fee',
				       opacity: 0.80
				   }).appendTo("body").fadeIn(200);
				}
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
		</form>
	</body>
</html>