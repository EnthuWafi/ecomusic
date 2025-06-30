<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="dashboard-root" class="container justify-content-center"></div>

<script type="module">
  import { AdminDashboard } from "${pageContext.request.contextPath}/assets/js/components/AdminDashboard.js";

  const container = document.getElementById('dashboard-root');
  const root = ReactDOM.createRoot(container);
  root.render(React.createElement(AdminDashboard, { baseUrl: window.baseUrl }));
</script>