<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="user-list-root" class="container justify-content-center"></div>

<script
	src="${pageContext.request.contextPath}/assets/js/components/AdminUserList.js"></script>
<script>
	const container = document.getElementById('user-list-root');
	const root = ReactDOM.createRoot(container);
	root.render(React.createElement(AdminUserList, {}));
</script>