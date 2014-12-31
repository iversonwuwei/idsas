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
     			document.forms['queryResult'].action = basePath + 'vehicle/com!doExport';
         		document.forms['queryResult'].submit();
         		document.forms['queryResult'].action = basePath + 'vehicle/com!index';
			}
	 		
	 		function delOrg(orgID) {
	 			$.ajax({
	 			    type: "POST",
	 			    async: false,
	 			    dataType: 'JSON',
	 			    data :{
	 			    	'vo.departID' : orgID
	 			    },
	 			    url : basePath + "vehicle/com!checkDelete",
	 			    success : function(data) {
	 			    	if(data.result == "OK") {
				 			if(confirm('Be sure you want to delete this data?')) {
				 				location = basePath + 'vehicle/com/' + orgID + '/destory?editble=1';
				 			}
	 			    	} else {
	 			    		alert('The company which has subordinate department can not be deleted!');
	 			    	}
	 			    },
	 			    error : function(d) {
	 					alert("ERROR!");
	 			    }
	 			});
	 		}
		</script>
	</head>
	<body>
		<s:if test="%{editble == 1}">
			<custom:navigation father="Organization Management" model="Company" excelMethod="excel()" addPath="vehicle/com" addMethod="editNew" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Query" model="Company"/>
		</s:else>
		<form name="queryResult" action="<%=basePath%>vehicle/com!index" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<custom:searchBody>
				<custom:searchItem title="Company Name" side="first">
					<input name="vo.departName" value="${vo.departName}"/>
				</custom:searchItem>
				<custom:searchItem title="Schedule Option" >
					<s:select list="#{'3':'all','0':'Non-RFID','1':'RFID'}" name="vo.departID"  theme="simple"/>				
				</custom:searchItem>
				<custom:searchItem side="last" width="80%">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true">
					<custom:sort property="orgName" page="${page}" width="25%">Company Name</custom:sort>
					<custom:sort property="lineno" page="${page}" width="25%">Schedule Option</custom:sort>
					<custom:sort property="description" page="${page}" width="40%">Description</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="orgID" index="i" var="u">
					<custom:tableItem>${u.orgName}</custom:tableItem>
					<custom:tableItem>
					<c:if test="${u.lineno==0}">Non-RFID</c:if>
					<c:if test="${u.lineno==1}">RFID</c:if>
					
					</custom:tableItem>
					<custom:tableItem>
						<div title="${u.description}" style="width:480px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
			        		<label>${u.description}</label>
			        	</div>
					</custom:tableItem>
					<custom:tableItem>
					<custom:show path="vehicle/com" id="${u.orgID}"/>
					<s:if test="%{editble == 1}">
						<custom:edit path="vehicle/com" id="${u.orgID}"/>
						<custom:del path="vehicle/com" id="${u.orgID}" jsmethod="delOrg" />
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