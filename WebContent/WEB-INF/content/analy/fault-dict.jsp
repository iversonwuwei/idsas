<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<body>
		<custom:navigation father="Fault Analyzer" model="Fault Dictionary" addPath="analy/fault-dict" addMethod="add" />
		<form name="queryResult" action="<%=basePath%>analy/fault-dict!index" method="post">
			<custom:searchBody row="1">
				<custom:searchItem title="Code" side="first">
					<input type="text" name="csVo.uname" id="csVo.uname" value="${csVo.uname}" maxlength="25" style="width:140px"/>
				</custom:searchItem>
				<custom:searchItem title="Scope">
					<input type="text" name="csVo.dname" id="csVo.dname" value="${csVo.dname}" maxlength="25" style="width:140px"/>
				</custom:searchItem>
				<custom:searchItem title="Definition">
					<input type="text" name="csVo.code" id="csVo.code" value="${csVo.code}" maxlength="25" style="width:140px"/>
				</custom:searchItem>
				<custom:searchItem side="last" width="20%">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true" width="2%">
					<custom:sort property="code" page="${page}" width="7%">Code</custom:sort>
					<custom:sort property="engscope" page="${page}" width="20%">Scope</custom:sort>
					<custom:sort property="engdefinition" page="${page}" width="20%">Definition</custom:sort>
					<custom:sort property="engcontext" page="${page}" width="20%">Context</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="faultcodeid" index="i" var="u">
					<custom:tableItem>${u.code}</custom:tableItem>
					<custom:tableItem>
						<c:if test="${u.engscope.length()>10}">
							${fn:substring(u.engscope, 0, 10)}...
						</c:if>
						<c:if test="${u.engscope.length()<11}">
							${u.engscope}
						</c:if>
					</custom:tableItem>
					<custom:tableItem>
						<c:if test="${u.engdefinition.length()>10}">
							${fn:substring(u.engdefinition, 0, 10)}...
						</c:if>
						<c:if test="${u.engdefinition.length()<11}">
							${u.engdefinition}
						</c:if>
					</custom:tableItem>
					<custom:tableItem>
						<c:if test="${u.engcontext.length()>10}">
							${fn:substring(u.engcontext, 0, 10)}...
						</c:if>
						<c:if test="${u.engcontext.length()<11}">
							${u.engcontext}
						</c:if>
					</custom:tableItem>
					<custom:tableItem>
						<custom:show path="analy/fault-dict" id="${u.faultcodeid}"/>
						<custom:edit path="analy/fault-dict" id="${u.faultcodeid}"/>
						<custom:del path="analy/fault-dict" id="${u.faultcodeid}"/>
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