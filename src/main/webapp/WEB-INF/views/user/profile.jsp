<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container mt-5">
	<h2 class="mb-4">User Profile</h2>

	<div class="row">
		<!-- Profile Sidebar -->
		<div class="col-md-4 mb-4">
			<div class="card shadow-sm">
				<div class="card-body text-center">
					<img
						src="${pageContext.request.contextPath}/stream/image/user/${user.userId}"
						class="rounded-circle mb-3" alt="Profile Image" width="120"
						height="120" />
					<h4>${user.firstName} ${user.lastName}</h4>
					<p class="text-muted">@${user.username}</p>
					<p>${user.bio}</p>
					<span class="badge bg-secondary">${user.roleName}</span>
					<c:if test="${user.premium}">
						<span class="badge bg-warning text-dark">Premium</span>
					</c:if>
					<c:if test="${user.artist}">
						<span class="badge bg-info text-dark">Artist</span>
					</c:if>
				</div>
				<div class="card-footer text-center">
					<a href="${pageContext.request.contextPath}/user/subscription"
						class="btn btn-outline-primary btn-sm"> <i
						class="bi bi-star-fill"></i> View Subscription
					</a>
				</div>
			</div>

			<div class="card mt-4 shadow-sm">
				<div class="card-header">Stats</div>
				<div class="card-body">
					<p>
						<strong>Uploaded Music:</strong> ${musicCount}
					</p>
					<p>
						<strong>Playlists:</strong> ${playlistCount}
					</p>
					<p>
						<strong>Total Listening Time:</strong> ${listeningTime}
					</p>
				</div>
			</div>
		</div>

		<!-- Profile Form -->
		<div class="col-md-8">
			<div class="card shadow-sm">
				<div class="card-header">Update Profile</div>
				<div class="card-body">
					<form method="post"
						action="${pageContext.request.contextPath}/user/profile"
						enctype="multipart/form-data">
						<div class="mb-3">
							<label for="firstName" class="form-label">First Name</label> <input
								type="text" name="firstName" class="form-control"
								value="${user.firstName}" required>
						</div>
						<div class="mb-3">
							<label for="lastName" class="form-label">Last Name</label> <input
								type="text" name="lastName" class="form-control"
								value="${user.lastName}" required>
						</div>
						<div class="mb-3">
							<label for="username" class="form-label">Username</label> <input
								type="text" name="username" class="form-control"
								value="${user.username}" required>
						</div>
						<div class="mb-3">
							<label for="bio" class="form-label">Bio</label>
							<textarea name="bio" class="form-control" rows="3">${user.bio}</textarea>
						</div>
						<div class="mb-3">
							<label for="email" class="form-label">Email</label> <input
								type="email" name="email" class="form-control"
								value="${user.email}" required>
						</div>
						<div class="mb-3">
							<label for="image" class="form-label">Profile Picture</label> <input
								type="file" name="image" class="form-control" accept="image/*">
							<div class="mt-2">
								<img
									src="${pageContext.request.contextPath}/stream/image/user/${user.userId}"
									class="rounded" alt="Current Profile Image" width="100">
							</div>
						</div>
						<button type="submit" class="btn btn-primary">
							<i class="bi bi-save me-1"></i> Save Changes
						</button>
					</form>

				</div>
			</div>

			<div class="text-muted mt-3 small">
				Account created on:
				<fmt:formatDate value="${user.createdAtDate}" type="date" />
				<br /> Last updated:
				<fmt:formatDate value="${user.updatedAtDate}" type="date" />
			</div>
		</div>
	</div>
</div>
