<?xml version="1.0" encoding="GBK"?>
<xsl:transform version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:template match="/">
		<fo:root>
			<fo:layout-master-set>
				<!-- A3纵向 -->
				<fo:simple-page-master master-name="A3-portrait"
					page-height="42.0cm" page-width="28.5cm" margin="1cm">
					<fo:region-body margin-top="1cm" margin-bottom="3cm" />
					<fo:region-after extent="2cm" region-name="xsl-region-after"/>
				</fo:simple-page-master>
				<!-- A3横向 -->
				<fo:simple-page-master master-name="A3-landscape"
					page-height="28.5cm" page-width="42.0cm" margin="1cm">
					<fo:region-body margin-top="1cm" margin-bottom="3cm" />
					<fo:region-after extent="2cm" region-name="xsl-region-after"/>
				</fo:simple-page-master>
				<!-- A4纵向 -->
				<fo:simple-page-master master-name="A4-portrait"
					page-height="29.7cm" page-width="21.0cm" margin="1cm">
					<fo:region-body margin-top="1cm" margin-bottom="3cm" />
					<fo:region-after extent="2cm" region-name="xsl-region-after"/>
				</fo:simple-page-master>
				<!-- A4横向（默认） -->
				<fo:simple-page-master master-name="A4-landscape"
					page-height="21.0cm" page-width="29.7cm" margin="1cm">
					<fo:region-body margin-top="1cm" margin-bottom="3cm" />
					<fo:region-after extent="2cm" region-name="xsl-region-after"/>
				</fo:simple-page-master>
				<!-- A5纵向 -->
				<fo:simple-page-master master-name="A5-portrait"
					page-height="21.0cm" page-width="14.8cm" margin="1cm">
					<fo:region-body margin-top="1cm" margin-bottom="3cm" />
					<fo:region-after extent="2cm" region-name="xsl-region-after"/>
				</fo:simple-page-master>
				<!-- A5横向 -->
				<fo:simple-page-master master-name="A5-landscape"
					page-height="14.8cm" page-width="21.0cm" margin="1cm">
					<fo:region-body margin-top="1cm" margin-bottom="3cm" />
					<fo:region-after extent="2cm" region-name="xsl-region-after"/>
				</fo:simple-page-master>
				<!-- 241 -->
				<fo:simple-page-master master-name="241-1"
					page-height="28.0cm" page-width="24.1cm" margin="1cm">
					<fo:region-body margin-top="1cm" margin-bottom="3cm" />
					<fo:region-after extent="2cm" region-name="xsl-region-after"/>
				</fo:simple-page-master>
				<!-- 381 -->
				<fo:simple-page-master master-name="381-1"
					page-height="28.0cm" page-width="38.1cm" margin="1cm">
					<fo:region-body margin-top="1cm" margin-bottom="3cm" />
					<fo:region-after extent="2cm" region-name="xsl-region-after"/>
				</fo:simple-page-master>
			</fo:layout-master-set>

			<fo:page-sequence id="sequence" master-reference="A4-landscape" initial-page-number="1">
				<fo:static-content flow-name="xsl-region-after">
					<fo:block text-align="center" font-size="9pt" font-family="SimSun">
						<fo:retrieve-marker retrieve-class-name="np-number"/>
						<fo:leader rule-thickness="0pt"/>
						第 <fo:page-number format="1"/> 页，共 <fo:page-number-citation ref-id="lastPage"/> 页
					</fo:block>
				</fo:static-content>
				<fo:flow flow-name="xsl-region-body">
					<fo:block font-family="SimSun" font-size="24pt" text-align="center" margin-bottom="10px">
						<xsl:value-of select="source/title" />
					</fo:block>
					<fo:block font-family="SimSun" font-size="9pt" text-align="right" margin-bottom="2px">
					</fo:block>
					<fo:table table-layout="fixed" width="100%"></fo:table>
					<fo:block id="lastPage" />
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:transform>