<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<body>
		<custom:navigation father="Task Management" model="Counselling List" addPath="task/counsell" addMethod="add" />
		<form name="queryResult" action="<%=basePath%>task/counsell!index" method="post">
			<custom:searchBody row="2">
				<custom:searchItem title="User" side="first">
					<input type="text" name="csVo.uname" id="csVo.uname" value="${csVo.uname}" maxlength="25" style="width:140px"/>
				</custom:searchItem>
				<custom:searchItem title="Department">
					<custom:selCom name="csVo.uid" value="${csVo.uid }"/>
				</custom:searchItem>
				<custom:searchItem title="Driver" >
					<custom:selDriver name="csVo.derid" value="${csVo.derid}" />
				</custom:searchItem>
				</tr>
				<tr>
				<td class='text_right'>
					Time:
				</td>
				<td colspan="2">
					<label class="input_date_l"><input id="csVo.starttime" name="csVo.starttime" value="${csVo.starttime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm', maxDate:'#F{$dp.$D(\'csVo.endtime\')||\'%y-%M-%d %H:%m\'}'})" style="width: 135px"/></label><label class="input_date_r"><input id="csVo.endtime" name="csVo.endtime" value="${csVo.endtime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm', maxDate:'%y-%M-%d %H:%m',minDate:'#F{$dp.$D(\'csVo.starttime\')}'})" style="width: 135px"/></label>
				</td>
				<custom:searchItem side="last">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true">
					<custom:sort property="username" page="${page}" width="12%">User</custom:sort>
					<custom:sort property="drivername" page="${page}" width="12%">Department</custom:sort>
					<custom:sort property="drivername" page="${page}" width="12%">Driver</custom:sort>
					<custom:sort property="startime" page="${page}" width="18%">Start Time</custom:sort>
					<custom:sort property="endtime" page="${page}" width="18%">End Time</custom:sort>
					<custom:sort property="remark" page="${page}" width="18%">Remark</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="counseling" index="i" var="u">
					<custom:tableItem>${u.username}</custom:tableItem>
					<custom:tableItem>${u.departname}</custom:tableItem>
					<custom:tableItem>${u.drivername}</custom:tableItem>
					<custom:tableItem>${u.startime}</custom:tableItem>
					<custom:tableItem>${u.endtime}</custom:tableItem>
					<custom:tableItem>${u.remark}</custom:tableItem>
					<custom:tableItem>
						<custom:edit path="task/counsell" id="${u.counseling}"/>
						<custom:del path="task/counsell" id="${u.counseling}"/>
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