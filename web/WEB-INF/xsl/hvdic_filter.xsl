<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : hvdic_filter.xsl
    Created on : October 10, 2019, 2:58 PM
    Author     : Poca
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns="http://project.com/jaxb">
    <xsl:output method="xml" omit-xml-declaration="yes" encoding="UTF-8"/>
    <xsl:template match="/">
        <xsl:element name="words">
            <xsl:element name="word">
            <xsl:apply-templates/>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="/body/section/table/tr/td/div[@class='hvres han-word']">
        <xsl:element name="unicodeString">
            <xsl:value-of select=".//div/div[@class='hvres-meaning'][1]/a[@target='_blank']"/>
        </xsl:element>
        
        <xsl:element name="wordString">
            <xsl:value-of select=".//div/div/a[contains(@href,'/whv/')]"/>
        </xsl:element>
        
        <xsl:element name="noOfStroke"/>
        
        <xsl:element name="variants">
            <xsl:for-each select=".//div/div[@class='hvres-meaning'][3]/a[@href]">
                <xsl:element name="variant">
                    <xsl:value-of select=".//span[@class='hvres-variant han']"/>
                </xsl:element>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

    <xsl:template match="/body/section/table/tr/td[@class='main-content']">
        <xsl:apply-templates/>
        <xsl:element name="wordDetails">
            <xsl:for-each select=".//div[@data-hvres-idx]">
                <xsl:element name="wordDetail">
                    <xsl:element name="pronounciation">
                        <xsl:element name="reading">
                            <xsl:attribute name="type">
                                <xsl:text>hanviet</xsl:text>
                            </xsl:attribute>
                            <xsl:value-of select=".//a[contains(@href,'/hv/')]"/>
                        </xsl:element>
                    </xsl:element>
                    <xsl:element name="wordType">
                        <xsl:value-of select=".//p[@class='hvres-info gray small']"/>
                    </xsl:element>
                    <xsl:element name="wordInfos">
                        <xsl:for-each select=".//div[@class='hvres-meaning han-clickable']">
                            <xsl:element name="wordInfo">
                                <xsl:text>&lt;Explain/&gt; &lt;br/&gt;</xsl:text>
                                <xsl:value-of select="."/>
                            </xsl:element>
                        </xsl:for-each>
                    </xsl:element>
                </xsl:element>
            </xsl:for-each>
            
        </xsl:element>
        
    </xsl:template>
    <xsl:template match="text()"/>
</xsl:stylesheet>
