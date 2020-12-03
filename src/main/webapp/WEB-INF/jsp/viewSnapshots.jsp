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
       <th>New Cases</th>
       <th>User</th>
     </tr>
          <tr>
             <c:forEach var = "country" items = "${countryList}">

                 <td>${country.getCountryName()}</td>
                 <td>${country.getDate()}</td>
                 <td>${country.getTotalCases()}</td>
                 <td>${country.getTotalDeaths()}</td>
                 <td>${country.getNewCases()}</td>
                 <td><a href="/edit/${country.getId()}">Edit</a>
          </tr>
           </c:forEach>
           </table>












 </body>
 </html>






