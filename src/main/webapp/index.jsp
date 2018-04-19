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
<%@ page import="java.util.Date" %>
<%@ page import="java.util.UUID" %>
<%
    final String VISIT_COUNTER = "visitCounter";
    final String UID = "uid";

    String title;
    if (session.isNew()) {
        title = "New Session";
        session.setAttribute(UID, UUID.randomUUID().toString());
        session.setAttribute(VISIT_COUNTER, 0);
    } else {
        title = "Found Session";
    }

    int visitCount = (int) session.getAttribute(VISIT_COUNTER);
    visitCount = visitCount + 1;
    String uid = (String) session.getAttribute(UID);
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

</body>
</html>
