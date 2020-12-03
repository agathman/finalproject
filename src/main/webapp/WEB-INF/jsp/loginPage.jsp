<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html class="loginHtml">
<head>
 <style><%@include file="../css/style.css"%></style>
 </head>
 <body class="loginBody">
    <div class="container1">
        <div class="container2">
        <h3>Admin Username: admin</h3>
        <h3>Non-Privileged Username: user</h3>
        <h3>Password: password</h3>

        <form id="login" method="get" action="/login/">
        <fieldset>
        <table>
            <tr>
            <td>
             <label>Username:</label></td>
            <td>
             <input type="text" name="userName" value="admin"></td>
            </tr>
        <tr>
            <td>
            <label>Password:</label>
        </td>
            <td>
            <input type="text" name="passWord" value="password">
            </td>
         </tr>
        </table>
        <input type="submit" value="Login">
        <br><a href="/">Back</a>
        </fieldset>
        </form>
        </div>
        </div>
    <body>
    </html>
