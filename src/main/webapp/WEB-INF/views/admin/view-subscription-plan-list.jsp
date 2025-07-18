<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="list-root" class="container justify-content-center"></div>

<script type="module">
  import { AdminSubscriptionPlanList } from "${pageContext.request.contextPath}/assets/js/components/AdminSubscriptionPlanList.js";

  const container = document.getElementById('list-root');
  const root = ReactDOM.createRoot(container);
  root.render(React.createElement(AdminSubscriptionPlanList, { baseUrl: window.baseUrl }));
</script>