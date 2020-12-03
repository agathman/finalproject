<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Final Project</title>
<style><%@include file="../css/style.css"%></style>
</head>
<body>
<a href="/">Home</a> &emsp; <a href="/viewSnapshots">Back</a>
<h1>${country.getCountryName()}</h1>
<h2>${country.getDate()}</h2>

<form method="post" action="/submitChanges">
<input type="hidden" name="id" value="${country.getId()}">
<table>

    <tr>
         <td>Total Cases</td>
         <td><input type="text" name="totalCases" value="${country.getTotalCases()}"></td>
         </tr>
    <tr>
        <td>Total Deaths</td>
         <td><input type="text" name="totalDeaths" value="${country.getTotalDeaths()}"></td>
         </tr>
    <tr>
        <td>New Cases</td>
         <td><input type="text" name="newCases" value="${country.getNewCases()}"></td>
         </tr>
</table>
        <input type="submit" value="Submit Changes">

