<%@ page language="java" contentType="text/html; charset=ISO-8859-1" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@ taglib prefix="minos" uri="http://www.synyx.org/minos/tags" %>

<%@ taglib prefix="core" tagdir="/WEB-INF/tags/core" %>

<h2><spring:message code="skillz.template.switch" /></h2>
<p><b><spring:message code="skillz.template.switch.warning" /></b></p>

<spring:url value="/web/skillz/user/${user.username}/template" var="selectActionUrl" />

<display:table id="template" name="templates">
    <minos:column titleKey="name"><c:out value="${template.name}" /></minos:column>
    <minos:column titleKey="skillz.skills">
        <c:forEach items="${template.categories}" var="category">
            ${category.name},&nbsp;
        </c:forEach>
    </minos:column>
    <minos:column class="actions">
        <form action="${selectActionUrl}" method="post">
            <input type="hidden" name="template" value="${template.id}" />
            <input type="submit" name="Ausw&auml;hlen" value="Ausw&auml;hlen" />
        </form>
    </minos:column>
</display:table>


