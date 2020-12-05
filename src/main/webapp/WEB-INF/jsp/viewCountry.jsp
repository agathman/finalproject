<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
        <head>

            <title>Final Project</title>
        </head>

        <style><%@include file="../css/style.css"%></style>

 <body class="viewCountryBody">
  <a href="/">Home</a>
  <table>
      <tr>
        <th>Country</th>
        <th>Date</th>
        <th>Total Cases</th>
        <th>Total Deaths</th>
        <th>Total Recovered</th>
        <th></th>
      </tr>
           <tr>
                                  <td>${country.getCountryName()}</td>
                                  <td>${country.getDate()}</td>
                                  <td>${country.getTotalCases()}</td>
                                  <td>${country.getTotalDeaths()}</td>
                                  <td>${country.getRecovered()}</td>

                                  <form method="post" action="/save/${country.getCountryName()}">
                                  <input type="hidden" name="date" value="${country.getDate()}">
                                                      <td><input type="submit" value="Save">
                                                      </form>
           </tr>

            </table>












  </body>
  </html>