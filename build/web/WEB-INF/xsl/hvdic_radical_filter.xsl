<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : hvdic_radical_filter.xsl
    Created on : October 14, 2019, 11:54 AM
    Author     : Poca
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/">
        <xsl:element name="info">
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="/body/section/table/tr/td[@class='main-content']">
        <xsl:for-each select=".//div[@class='hv-word-group']">
            <xsl:element name="group">
                <xsl:element name="noOfRadical">
                    <xsl:value-of select=".//h3"/>
                    <xsl:element name="links">
                        <xsl:for-each select=".//a[@class and @href]">
                            <xsl:element name="link">
                                <xsl:value-of select=".//@href"/>
                            </xsl:element>
                        </xsl:for-each>
                    </xsl:element>
                </xsl:element>
            </xsl:element>
        </xsl:for-each>
    </xsl:template>
    <xsl:template match="text()"/>
</xsl:stylesheet>
