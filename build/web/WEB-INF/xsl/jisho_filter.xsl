<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : jisho-filter.xsl
    Created on : October 13, 2019, 11:29 PM
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
        
    <xsl:template match="//div[@class='kanji details']/div[@class='row'][3]//section[@class and @id='codepoints']//tr[@class][2]">
        <!--<xsl:apply-templates/>-->
        <xsl:element name="unicodeString">
            <xsl:value-of select=".//td[@class='dic_ref']"/>
        </xsl:element>
        
    </xsl:template>
    
    <!--<xsl:template match="//div[@class='large-12 columns']/div[@id='main_results']/div[@id='result_area']/div[@class='kanji details']/div[@class='row']">-->
<!--    <xsl:template match="//div[@class='kanji details']/div[@class='row'][1]/div[@class='small-12 large-2 columns']/div[@class='row']">
        
        <xsl:apply-templates/>
    </xsl:template>-->
    
    <xsl:template match="//div[@class='kanji details']/div[@class='row'][1]/div[@class='small-12 large-2 columns']/div[@class='row']">
        <xsl:apply-templates/>
        <xsl:element name="wordString">
            <xsl:value-of select=".//h1[@class='character' and @lang='ja']"/>
        </xsl:element>
        <xsl:element name="noOfStroke">
            <xsl:value-of select=".//div[@class='kanji-details__stroke_count']/strong"/>
        </xsl:element>
        <xsl:element name="variants">
            <xsl:for-each select=".//dl[@class='dictionary_entry variants']/dd//a[@href]">
                <xsl:element name="variant">
                    <xsl:value-of select="."/>
                </xsl:element>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>
    
    
    <xsl:template match="//div[@class='kanji details']/div[@class='row'][1]/div[@class='small-12 large-10 columns']">
        <xsl:apply-templates/>
        <xsl:element name="wordDetails">
            <xsl:element name="wordDetail">
                <xsl:element name="pronounciation">
                    <xsl:for-each select=".//dl[@class='dictionary_entry on_yomi']/dd[@class and @lang='ja']/a[@href]">
                        <xsl:element name="reading">
                            <xsl:attribute name="type">
                                <xsl:text>onyomi</xsl:text>
                            </xsl:attribute>
                            <xsl:value-of select="."/>
                        </xsl:element>
                    </xsl:for-each>
                    
                    <xsl:for-each select=".//dl[@class='dictionary_entry kun_yomi']/dd[@class and @lang='ja']/a[@href]">
                        <xsl:element name="reading">
                            <xsl:attribute name="type">
                                <xsl:text>kunyomi</xsl:text>
                            </xsl:attribute>
                            <xsl:value-of select="."/>
                            
                        </xsl:element>
                    </xsl:for-each>
                </xsl:element>
                
                <xsl:element name="wordType"/>
                <xsl:element name="wordInfos">
                    <xsl:element name="wordInfo">
                        <xsl:value-of select=".//div[@class='kanji-details__main-meanings']"/>
                    </xsl:element>
                </xsl:element>
            </xsl:element>
        </xsl:element>
        <!--<xsl:apply-templates/>-->
    </xsl:template>
    
    <xsl:template match="text()"/>
</xsl:stylesheet>
