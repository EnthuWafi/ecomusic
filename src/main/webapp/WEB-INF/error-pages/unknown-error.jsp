<%@ page isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contentPage" value="/WEB-INF/error-pages/content/unknown-error-message.jsp" scope="request"/>
<jsp:include page="/WEB-INF/views/layout-main.jsp"/>