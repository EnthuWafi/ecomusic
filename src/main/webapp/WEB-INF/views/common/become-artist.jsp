<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="become-artist-container">
    <h1>Upgrade to Artist</h1>
    <p>Start sharing your music, build your audience, and earn.</p>

    <div class="subscription-plan">
        <h2>Artist Plan</h2>
        <p><strong>$9.99/month</strong></p>
        <ul>
            <li>Unlimited music uploads</li>
            <li>Artist dashboard & analytics</li>
            <li>Direct fan subscriptions</li>
            <li>Priority support</li>
        </ul>

        <form action="${pageContext.request.contextPath}/subscribe-artist" method="post">
            <button type="submit" class="btn btn-primary">Upgrade Now</button>
        </form>
    </div>
</div>
