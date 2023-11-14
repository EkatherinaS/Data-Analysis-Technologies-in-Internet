<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl"
>
    <xsl:output method="html" indent="yes"/>
	<xsl:strip-space elements="*"/>

    <xsl:template match="/">
		<html>
			<style>
			table, th, td {
				border:1px solid;
				border-color: #8E8E8E;
			}
			</style>
			<body>
				<table>
					<tr>
						<th>время</th>
						<th>предмет</th>
						<th>препод.</th>
						<th>ауд.</th>
						<xsl:apply-templates/>
					</tr>
				</table>
			</body>
		</html>
    </xsl:template>

	<xsl:template match="weekday">
		<tr>
			<td colspan="4">
				 <b><xsl:value-of select="@name"/></b> 
			</td>
		</tr>
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="subject">
		<tr>
			<td style="text-align: center">
				<xsl:value-of select="time/@start"/> - <xsl:value-of select="time/@finish"/>
			</td>
			<td>
				<xsl:value-of select="name"/>&#32;&#40;<xsl:value-of select="@type"/>&#41;
			</td>
			<td>
				<xsl:value-of select="professor"/>
			</td>
			<td style="text-align: center">
				<xsl:value-of select="room"/>
			</td>
		</tr>
	</xsl:template>
	
</xsl:stylesheet>
