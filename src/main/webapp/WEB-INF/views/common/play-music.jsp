<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="player-root"></div>


<script type="module">
	import { MusicPlayer } from "${pageContext.request.contextPath}/assets/js/components/MusicPlayer.js";
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
