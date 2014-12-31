<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
	    <script type="text/JavaScript">
	    function timeTypeChange(t){
	    	$("#date1,#date2,#date3,#date4").hide().val("");//隐藏那四个日期框
	    	$("#date"+t).show();
	    }
		     $(document).ready(function() {
		    	 $("#behCont").change();// 初始页面触发日期类型的change事件
		 		if (1 == "${rdvo.behCont}") {// 然后赋值
		 			$("#date1").val("${rdvo.date1}")
		 		}
		 		if (2 == "${rdvo.behCont}") {
		 			$("#date2").val("${rdvo.date2}")
		 		}
		 		if (3 == "${rdvo.behCont}") {
		 			$("#date3").val("${rdvo.date3}")
		 		}
		 		if (4 == "${rdvo.behCont}") {
		 			$("#date4").val("${rdvo.date4}")
		 		}
		    	initTableWidth('maintable5422');
		        if(0 < "${fn:length(page.result)}"){
		        	var mysuperTable = new superTable("maintable5422",{headerRows :2,fixedCols : 6});
		        }
		     });
		     function initTableWidth(tableID) {
		    		var eventCount = "${fn:length(listDict)}";	//事件个数
		    		var tWidth = (410 + eventCount * 125);	//计算总宽度245位前面固定列宽
		    		if(tWidth < $(".main").width()) {
		    			tWidth = $(".main").width() * 0.98;	//最小宽度位父div宽度
		    		}
		    		tWidth = tWidth.toString() + "px";
		    		$("#" + tableID).width(tWidth);	//设置宽度
		    	}
		    function excel(){
			    if(0 == "${fn:length(page.result)}"){
		    	   alert("无导出数据！");
		    	   return;
			    }
			    document.forms['kindForm'].action='<%=basePath%>query/rep-daily!exportDetail';
		        document.forms['kindForm'].submit();
		        document.forms['kindForm'].action='<%=basePath%>query/rep-daily!index';
		     }
	    </script>
    </head>
	<body>
		<custom:navigation father="Data Query" model="Report Query" excelMethod="excel()"/>
		<form id="kindForm" name="queryResult" action="<%=basePath%>query/rep-daily!index"  method="post">
			 <custom:searchBody>
				<custom:searchItem title="Driver" side="first">
					<custom:selDriver name="rdvo.orgId" value="${rdvo.orgId}" />
				</custom:searchItem>
				<custom:searchItem title="Stat Type" >
					<s:select list="#{1:'Daily',2:'Weekly',3:'Monthly',4:'Yearly'}" id="behCont" name="rdvo.behCont" theme="simple" onchange="timeTypeChange(this.value)"/>
				</custom:searchItem>
				<custom:searchItem title="Date">
					<custom:date dateName="rdvo.date1" dateValue="${rdvo.date1}" id="date1" />
					<custom:date dateName="rdvo.date2" dateValue="${rdvo.date2}" id="date2" isShowWeek="true" />
					<custom:date dateName="rdvo.date3" dateValue="${rdvo.date3}" id="date3" dateFmt="yyyy-MM" />
					<custom:date dateName="rdvo.date4" dateValue="${rdvo.date4}" id="date4" dateFmt="yyyy" />
			</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="sub();" />
				</custom:searchItem>
			</custom:searchBody>
			<div class="main">
				<table id="maintable5422" border="0" class='zhzd'>
					<thead>
				       <tr class="Reprot_Head">
        					<td rowspan="2" width="50px"><label>No.</label></td>
					        <td rowspan="2" width="100px"><label>Driver</label></td>
					        <td rowspan="2" width="150px"><label>Start Time</label></td>
					        <td rowspan="2" width="150px"><label>End Time</label></td>
					        <td rowspan="2" width="55px">Total</td>
					        <td rowspan="2" width="55px">Score</td>
					        <s:iterator value="listDict" var="b" >
						        <td  
						        	<c:if test="${!fn:startsWith(b.dictName,'Sudden')}">colspan="2"</c:if>
						        	<c:if test="${fn:startsWith(b.dictName,'Sudden')}">rowspan="2" width="100px"</c:if>
						        >
						        	<label>
						        	<c:if test="${fn:startsWith(b.dictName,'Sudden')}">
						        	${fn:substring(b.dictName,0,6)}<br>
						        	${fn:replace(b.dictName,'Sudden','')}
						        	</c:if>
						        	<c:if test="${!fn:startsWith(b.dictName,'Sudden')}">
						        	${b.dictName}
						        	</c:if>
						        	</label>
						        </td>
					        </s:iterator>
 						</tr>
						<tr class="Reprot_Head">
						  <s:iterator value="listDict" var="b" >
							<c:if test="${!fn:startsWith(b.dictName,'Sudden')}">
							<td width="45px"><label>times</label>
							</td>
							<td width="80px"><label>Cont</label>
							</td>
							</c:if>
						  </s:iterator>
						</tr>
					</thead>
					<tr class="Reprot_td" style="font-weight : bolder; color : #990000;" >
				  <c:if test="${0 != fn:length(page.result)}"><td colspan="4">SUM</td></c:if>
						
						<s:iterator value="count" var="u" status="s">
						<td>
							<c:if test="${fn:startsWith(u,'t')}">
						        <custom:formatTime value="${fn:replace(u,'t','')}"></custom:formatTime>
						       </c:if>
						      <c:if test="${!fn:startsWith(u,'t')}">
						      <s:if test="#u!=null">
							 			<fmt:formatNumber value="${u}" pattern="#.##" />
							 		</s:if>
							 		<s:else>0</s:else>
						     </c:if>
						     
						</td>
						</s:iterator>
					</tr>
					<s:iterator value="page.result" var="u" status="s">
						<tr class="Reprot_td">
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
							 			<fmt:formatNumber value="${u[3]}" pattern="#.##" />
							 		</s:if>
							 		<s:else>0</s:else>
							 	</td>
							 	<td>
							 	<c:if test="${rdvo.behCont==1}">
							 		<s:if test="(100-#u[4])>0">
							 		<fmt:formatNumber value="${100-u[4]}" pattern="#.##" />
							 		</s:if><s:else>0</s:else>
							 	</c:if>
							 	<c:if test="${rdvo.behCont==2}">
							 		<s:if test="(100-#u[4]/7)>0">
							 			<fmt:formatNumber value="${100-u[4]*1.00001/7}" pattern="#.##" />
							 		</s:if><s:else>0</s:else>
							 	</c:if>
							 	<c:if test="${rdvo.behCont==3}">
							 		<s:if test="(100-#u[4]/30)>0">
							 			<fmt:formatNumber value="${100-u[4]*1.00001/30}" pattern="#.##" />
							 		</s:if><s:else>0</s:else>
							 	</c:if>
							 	<c:if test="${rdvo.behCont==4}">
							 		<s:if test="(100-#u[4]/365)>0">
							 			<fmt:formatNumber value="${100-u[4]*1.00001/365}" pattern="#.##" />
							 		</s:if>
							 		<s:else>0</s:else>
							 	</c:if>
							 	</td>
							 <s:iterator value="u" id="coun1" status="t" begin="5">
						       <td >
						       <c:if test="${fn:startsWith(coun1,'t')}">
						        <custom:formatTime value="${fn:replace(coun1,'t','')}"></custom:formatTime>
						       </c:if>
						      <c:if test="${!fn:startsWith(coun1,'t')}">
						       <c:if test="${ coun1=='d'}">0</c:if>
						       <c:if test="${coun1!='d'}">${fn:replace(coun1,'d','')}</c:if>
						     </c:if>
						       </td>
			                </s:iterator>
						</tr>
					</s:iterator>
			   </table>
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