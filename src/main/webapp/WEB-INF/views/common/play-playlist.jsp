<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="player-root"></div>


<script type="module">
	import { PlaylistPlayer } from "${pageContext.request.contextPath}/assets/js/components/PlaylistPlayer.js";
	const path = window.location.pathname;
	const idMatch = path.match(/\/(\d+)(\/)?$/);
	const playlistId = idMatch ? idMatch[1] : null;
	const userId = ${not empty sessionScope.user ? sessionScope.user.userId : 'null'}; // OK as is
	const isAdmin = ${not empty sessionScope.user and (sessionScope.user.admin or sessionScope.user.superAdmin)};

	const container = document.getElementById('player-root');
	const root = ReactDOM.createRoot(container);
	root.render(React.createElement(PlaylistPlayer, {
		baseUrl : window.baseUrl,
		playlistId : playlistId,
		isAdmin : isAdmin,
		userId : userId
	}));
</script>
