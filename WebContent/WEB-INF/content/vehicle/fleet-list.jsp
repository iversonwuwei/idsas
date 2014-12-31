<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript">
			function excel(){
				if(0 == "${fn:length(page.result)}"){
		         	alert("No derived data!");
		         	return;
		     	}
	     		document.forms['queryResult'].action = basePath + 'vehicle/fleet-list!doExport';
	         	document.forms['queryResult'].submit();
	         	document.forms['queryResult'].action = basePath + 'vehicle/fleet-list!index';
			}
			
			function delOrg(orgID) {
	 			$.ajax({
	 			    type: "POST",
	 			    async: false,
	 			    dataType: 'JSON',
	 			    data :{
	 			    	'vo.fleetID' : orgID
	 			    },
	 			    url : basePath + "vehicle/fleet-list!checkDelete",
	 			    success : function(data) {
	 			    	if(data.result == "OK") {
				 			if(confirm('Be sure you want to delete this data?')) {
				 				location = basePath + 'vehicle/fleet-list/' + orgID + '/destory?editble=1';
				 			}
	 			    	} else {
	 			    		alert('The fleet which has subordinate ' + data.result + ' can not be deleted!');
	 			    	}
	 			    },
	 			    error : function(d) {
	 					alert("ERROR!");
	 			    }
	 			});
	 		}
		</script>
	<head>
	<body>
		<s:if test="%{editble == 1}">
			<custom:navigation father="Organization Management" model="Fleet" excelMethod="excel()" addPath="vehicle/fleet-list" addMethod="editNew" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Query" model="Fleet" excelMethod="excel()" />
		</s:else>
		<form name="queryResult" action="<%=basePath%>vehicle/fleet-list!index" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<custom:searchBody>
				<custom:searchItem title="Fleet Name" side="first">
					<custom:selTeam name="vo.fleetID" value="${vo.fleetID}" />
				</custom:searchItem>
				<custom:searchItem title="Department">
					<custom:selCom name="vo.parentID" value="${vo.parentID}" width="150" />
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true">
					<custom:sort property="orgName" page="${page}" width="25%">Fleet Name</custom:sort>
					<custom:sort property="parentName" page="${page}" width="25%">Department</custom:sort>
					<custom:sort property="description" page="${page}" width="40%">Description</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="orgID" index="i" var="u">
					<custom:tableItem>${u.orgName}</custom:tableItem>
					<custom:tableItem>${u.parentName}</custom:tableItem>
					<custom:tableItem>
						<div title="${u.description}" style="width:480px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
			        		<label>${u.description}</label>
			        	</div>
					</custom:tableItem>
					<custom:tableItem>
						<custom:show path="vehicle/fleet-list" id="${u.orgID}"/>
						<s:if test="%{editble == 1}">
							<custom:edit path="vehicle/fleet-list" id="${u.orgID}"/>
							<custom:del path="vehicle/fleet-list" id="${u.orgID}" jsmethod="delOrg" />
						</s:if>
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