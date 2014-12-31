<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<body>
		<custom:navigation father="System Settings" model="Vehicle Type" addPath="system/vehicle-type" addMethod="add" />
		<form name="queryResult" action="<%=basePath%>system/vehicle-type!index" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<custom:table>
				<custom:tableHeader>
						<custom:sort property="comname" page="${page}" width="30%">Company</custom:sort>
						<custom:sort property="type" page="${page}" width="30%">Vehicle Type</custom:sort>
						<custom:sort property="description" page="${page}" width="30%">Description</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="typeid" index="i" var="v">
						<custom:tableItem>${v.comname}</custom:tableItem>
						<custom:tableItem>${v.type}</custom:tableItem>
						<custom:tableItem>
							<div title="${v.description}" style="width:350px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
				        		<label>${v.description}</label>
				        	</div>
			        	</custom:tableItem>
					<s:if test="%{editble == 1}">
						<custom:tableItem>
						<custom:show path="system/vehicle-type" id="${v.typeid}"/>
							<custom:edit path="system/vehicle-type" id="${v.typeid}"/>
							<a href="#" onclick="_del('${v.typeid}')"><img title="Delete" src="<%=basePath %>images/dtree/ico_delete.gif"/></a>
						</custom:tableItem>
					</s:if>		
					</custom:tableBody>
			</custom:table>
			<s:include value="/common/page.jsp">
				<s:param name="pageName" value="'page'" />
				<s:param name="formName" value="'queryResult'" />
				<s:param name="linkCount" value="5" />
			</s:include>
			<script type="text/javascript">
				// 删除方法，验证这个Type在没在用着，在用着不让删
				function _del(id){
					if(confirm('Be sure you want to delete this data?')){
						if(0 != checkUsed(id)){
							alert("The type is using");
						}else{
							location="<%=basePath%>system/vehicle-type/"+id+"/delete?editble=1";				
						}
					}
				}
				
				// 检查这个Type在没在用着
				function checkUsed(id){
					var temp;
					$.ajax({
						type: "POST",
					    async: false,
					    dataType:'text',
					    data :{
					    	typeid : id
					    },
					    url : basePath + "system/vehicle-type!checkUsed",
					    success : function(data) {
					    	temp = data;
					    }
					});
					return temp;
				}
			</script>
		</form>
	</body>
</html>