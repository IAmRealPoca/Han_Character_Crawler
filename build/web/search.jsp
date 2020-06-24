<%-- 
    Document   : success
    Created on : Oct 13, 2019, 12:06:37 AM
    Author     : Poca
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search page</title>
        <script>
            function encode() {
                var uri = document.getElementById("wordInput").value;
                var res = encodeURI(uri);
                document.getElementById("wordInput").value = res;
                return res;
            }
        </script>
        <style>
            html{
                font-size:20px;
            }

            @font-face {
                font-family: "My Custom Font";
                src: url("font/hgrskp_0.ttf") format("truetype");
            }
            customfont { 
                font-family: "My Custom Font", Verdana, Tahoma;
            }
        </style>
    </head>
    <body>
        <h3>Search page</h3>
        <form action="DispatcherServlet">
            Search by unicode: <input type="text" name="txtSearch" value="" />
            <br/>
            <input type="submit" value="Search" name="btAction" />
        </form>

        <form action="DispatcherServlet">
            Search by word: <input id="wordInput" type="text" name="txtWordSearch" value=""/>
            <br/>
            <input type="submit" value="Search" name="btAction"/>
        </form>
        
        <c:import charEncoding="UTF-8" url="WEB-INF/xml/output-hvdic-word.xml" var="xml"/>
        <c:import charEncoding="UTF-8" url="WEB-INF/xsl/hvdic_transform_to_html.xsl" var="xslt"/>
        <c:if test="${not empty xml}">
            <div>
            <x:transform xml="${xml}" xslt="${xslt}"/>
            </div>
        </c:if>
        <%--<x:out select="$xml"/>--%>
    </body>
</html>
