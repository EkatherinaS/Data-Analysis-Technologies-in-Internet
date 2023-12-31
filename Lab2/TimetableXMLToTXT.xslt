﻿<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl"
>
    <xsl:output method="text" indent="yes"/>
	<xsl:strip-space elements="*"/>

	<xsl:template match="/">Расписание:
	<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="weekday">
		<xsl:text>&#10;</xsl:text>
	<xsl:value-of select="@name"/>
		<xsl:text>&#10;</xsl:text>
	<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="subject">
		<xsl:text>&#10;</xsl:text>
		<xsl:value-of select="time/@start"/>-<xsl:value-of select="time/@finish"/>
		<xsl:text>&#10;</xsl:text>
		<xsl:value-of select="name"/>
		<xsl:text>&#32;&#40;</xsl:text>
		<xsl:value-of select="@type"/>
		<xsl:text>&#41;&#10;</xsl:text>
		<xsl:value-of select="professor"/>&#10;
		<xsl:text>&#10;</xsl:text>
		<xsl:value-of select="room"/>&#10;
		<xsl:text>&#10;</xsl:text>
	</xsl:template>
	
</xsl:stylesheet>
