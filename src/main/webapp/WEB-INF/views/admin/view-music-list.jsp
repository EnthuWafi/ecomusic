<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="music-list-root" class="container justify-content-center"></div>

<script type="module">
  import { AdminMusicList } from "${pageContext.request.contextPath}/assets/js/components/AdminMusicList.js";

  const container = document.getElementById('music-list-root');
  const root = ReactDOM.createRoot(container);
  root.render(React.createElement(AdminMusicList, { baseUrl: window.baseUrl }));
</script>