<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="dashboard-root" class="container justify-content-center"></div>

<script
src="${pageContext.request.contextPath}/assets/js/components/AdminDashboard.js"></script>
<script>
	const container = document.getElementById('dashboard-root');
	const root = ReactDOM.createRoot(container);
	root.render(React.createElement(AdminDashboard, {}));
</script>