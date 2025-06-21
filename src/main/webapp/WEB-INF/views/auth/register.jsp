<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="container d-flex align-items-center justify-content-center"
	style="min-height: 100vh;">
	<div class="card shadow-lg p-4 w-100" style="max-width: 500px;">
		<h2 class="mb-4 text-center">Create an Account</h2>
		<form action="${pageContext.request.contextPath}/register"
			method="post">

			<div class="row mb-3">
				<div class="col">
					<label for="fname" class="form-label">First Name</label> <input
						type="text" class="form-control" id="fname" name="fname"
						placeholder="John" autocomplete="off" required>
				</div>
				<div class="col">
					<label for="lname" class="form-label">Last Name</label> <input
						type="text" class="form-control" id="lname" name="lname"
						placeholder="Doe" autocomplete="off" required>
				</div>
			</div>

			<div class="mb-3">
				<label for="username" class="form-label">Username</label> <input
					type="text" class="form-control" id="username" name="username"
					placeholder="johndoe123" autocomplete="off" required>
			</div>

			<div class="mb-3">
				<label for="email" class="form-label">Email address</label> <input
					type="email" class="form-control" id="email" name="email"
					placeholder="john@example.com" autocomplete="off" required>
			</div>

			<div class="mb-3">
				<label for="password" class="form-label">Password</label> <input
					type="password" class="form-control" id="password" name="password"
					autocomplete="off" required>
			</div>

			<button type="submit" class="btn btn-primary w-100">Create
				Account</button>
		</form>

		<p class="mt-3 text-center text-muted">
			Already have an account? <a
				href="${pageContext.request.contextPath}/login" class="text-info">Login</a>
		</p>
	</div>
</div>