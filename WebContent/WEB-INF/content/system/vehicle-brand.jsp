<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<body>
		<custom:navigation father="System Settings" model="Vehicle Brand"  addPath="system/vehicle-brand" addMethod="add" />
		<form name="queryResult" action="<%=basePath%>system/vehicle-brand!index" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<custom:table>
				<custom:tableHeader>
					<custom:sort property="comname" page="${page}" width="30%">Company</custom:sort>
					<custom:sort property="type" page="${page}" width="30%">Vehicle Brand</custom:sort>
					<custom:sort property="description" page="${page}" width="30%">Description</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="id" index="i" var="v">
					<custom:tableItem>${v.comname}</custom:tableItem>
					<custom:tableItem>${v.name}</custom:tableItem>
					<!-- <td style="text-align: center;"></td> -->
					<custom:tableItem>
						<div title="${v.memo}" style="width:350px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">
			        		${v.memo}
			        	</div>
					</custom:tableItem>
					<s:if test="%{editble == 1}">
					<custom:tableItem>
						<custom:show path="system/vehicle-brand" id="${v.id}"/>
						<custom:edit path="system/vehicle-brand" id="${v.id}"/>
						<a href="#" onclick="_del('${v.id}')"><img title="Delete" src="<%=basePath %>images/dtree/ico_delete.gif"/></a>
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
							alert("The name is using");
						}else{
							location="<%=basePath%>system/vehicle-brand/"+id+"/delete?editble=1";				
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
					    	id : id
					    },
					    url : basePath + "system/vehicle-brand!checkUsed",
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