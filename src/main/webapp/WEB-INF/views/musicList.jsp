<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h2>All Music</h2>
<a href="${pageContext.request.contextPath}/music?action=add">Add
	New Music</a>

<table border="1">
	<tr>
		<th>Title</th>
		<th>Genre</th>
		<th>Description</th>
		<th>Audio URL</th>
		<th>Premium Content</th>
	</tr>

	<c:forEach var="music" items="${musicList}">
		<tr>
			<td>${music.title}</td>
			<td>${music.genre}</td>
			<td>${music.description}</td>
			<td>${music.audioFileUrl}</td>
			<td>${music.premiumContent ? 'Yes' : 'No'}</td>
		</tr>
	</c:forEach>

</table>