<%--
  ~
  ~   Licensed under the Apache License, Version 2.0 (the "License");
  ~   you may not use this file except in compliance with the License.
  ~   You may obtain a copy of the License at
  ~
  ~       http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~   Unless required by applicable law or agreed to in writing, software
  ~   distributed under the License is distributed on an "AS IS" BASIS,
  ~   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~   See the License for the specific language governing permissions and
  ~   limitations under the License.
  ~
  --%>
<%@ page import="pivotal.field.example.gemfirehttpsession.SomeData" %>
<%@ page import="java.net.InetAddress" %>
<%@ page import="static pivotal.field.example.gemfirehttpsession.BaseGemFireSession.VISIT_COUNTER" %>
<%@ page import="static pivotal.field.example.gemfirehttpsession.BaseGemFireSession.UID" %>
<%@ page import="java.util.Date" %>
<%@ page import="static pivotal.field.example.gemfirehttpsession.BaseGemFireSession.USER_OBJECT" %>
<%@ page import="java.util.UUID" %>
<%


    String title;
    if (session.isNew()) {
        title = "New Session";
        session.setAttribute(UID, UUID.randomUUID().toString());
        session.setAttribute(VISIT_COUNTER, 0);
        session.setAttribute(USER_OBJECT, new SomeData("SomeFirstName", "SomeLastName"));
    } else {
        title = "Found Session";
    }

    int visitCount = (int) session.getAttribute(VISIT_COUNTER);
    visitCount = visitCount + 1;
    String uid = (String) session.getAttribute(UID);
    SomeData someData = (SomeData) session.getAttribute(USER_OBJECT);
    someData.getLocations().add("JSP - " + InetAddress.getLocalHost().getCanonicalHostName());
    session.setAttribute(VISIT_COUNTER, visitCount);
%>

<html>
<head>
    <title><%= title %>
    </title>
</head>

<style>
    h1 {
        text-align: center;
    }
</style>
<body>
<h1>Spring Session Data Gemfire</h1>
<h1><%= title%>
</h1>

<table border="1" align="center">
    <tr bgcolor="#d3d3d3">
        <th>Session info</th>
        <th>Value</th>
    </tr>
    <tr>
        <td>id</td>
        <td><%= session.getId()%>
        </td>
    </tr>
    <tr>
        <td>Creation Time</td>
        <td><%= new Date(session.getCreationTime()) %>
        </td>
    </tr>
    <tr>
        <td>Time of Last Access</td>
        <td><%= new Date(session.getLastAccessedTime()) %>
        </td>
    </tr>
    <tr>
        <td>UID</td>
        <td><%= uid %>
        </td>
    </tr>
    <tr>
        <td>Visit Counter</td>
        <td><%= visitCount %>
        </td>
    </tr>
</table>
<h1>The locations where the session data has been accessed</h1>
<%= someData.getName()%> <%= someData.getLastName()%>
<ol>

    <%
        for (String location : someData.getLocations()) {
    %>
    <li><%=location%>
    </li>
    <%
        }
    %>
</ol>
</body>
</html>
