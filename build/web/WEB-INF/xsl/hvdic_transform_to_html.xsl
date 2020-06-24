<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : hvdic_transform_to_html.xsl
    Created on : October 13, 2019, 1:33 AM
    Author     : Poca
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:x="http://project.com/jaxb"
>
    <xsl:output method="html" encoding="UTF-8"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/">
        <html>
            <body>
                <xsl:apply-templates/>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="x:word">
        <p style="font-size:100px; font-family: DFKai-SB">
            <xsl:value-of select="x:wordString"/>
        </p>
        <p style="font-size:30px">
            <xsl:for-each select=".//x:wordDetail">
                <xsl:value-of select=".//x:reading"/>, 
            </xsl:for-each>
        </p>
        <table border="1px">
            <tr>
                <th>Unicode</th>
                <th>Số nét</th>
            </tr>
            <tr>
                <td>
                    <xsl:value-of select="x:unicodeString"/>
                </td>
                <td>
                    <xsl:value-of select="x:noOfStroke"/>
                </td>
            </tr>
        </table>
        
        <p>
            <b>Giải nghĩa</b>
        </p>
        <!--<xsl:text>-->
            <xsl:for-each select=".//x:wordInfos">
                <p>
                    <xsl:value-of select=".//x:wordInfo" disable-output-escaping="yes"/>
                </p>
            </xsl:for-each>
        
        <!--</xsl:text>-->
        
    </xsl:template>
</xsl:stylesheet>
