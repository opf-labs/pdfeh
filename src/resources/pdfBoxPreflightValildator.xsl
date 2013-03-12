<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0">
    <xsl:output method="xml" encoding="utf-8" indent="yes"/>
    <xsl:variable name="POLICY" select="'PdfErrorPolicy.xml'"/>
    <xsl:variable name="ERRORCODES" select="document($POLICY)/PdfErrors"/>
  
    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>
      
    <xsl:template match="preflight">
        <preflight>
            <xsl:apply-templates select="./@name"/>
            <xsl:apply-templates select="./executionTimeMS"/>
            <xsl:choose>
                <xsl:when test="./epicFail">
                    <xsl:apply-templates select="./epicFail"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="getReportableErrors">
                        <xsl:with-param name="errors" select="./error"/>
                    </xsl:call-template>
                    <xsl:call-template name="getReportableWarnings">
                        <xsl:with-param name="errors"  select="./error"/>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>            
        </preflight>
    </xsl:template>
    
    <xsl:template match="error" mode="error">
        <error>
            <xsl:attribute name="code"><xsl:apply-templates select="./code"/></xsl:attribute>
            <xsl:if test="./count">
                <xsl:attribute name="occurrences"><xsl:apply-templates select="./count"/></xsl:attribute>
            </xsl:if>
            <message><xsl:apply-templates select="./details"/></message>
        </error>
    </xsl:template>
    
    <xsl:template match="error" mode="warning">
        <warning>
            <xsl:attribute name="code"><xsl:apply-templates select="./code"/></xsl:attribute>
            <xsl:if test="./count">
                <xsl:attribute name="occurrences"><xsl:apply-templates select="./count"/></xsl:attribute>
            </xsl:if>
            <message><xsl:apply-templates select="./details"/></message>
        </warning>
    </xsl:template>
    
    <xsl:template match="code">
        <xsl:apply-templates/>  
    </xsl:template>
    
    <xsl:template match="details">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="@name">
        <fileName>
            <xsl:value-of select="."/>
        </fileName>
    </xsl:template>
    
    <xsl:template match="executionTimeMS">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="epicFail">
        <epicFail>This PDF file could not even be parsed by PDFBox.  It is at serious risk for long-term use.  If you want to see more details of the parse error, you can look at PDFBox Preflight output file.</epicFail>
    </xsl:template>
    
    <xsl:template name="getReportableErrors">
        <xsl:param name="errors"/>
        <errors>
        <xsl:for-each select="$errors">
            <xsl:variable name="theError" select="."/>
            <xsl:variable name="code" select="$theError/code/text()"/>
            <xsl:for-each select="$ERRORCODES/PdfError">
                <xsl:if test="./@code=$code and ./@shouldFail='true'">
                    <xsl:apply-templates select="$theError" mode="error"/>
                </xsl:if>
            </xsl:for-each>           
        </xsl:for-each>
        </errors>
    </xsl:template>
    
    <xsl:template name="getReportableWarnings">
        <xsl:param name="errors"/>
        <warnings>
        <xsl:for-each select="$errors">
            <xsl:variable name="theError" select="."/>
            <xsl:variable name="code" select="./code/text()"/>
            <xsl:for-each select="$ERRORCODES/PdfError">
                <xsl:if test="./@code=$code and ./@shouldFail='false' and ./@shouldWarn='true'">
                    <xsl:apply-templates select="$theError" mode="warning"/>
                </xsl:if>
            </xsl:for-each>           
        </xsl:for-each>
        </warnings>
    </xsl:template>
    
</xsl:stylesheet>