<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2>Login here</h2>
<form action="${pageContext.request.contextPath}/login" method="post">
    <label>Username or Email:</label>
    <input type="text" name="email" required />
    <label>Password:</label>
    <input type="password" name="password" required />
    <button type="submit">Login</button>
</form>
