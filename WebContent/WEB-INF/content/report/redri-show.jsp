<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<body>
		<custom:navigation father="Chart Report" model="Report Driver"  operate="Show" homePath="report/redri" />
		<form action="<%=basePath%>system/device-status!create" name="queryResult" method="post">
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="15%">Driver:</td>
						<td width="35%" style="text-align: left;">${os[0]}</td>
						<td class="text_bg" width="15%">Time:</td>
						<td width="35%" style="text-align: left;">${ges}&nbsp;00:00:00 - ${les}&nbsp;23:59:59</td>
					</tr>
					<tr>
						<td class="text_bg" >Total:</td>
						<td style="text-align: left;">
							 ${os[2]}
						</td>
						<td class="text_bg">Score:</td>
						<td style="text-align: left;">
							<c:if test="${dvo.behCont==1}">
							 		<c:if test="${(100-os[3])>0}">
							 		<fmt:formatNumber value="${100-os[3]}" pattern="#.##" />
							 		</c:if>
							 		<c:if test="${(100-os[3])<=0}">
							 		0
							 		</c:if>
							 	</c:if>
							 	<c:if test="${dvo.behCont==2}">
							 		<c:if test="${(100-os[3]/7)>0}">
							 			<fmt:formatNumber value="${100-os[3]/7}" pattern="#.##" />
							 		</c:if>
							 		<c:if test="${(100-os[3]/7)<=0}">
							 		0
							 		</c:if>
							 	</c:if>
							 	<c:if test="${dvo.behCont==3}">
							 		<c:if test="${(100-os[3]/30)>0}">
							 			<fmt:formatNumber value="${100-os[3]/30}" pattern="#.##" />
							 		</c:if>
							 		<c:if test="${(100-os[3]/30)<=0}">
							 			0
							 		</c:if>
							 	</c:if>
							 	<c:if test="${dvo.behCont==4}">
							 		<c:if test="${(100-os[3]/365)>0}">
							 			<fmt:formatNumber value="${100-os[3]/365}" pattern="#.##" />
							 		</c:if>
							 		<c:if test="${(100-os[3]/365)<=0}">
							 			0
							 		</c:if>
							 	</c:if>
						</td>
					</tr>
					<s:iterator value="scorelist" var="u" status="s">
					<tr>
						<td class="text_bg">
							${u.type}
						</td>
						<td colspan="3">
							<div style="width: 80px;float: left;">&nbsp;&nbsp;count:&nbsp;&nbsp;${os[s.index*2+6]} </div>
							<div style="width:85px;float: left;">&nbsp;&nbsp;score:&nbsp;&nbsp;${(u.weight-os[s.index*2+7])} </div>
							&nbsp;(
							<c:if test="${u.weight!=null}">
								${u.weight}
							</c:if>
							<c:if test="${u.weight==null}">
								no set
							</c:if>
							) 
						</td>
					</tr>
					</s:iterator>
				</table>
			</div>
		</form>
	</body>
</html>