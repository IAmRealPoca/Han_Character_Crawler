<%-- 
    Document   : crawlManager
    Created on : Oct 15, 2019, 11:56:50 AM
    Author     : Poca
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Crawl Manager</title>
    </head>
    <body>
        <h1>Crawl</h1>
        <form action="DispatcherServlet">
            Try to crawl everything.
            <br/>
            Number of Page to crawl: <input type="number" name="txtNoOfPage" value=""/><br/>
            <input type="hidden" name="txtCrawlAll" value="true"/>
            <input type="submit" value="CrawlAll" name="btAction" />
        </form>

        <form action="DispatcherServlet">
            Crawl list of popular characters<br/>
            Base page url: <input disabled="true" type="text" name="txtBasePage" value=""/> <br/>
            Number of Page to crawl: <input type="number" name="txtNoOfPage" value=""/>
            <br/>
            <input type="submit" value="CrawlCareful" name="btAction" onclick="encode()"/>
        </form>
        
        <form action="DispatcherServlet">
            Crawl one word<br/>
            Word: <input type="text" name="txtWord" value="" /><br/>
            Unicode: <input type="text" name="txtUnicode" value="" />
            <br/>
            <input type="submit" value="CrawlOnePage" name="btAction"/>
        </form>
    </body>
</html>
