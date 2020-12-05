<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html class="view">
<head>
 <style><%@include file="../css/style.css"%></style>
 </head>
 <body>
 <a href="/">Home</a>
 <table>
     <tr>
       <th>Country</th>
       <th>Date</th>
       <th>Total Cases</th>
       <th>Total Deaths</th>
       <th>Admin Controls</th>
     </tr>
          <tr>
             <c:forEach var = "country" items = "${countryList}">

                 <td>${country.getCountryName()}</td>
                 <td>${country.getDate()}</td>
                 <td>${country.getTotalCases()}</td>
                 <td>${country.getTotalDeaths()}</td>
                 <td><a href="/edit/${country.getId()}">Edit</a>
                 <a href="/delete/${country.getId()}">Delete</a></td>

          </tr>
           </c:forEach>
           </table>












 </body>
 </html>






