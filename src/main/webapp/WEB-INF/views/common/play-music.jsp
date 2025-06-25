<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="player-root"></div>

<script
	src="${pageContext.request.contextPath}/assets/js/components/MusicPlayer.js"></script>
<script>
	const path = window.location.pathname;
	const idMatch = path.match(/\/(\d+)(\/)?$/);
	const musicId = idMatch ? idMatch[1] : null;

	const container = document.getElementById('player-root');
	const root = ReactDOM.createRoot(container);
	root.render(React.createElement(MusicPlayer, {
		baseURL : window.baseUrl,
		musicId : musicId
	}));
</script>
