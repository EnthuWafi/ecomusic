<h2>Add New Music</h2>

<form action="${pageContext.request.contextPath}/music" method="post">
    <label for="title">Title:</label>
    <input type="text" id="title" name="title" required><br><br>

    <label for="genre">Genre:</label>
    <input type="text" id="genre" name="genre" required><br><br>

    <label for="description">Description:</label>
    <textarea id="description" name="description" required></textarea><br><br>

    <label for="audioFileUrl">Audio File URL:</label>
    <input type="text" id="audioFileUrl" name="audioFileUrl" required><br><br>

    <label for="premiumContent">Premium Content:</label>
    <input type="checkbox" id="premiumContent" name="premiumContent"><br><br>

    <input type="submit" value="Add Music">
</form>