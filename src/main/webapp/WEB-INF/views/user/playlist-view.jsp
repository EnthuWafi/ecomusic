<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="playlist-root" class="container justify-content-center"></div>

<script type="module">
  import { PlaylistView } from "${pageContext.request.contextPath}/assets/js/components/PlaylistView.js";
  
  const playlistJSON = ${playlistJSON};
  const container = document.getElementById('playlist-root');
  const root = ReactDOM.createRoot(container);
  root.render(React.createElement(PlaylistView, { baseUrl: window.baseUrl , playlist: playlistJSON}));
</script>