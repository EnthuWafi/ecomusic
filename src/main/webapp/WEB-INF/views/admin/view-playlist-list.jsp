<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="list-root" class="container justify-content-center"></div>

<script type="module">
  import { AdminPlaylistList } from "${pageContext.request.contextPath}/assets/js/components/AdminPlaylistList.js";

  const container = document.getElementById('list-root');
  const root = ReactDOM.createRoot(container);
  root.render(React.createElement(Playlist, { baseUrl: window.baseUrl }));
</script>