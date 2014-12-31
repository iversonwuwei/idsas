<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.orderBars.js"></script>
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
		    	 if(0 < "${fn:length(page.result)}"){
			        	var mysuperTable = new superTable("maintable5422",{headerRows :1,fixedCols : 0});
			        }
		    	 
		    	 if(6< "${fn:length(page.result)}"){
		    		 var count="${fn:length(page.result)}";
		    		 $("#pie_chart").width($("#pie_all").width()+150*(count-6));
		    		 $("#pie_chart").height($("#pie_chart").height()-18);
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
	    		- (null == $(".page").height() ? 0 :  $(".page").height())-150;
	    		$(".main").height("130px");
	    		$("#pie_chart").height(h+"px");
	    		tWidth = tWidth.toString() + "px";
	    		$("#" + tableID).width(tWidth);	//设置宽度
	    	}
		     
		    function excel(){
			    if(0 == "${fn:length(page.result)}"){
		    	   alert("No data to export!");
		    	   return;
			    }
			    document.forms['kindForm'].action='<%=basePath%>report/dri-contrast!exportDetail';
		        document.forms['kindForm'].submit();
		        document.forms['kindForm'].action='<%=basePath%>report/dri-contrast!index';
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
		<custom:navigation father="Driver Report" model="Driver Violation Compare" excelMethod="excel()"/>
		<form id="kindForm" name="queryResult" action="<%=basePath%>report/dri-contrast!index" method="post" >
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
			<div class="main" style="height:130px;">
			<div style="height:130px;overflow: auto;">
				<table id="maintable5422" border="0" class='zhzd'>
					<thead>
				       <tr class="Reprot_Head">
        					<td width="50px"><label>No.</label></td>
					        <td width="100px"><label>Department</label></td>
					        <td width="100px"><label>Driver</label></td>
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
					<tr class="Reprot_td" style="font-weight : bolder; color : #990000;">
						<td colspan="5">SUM</td>
						<s:iterator value="count" var="u" status="s">
						<td>
							<s:if test="#u!=null">
							 	<fmt:formatNumber value="${u}" pattern="#.##" />
							</s:if><s:else>0</s:else>
						</td>
						</s:iterator>
					</tr>
					</c:if>
					<s:iterator value="page.result" var="u" status="s">
						<tr class="Reprot_td" <c:if test="${dvo.behCont==1}"><c:if test="${ 100-u[5] < 50 }">style="color : #FF0000;" </c:if></c:if>>
							 <td>
							 	${u[0]}
							 </td>
							 <td>
							 	${u[1]}
							 </td>
							 <td>
							 	${u[2]}
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
							 			<fmt:formatNumber value="${100-u[5]/7}" pattern="#.##" />
							 		</s:if><s:else>0</s:else>
							 	</c:if>
							 	<c:if test="${dvo.behCont==3}">
							 		<s:if test="(100-#u[5]/30)>0">
							 			<fmt:formatNumber value="${100-u[5]/30}" pattern="#.##" />
							 		</s:if><s:else>0</s:else>
							 	</c:if>
							 	<c:if test="${dvo.behCont==4}">
							 		<s:if test="(100-#u[5]/365)>0">
							 			<fmt:formatNumber value="${100-u[5]/365}" pattern="#.##" />
							 		</s:if>
							 		<s:else>0</s:else>
							 	</c:if>
							 	</td>
							 <s:iterator value="u" id="coun" status="t"  begin="6">
						       <td>
						        <s:if test="#coun==null">0</s:if>
						        <s:else><s:property value="#coun" /></s:else>
						       </td>
			                </s:iterator>
						</tr>
					</s:iterator>
			   </table>
			</div>
			<s:hidden id="dataList" value="%{dates}"></s:hidden>
			<script type="text/javascript">
			function check1(str){
				 
				var strsb;
				if(null == str){
					strsb="";
				} else {
					strsb = str;
				}
				return strsb;
			}
			
			$(function(){
				var tooltip = [];
				var mapDate1 = [];
				var mapDate2 = [];
				var mapDate3 = [];
				var mapDate4 = [];
				var mapDate5 = [];
				var mapDate6 = [];
				var mapDate7 = [];
				var mapDate8 = [];
				var mapDate9 = [];
				var ticks = [];
				var distination = $("#pie_chart");	//目标载体div
				//存在数据集则处理
				if($("#dataList").val() != null && $("#dataList").val() != "" && $("#dataList").val() != "[]") {
					var dataList = eval($("#dataList").val());	//获得数据,并转成对象
					for (var i = 0, j = dataList.length; i < j; i++) {	//生成原始数据格式，因为图标组件要求每条线数据源为二维数组格式，所以总数据需要3维数组格式
						if(dataList[i].count==''||dataList[i].count==null||dataList[i].count=='null'){
							mapDate1[i] = [(i+1), 0];
							mapDate2[i] = [(i+1), 0];
							mapDate3[i] = [(i+1), 0];
							mapDate4[i] = [(i+1), 0];
							mapDate5[i] = [(i+1), 0];
							mapDate6[i] = [(i+1), 0];
							mapDate7[i] = [(i+1), 0];
							mapDate8[i] = [(i+1), 0];
							mapDate9[i] = [(i+1), 0];
							if('null' != dataList[i].driver){
								ticks[i] = [(i+1),dataList[i].driver];
							} else {
								ticks[i] = [(i+1),''];
							}
							
						}else{
							var date1 = dataList[i].count.split(",");
						
							mapDate1[i] = [(i+1),date1[0]];
							mapDate2[i] = [(i+1),date1[1]];
							mapDate3[i] = [(i+1),date1[2]];
							mapDate4[i] = [(i+1),date1[3]];
							mapDate5[i] = [(i+1),date1[4]];
							mapDate6[i] = [(i+1),date1[5]];
							mapDate7[i] = [(i+1),date1[6]];
							mapDate8[i] = [(i+1),date1[7]];
							mapDate9[i] = [(i+1),date1[8]];
							if('null' != dataList[i].driver){
								ticks[i] = [(i+1),dataList[i].driver];
							} else {
								ticks[i] = [(i+1),''];
							}
						}
					}
				}
				var datas1 = {
						label: "Sudden Acceleration",
						data:mapDate1,
                    	bars : {
                    		order:1
						}
				};
				tooltip[0] = "Sudden Acceleration";
				var datas2 = {
						label: "Sudden Braking",
						data:mapDate2,
                    	bars : {
                    		order:2
						}
				};
				tooltip[1] = "Sudden Braking";
				var datas3 = {
						label: "Sudden Left",
						data:mapDate3,
                    	bars : {
                    		order:3
						}
				};
				tooltip[2] = "Sudden Left";
				var datas4 = {
						label: "Sudden Right",
						data:mapDate4,
                    	bars : {
                    		order:4
						}
				};
				tooltip[3] = "Sudden Right";
				var datas5 = {
						label: "Speeding",
						data:mapDate5,
                    	bars : {
                    		order:5
						}
				};
				tooltip[4] = "Speeding";
				var datas6 = {
						label: "Neutral Slide",
						data:mapDate6,
                    	bars : {
                    		order:6
						}
				};
				tooltip[5] = "Neutral Slide";
				var datas7 = {
						label: "Idle",
						data:mapDate7,
                    	bars : {
                    		order:7
						}
				};
				tooltip[6] = "Idle";
				var datas8 = {
						label: "Idle Air Conditioning",
						data:mapDate8,
                    	bars : {
                    		order:8
						}
				};
				tooltip[7] = "Idle Air Conditioning";
				var datas9 = {
						label: "Engine Overspeed",
						data:mapDate9,
                    	bars : {
                    		order:9
						}
				};
				tooltip[8] = "Engine Overspeed";
				//选项设置
			    var options = {
		    		series: {
						bars: {
							show: true,
							fill: 1,
							barWidth:0.05
						}
					},
			        xaxis: { //横轴设置
			        	tickSize:1,
			        	ticks:ticks,
			        	tickLength: 0,
			        	autoscaleMargin:0.02,	
			        },
			        colors: chartColorArr,
			        grid: {// 开启鼠标在上面的效果和事件绑定
						hoverable: true
					},
					multiplebars: false,
					zoom: {
						interactive: true
					},
					pan: {
						interactive: true
					}

			    };
			    
			    var plot = $.plot(distination, [ datas1,  datas2,  datas3,  datas4,  datas5, datas6, datas7, datas8, datas9 ], options);		//生成图形
			    var previousPoint = null;
			  	//绑定mouseover事件
			  	// item有这个属性item.dataIndex，需求变了的话，也许会用到
				$("#pie_chart").bind("plothover", function(event, pos, item) {
					if (item) {
						if (previousPoint != item.dataIndex) {
							previousPoint = item.dataIndex;
							$("#tooltip").remove();
							var x = item.datapoint[0].toFixed(2), y = item.datapoint[1].toFixed(0);
							showTooltip(item.pageX, item.pageY, 'Violation: ' + tooltip[item.series.bars.order - 1] + '<br/>times: ' + y);
						}
					} else {
						$("#tooltip").remove();
						previousPoint = null;
					}
				});
			});
			</script>
			</div> 
			
			<div id="pie_all" style="width:98%;overflow: auto;">
			<div id="pie_chart" style="">
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