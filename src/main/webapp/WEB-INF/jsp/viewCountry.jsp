<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
        <head>

            <title>Final Project</title>
        </head>

        <style><%@include file="../css/style.css"%></style>

 <body>
  <a href="/">Home</a>
  <table>
      <tr>
        <th>Country</th>
        <th>Date</th>
        <th>Total Cases</th>
        <th>Total Deaths</th>
        <th>New Cases</th>
        <th>Total Recovered</th>
      </tr>
           <tr>
                                  <td>${country.getCountryName()}</td>
                                  <td>${country.getDate()}</td>
                                  <td>${country.getTotalCases()}</td>
                                  <td>${country.getTotalDeaths()}</td>
                                  <td>${country.getNewCases()}</td>
                                  <td>${country.getRecovered()}</td>
           </tr>

            </table>












  </body>
  </html>