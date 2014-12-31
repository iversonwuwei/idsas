<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
<script type="text/JavaScript">
   function delete1(id){
	   if (confirm("Sure you want to reply to power off the oil?")) {
		    document.forms['form1'].action='<%=basePath%>/analy/spe-instruction!recover?id='+id;
	        document.forms['form1'].submit();
	   }
  }
   
</script>
	<body>
		<custom:navigation father="Fault Analyzer" model="Special Instruction" addPath="analy/spe-instruction" addMethod="add" />
		<form name="queryResult" id="form1" action="<%=basePath%>analy/spe-instruction!index" method="post">
			<custom:searchBody row="2">
				<custom:searchItem title="Plate Number" side="first">
					<input type="text" name="csVo.uname" id="csVo.uname" value="${csVo.uname}" maxlength="25" style="width:140px"/>
				</custom:searchItem>
				<custom:searchItem title="Definition">
					  <s:select id="code" name="csVo.code" list="listfa" listKey="key" listValue="value"    headerKey="" headerValue="All" theme="simple"   cssStyle="width:150px;"  /> 
				</custom:searchItem>
				<custom:searchItem title="Creator" side="first" >
					<input type="text" name="csVo.username" id="csVo.username" value="${csVo.username}" maxlength="25" style="width:140px"/>
				</custom:searchItem>
				<custom:searchItem title="Create Time" >
					<label class="input_date_l">
						<input type="text" style="width: 140px;" name="csVo.timeMin" value="${csVo.timeMin}" id="startMin" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:&quot;#F{$dp.$D('startMax')}&quot;, readOnly:true});this.blur()" /></label><label class="input_date_r"><input type="text" name="csVo.timeMax" value="${csVo.timeMax}" id="startMax" style="width: 140px;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:&quot;#F{$dp.$D('startMin')}&quot;, readOnly:true});this.blur();"/>
					</label>
				</custom:searchItem>
				<custom:searchItem side="last" width="20%">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true">
					<custom:sort property="licenseplate" page="${page}" width="20%">Plate Number</custom:sort>
					<custom:sort property="engdefinition" page="${page}" width="20%">Definition</custom:sort>
					<custom:sort property="creater" page="${page}" width="10%">Creator</custom:sort>
					<custom:sort property="creatime" page="${page}" width="20%">Create Time</custom:sort>
					<custom:sort property="success" page="${page}" width="20%">Success Or Failure</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="specialid" index="i" var="u">
					 <custom:tableItem>${u.licenseplate}</custom:tableItem>
					 <custom:tableItem>${u.engdefinition}</custom:tableItem>
					 <custom:tableItem>${u.creater}</custom:tableItem>
					 <custom:tableItem>${u.creatime}</custom:tableItem>
					 <custom:tableItem>
					 <c:if test="${'L' == u.success }">${u.engdefinition} success</c:if>
					 <c:if test="${'F' == u.success }">${u.engdefinition} success</c:if>
					 <c:if test="${'T' == u.success }">${u.engdefinition} failure</c:if>
					 <c:if test="${'K' == u.success }">${u.engdefinition} success</c:if>
					 <c:if test="${'P' == u.success }">${u.engdefinition} failure</c:if>
					 </custom:tableItem>
					<custom:tableItem>
					   <c:if test="${'F' == u.success }">
					    <a href="#" onclick="delete1(${u.specialid})"><img src="<%=basePath%>/images/cut.jpg"/></a>
					    </c:if>
						<custom:show path="analy/spe-instruction" id="${u.specialid}"/>
					</custom:tableItem>
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