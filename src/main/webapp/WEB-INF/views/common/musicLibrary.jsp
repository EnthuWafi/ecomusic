<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h2>Music Library</h2>

<table border="1">
	<tr>
		<th>Title</th>
		<th>Genre</th>
		<th>Description</th>
		<th>Audio URL</th>
		<th>Upload Date</th>
		<th>Premium Content</th>
	</tr>

	<c:forEach var="music" items="${musicList}">
		<tr>
			<td>${music.title}</td>
			<td>${music.genre}</td>
			<td>${music.description}</td>
			<td>${music.audioFileUrl}</td>
			<td><fmt:formatDate value="${music.uploadDate}" pattern="yyyy-MM-dd" /></td>
			<td>${music.premiumContent ? 'Yes' : 'No'}</td>
		</tr>
	</c:forEach>

</table>