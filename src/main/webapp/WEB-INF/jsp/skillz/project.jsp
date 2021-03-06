<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
 	
<%@ taglib prefix="minos" tagdir="/WEB-INF/tags/core" %>


<h2>
	<core:choose>
		<core:when test="${project.owner == null}">
			<spring:message code="skillz.project" />
			<core:set var="action" value="/web/skillz/projects" />
		</core:when>
		<core:otherwise>
			<spring:message code="skillz.project.private" />
			<core:set var="action" value="/web/skillz/user/${project.owner.username}/projects" />
		</core:otherwise>
	</core:choose>
</h2>


<minos:form modelAttribute="project" action="${action}">

	<table class="form">
		<tr>
			<td class="label"><spring:message code="name" />:</td>
			<td><form:input path="name" /></td>
			<td><form:errors path="name" /></td>
		</tr>
		<tr>
			<td class="label"><spring:message code="description" />:</td>
			<td><form:textarea path="description" cssClass="textarea" cols="70" rows="8" /></td>
			<td><form:errors path="description" /></td>
		</tr>
		<tr>
			<td class="label"><spring:message code="skillz.project.customer" />:</td>
			<td><form:input path="customer" /></td>
			<td><form:errors path="customer" /></td>
		</tr>
		<tr>
			<td class="label"><spring:message code="skillz.project.industry" />:</td>
			<td><form:input path="industry" /></td>
			<td><form:errors path="industry" /></td>
		</tr>
		<tr>
			<td class="label"><spring:message code="skillz.project.platform" />:</td>
			<td><form:textarea path="platform" cssClass="textarea" cols="70" rows="8" /></td>
			<td><form:errors path="platform" /></td>
		</tr>
		<tr>
			<td class="label"><spring:message code="skillz.project.confidential" />:</td>
			<td><form:checkbox path="confidential" /></td>
			<td><form:errors path="confidential" /></td>
		</tr>
		<tfoot>
			<tr>
				<td>
					<form:hidden path="id" />
                                        <form:hidden path="owner" />
					<input type="submit" value="<spring:message code="core.ui.ok" />" />
					<a href="../../skillz#tabs-3"><spring:message code="core.ui.cancel" /></a>
				</td>
			</tr>
		</tfoot>
	</table>
</minos:form>