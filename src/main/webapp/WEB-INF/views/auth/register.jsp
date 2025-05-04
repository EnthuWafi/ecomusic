
<h1>Registration</h1>
<h2>Create an Account</h2>
<form action="${pageContext.request.contextPath}/register" method="post">
  First Name: <input type="text" name="fname">
  Last Name: <input type="text" name="lname"><br>
  Username: <input type="text" name="username"><br>
  Email: <input type="email" name="email"><br>
  Password: <input type="password" name="password"><br>
  <button type="submit">Create</button>
</form>
