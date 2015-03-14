<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
    <xsl:output encoding="UTF-8" indent="yes" method="xml" standalone="no" omit-xml-declaration="no"/>
    <xsl:decimal-format name="de" decimal-separator=","
                        grouping-separator="." />
    <xsl:decimal-format name="fr" decimal-separator=","
                        grouping-separator=" " />
    <xsl:decimal-format name="ru" decimal-separator=","
                        grouping-separator=" " />
    <xsl:decimal-format name="uk" decimal-separator="."
                        grouping-separator="," />
    <xsl:decimal-format name="us" decimal-separator="."
                        grouping-separator="," />

    <!-- function to format a SQL-Date -->
    <!-- param: SQL-Date yyyy-MM-dd -->
    <!-- result: dd.MM.yyyy -->
    <xsl:template name="formatDate">
        <xsl:param name="sqlDate"/>
        <xsl:if test="$sqlDate and not($sqlDate = '')">
            <xsl:variable name="year" select="substring($sqlDate, 1, 4)"/>
            <xsl:variable name="month" select="substring($sqlDate, 6, 2)"/>
            <xsl:variable name="day" select="substring($sqlDate, 9, 2)"/>
            <xsl:value-of select="$day"/>
            <xsl:value-of select="'.'"/>
            <xsl:value-of select="$month"/>
            <xsl:value-of select="'.'"/>
            <xsl:value-of select="$year"/>
        </xsl:if>
    </xsl:template>

    <!-- XSLT 1.0 doesn't know the functions upper-case() and lower-case() neither XSLT 2.0 do -->
    <!-- workarround by using the common method of case conversion translate() -->
    <!-- stringToLowerCase() -->
    <xsl:template name="stringToLowerCase">
        <xsl:param name="inputString"/>
        <xsl:if test="$inputString and not($inputString = '')">
            <xsl:variable name="lowerCase" select="'abcdefghijklmnopqrstuvwxyzäöüß'"/>
            <xsl:variable name="upperCase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜß'"/>
            <xsl:value-of select="translate($inputString, $upperCase, $lowerCase)"/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="InvoiceContainer/Invoice">
        <!-- SellerParty/Company -->
        <xsl:variable name="sellerPartyCompany" select="SellerParty/Company"/>
        <xsl:variable name="sellerPartyCompanyName" select="$sellerPartyCompany/CompanyName"/>
        <xsl:variable name="sellerPartyRegisteredOffice" select="$sellerPartyCompany/RegisteredOffice"/>
        <xsl:variable name="sellerPartyManager" select="$sellerPartyCompany/Manager"/>
        <xsl:variable name="sellerPartyVatId" select="$sellerPartyCompany/VatId"/>
        <xsl:variable name="sellerPartyTaxNumber" select="$sellerPartyCompany/TaxNumber"/>
        <xsl:variable name="sellerPartyCommercialRegister" select="$sellerPartyCompany/CommercialRegister"/>
        <!-- SellerParty/Address -->
        <xsl:variable name="sellerPartyAdress" select="SellerParty/Address"/>
        <xsl:variable name="sellerPartyAddressAddition" select="$sellerPartyAdress/AddressAddition"/>
        <xsl:variable name="sellerPartyStreet" select="$sellerPartyAdress/Street"/>
        <xsl:variable name="sellerPartyCountryCode" select="$sellerPartyAdress/CountryCode"/>
        <xsl:variable name="sellerPartyCountryName" select="$sellerPartyAdress/CountryName"/>
        <xsl:variable name="sellerPartyStateCode" select="$sellerPartyAdress/StateCode"/>
        <xsl:variable name="sellerPartyStateName" select="$sellerPartyAdress/StateName"/>
        <xsl:variable name="sellerPartyZip" select="$sellerPartyAdress/Zip"/>
        <xsl:variable name="sellerPartyCity" select="$sellerPartyAdress/City"/>
        <!-- SellerParty/ContactData -->
        <xsl:variable name="sellerPartyContactData" select="SellerParty/ContactData"/>
        <xsl:variable name="sellerPartyPhone" select="$sellerPartyContactData/Phone"/>
        <xsl:variable name="sellerPartyFax" select="$sellerPartyContactData/Fax"/>
        <xsl:variable name="sellerPartyMail" select="$sellerPartyContactData/Mail"/>
        <xsl:variable name="sellerPartyWeb" select="$sellerPartyContactData/Web"/>
        <!-- SellerParty/BankAccount -->
        <xsl:variable name="sellerPartyBankAccount" select="SellerParty/BankAccount"/>
        <xsl:variable name="sellerPartyAccountHolder" select="$sellerPartyBankAccount/AccountHolder"/>
        <xsl:variable name="sellerPartyAccountNumber" select="$sellerPartyBankAccount/AccountNumber"/>
        <xsl:variable name="sellerPartyBankCode" select="$sellerPartyBankAccount/BankCode"/>
        <xsl:variable name="sellerPartyBankName" select="$sellerPartyBankAccount/BankName"/>
        <xsl:variable name="sellerPartyIBAN" select="$sellerPartyBankAccount/IBAN"/>
        <xsl:variable name="sellerPartyBIC" select="$sellerPartyBankAccount/BIC"/>
        <xsl:variable name="sellerPartyBankCity" select="$sellerPartyBankAccount/City"/>
        <xsl:variable name="connectorClientId" select="$sellerPartyBankAccount/ConnectorClientId"/>

        <!-- BuyerParty/Company -->
        <xsl:variable name="buyerPartyCompany" select="BuyerParty/Company"/>
        <xsl:variable name="buyerPartyCompanyName" select="$buyerPartyCompany/CompanyName"/>
        <xsl:variable name="buyerPartyVatId" select="$sellerPartyCompany/VatId"/>
        <!-- BuyerParty/Person -->
        <xsl:variable name="buyerPartyPerson" select="BuyerParty/Person"/>
        <xsl:variable name="buyerPartyFirstName" select="$buyerPartyPerson/FirstName"/>
        <xsl:variable name="buyerPartyLastName" select="$buyerPartyPerson/LastName"/>
        <xsl:variable name="buyerPartySalutation" select="$buyerPartyPerson/Salutation"/>
        <xsl:variable name="buyerPartyTitle" select="$buyerPartyPerson/Title"/>
        <xsl:variable name="buyerPartyGenderCode" select="$buyerPartyPerson/GenderCode"/>
        <xsl:variable name="buyerPartyDateOfBirth" select="$buyerPartyPerson/DateOfBirth"/>
        <!-- BuyerParty/Address -->
        <xsl:variable name="buyerPartyAddress" select="BuyerParty/Address"/>
        <xsl:variable name="buyerPartyAddressAddition" select="$buyerPartyAddress/AddressAddition"/>
        <xsl:variable name="buyerPartyStreet" select="$buyerPartyAddress/Street"/>
        <xsl:variable name="buyerPartyCountryCode" select="$buyerPartyAddress/CountryCode"/>
        <xsl:variable name="buyerPartyCountryName" select="$buyerPartyAddress/CountryName"/>
        <xsl:variable name="buyerPartyZip" select="$buyerPartyAddress/Zip"/>
        <xsl:variable name="buyerPartyCity" select="$buyerPartyAddress/City"/>
        <!-- BuyerParty/ContactData -->
        <xsl:variable name="buyerPartyContactData" select="BuyerParty/ContactData"/>
        <xsl:variable name="buyerPartyPhone" select="$buyerPartyContactData/Phone"/>
        <xsl:variable name="buyerPartyMail" select="$buyerPartyContactData/Mail"/>

        <!-- ShipToParty/Company -->
        <xsl:variable name="shipToPartyCompany" select="ShipToParty/Company"/>
        <xsl:variable name="shipToPartyCompanyName" select="$shipToPartyCompany/CompanyName"/>
        <!-- ShipToParty/Person -->
        <xsl:variable name="shipToPartyPerson" select="ShipToParty/Person"/>
        <xsl:variable name="shipToPartyPersonFirstName" select="$shipToPartyPerson/FirstName"/>
        <xsl:variable name="shipToPartyPersonLastName" select="$shipToPartyPerson/LastName"/>
        <!-- ShipToParty/Address -->
        <xsl:variable name="shipToPartyAddress" select="ShipToParty/Address"/>
        <xsl:variable name="shipToPartyStreet" select="$shipToPartyAddress/Street"/>
        <xsl:variable name="shipToPartyCountryCode" select="$shipToPartyAddress/CountryCode"/>
        <xsl:variable name="shipToPartyCountryName" select="$shipToPartyAddress/CountryName"/>
        <xsl:variable name="shipToPartyZip" select="$shipToPartyAddress/Zip"/>
        <xsl:variable name="shipToPartyCity" select="$shipToPartyAddress/City"/>

        <!-- PayeeParty/BankAccount -->
        <xsl:variable name="payeePartyBankAccount" select="PayeeParty/BankAccount"/>
        <xsl:variable name="payeePartyBankAccountAccountHolder" select="$payeePartyBankAccount/AccountHolder"/>
        <xsl:variable name="payeePartyAccountAccountNumber" select="$payeePartyBankAccount/AccountNumber"/>
        <xsl:variable name="payeePartyBankAccountBankCode" select="$payeePartyBankAccount/BankCode"/>
        <xsl:variable name="payeePartyBankAccountBankName" select="$payeePartyBankAccount/BankName"/>
        <xsl:variable name="payeePartyAccountIBAN" select="$payeePartyBankAccount/IBAN"/>
        <xsl:variable name="payeePartyAccountBIC" select="$payeePartyBankAccount/BIC"/>
        <!-- PayeeParty/CreditCard -->
        <xsl:variable name="payeePartyCreditCard" select="PayeeParty/CreditCard"/>
        <xsl:variable name="payeePartyCreditCardAccountHolder" select="$payeePartyCreditCard/AccountHolder"/>
        <xsl:variable name="payeePartyCreditCardCardPan" select="$payeePartyCreditCard/CardPan"/>
        <xsl:variable name="payeePartyCredtiCardExpireYear" select="$payeePartyCreditCard/ExpireYear"/>
        <xsl:variable name="payeePartyCreditCardExpireMonth" select="$payeePartyCreditCard/ExpireMonth"/>

        <!-- PaymentInstructions -->
        <xsl:variable name="paymentTypeCode">
            <!-- CC, ELV, SB, WLT, REC, COD, ... -->
            <xsl:call-template name="stringToLowerCase">
                <xsl:with-param name="inputString" select="PaymentInstructions/PaymentTypeCode" />
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="paymentTypeName" select="PaymentInstructions/PaymentTypeName"/>
        <xsl:variable name="paymentCreditorId" select="PaymentInstructions/CreditorId"/>
        <xsl:variable name="paymentMandateId" select="PaymentInstructions/MandateId"/>
        <xsl:variable name="paymentClearingDate" select="PaymentInstructions/ClearingDate"/>
        <xsl:variable name="paymentLegalNote" select="PaymentInstructions/LegalNote"/>
        <xsl:variable name="paymentReference" select="PaymentInstructions/PaymentReference"/> <!-- Verwendungszeck -->

        <!-- LanguageCode -->
        <xsl:variable name="languageCode" select="LanguageCode"/>
        <!-- PaymentProcessId -->
        <xsl:variable name="paymentProcessId" select="PaymentProcessId"/> <!-- "alt" txid, "neu" PaymentProccessId -->
        <!-- PaymentProcessReference -->
        <xsl:variable name="paymentProcessReference" select="PaymentProcessReference"/> <!-- Händler reference -->

        <!-- InvoiceId/InvoiceReference -->
        <xsl:variable name="invoiceId">
            <xsl:choose>
                <xsl:when test="InvoiceId and not(InvoiceId = '')">
                    <xsl:value-of select="InvoiceId"/>  <!-- invoiceid des H�ndler -->
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="InvoiceReference"/> <!-- die von PAYONE generierte invoiceid -->
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <!-- DebtorReference/DebtorId -->
        <xsl:variable name="debtorReference">
            <xsl:choose>
                <xsl:when test="DebtorReference and not(DebtorReference = '')">
                    <xsl:value-of select="DebtorReference"/> <!-- paygate.user.merchant_userid (customerid) -->
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="DebtorId"/> <!-- paygate.user.userid -->
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <!-- InvoiceType -->
        <xsl:variable name="invoiceType" select="InvoiceType"/> <!-- RG = Rechnung, GT = Gutschrift -->
        <xsl:variable name="invoiceTypeName">
            <xsl:choose>
                <xsl:when test="$invoiceType = 'RG'">
                    <xsl:text>Rechnung</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>Gutschrift</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <!-- InvoiceDate -->
        <xsl:variable name="invoiceDate">
            <xsl:call-template name="formatDate">
                <xsl:with-param name="sqlDate" select="InvoiceDate" />
            </xsl:call-template>
        </xsl:variable>
        <!-- InvoiceDate -->
        <xsl:variable name="dueDate">
            <xsl:call-template name="formatDate">
                <xsl:with-param name="sqlDate" select="DueDate" />
            </xsl:call-template>
        </xsl:variable>
        <!-- DeliveryStartDate -->
        <xsl:variable name="deliveryStartDate">
            <xsl:call-template name="formatDate">
                <xsl:with-param name="sqlDate" select="DeliveryStartDate" />
            </xsl:call-template>
        </xsl:variable>
        <!-- DeliveryEndDate -->
        <xsl:variable name="deliveryEndDate">
            <xsl:call-template name="formatDate">
                <xsl:with-param name="sqlDate" select="DeliveryEndDate" />
            </xsl:call-template>
        </xsl:variable>
        <!-- Currency -->
        <xsl:variable name="currency" select="Currency"/> <!-- EUR, USD, GBP, ... -->
        <!-- GrossAmount -->
        <xsl:variable name="grossAmount" select="GrossAmount"/>
        <!-- NetAmount -->
        <xsl:variable name="netAmount" select="NetAmount"/>
        <!-- TaxAmount -->
        <xsl:variable name="taxAmount" select="TaxAmount"/>

        <!-- Appendix 1-5 -->
        <xsl:variable name="appendix1" select="Appendix1"/>
        <xsl:variable name="appendix2" select="Appendix2"/>
        <xsl:variable name="appendix3" select="Appendix3"/>
        <xsl:variable name="appendix4" select="Appendix4"/>
        <xsl:variable name="appendix5" select="Appendix5"/>

        <!-- fo Document starts here -->
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="first-page" page-width="210mm" page-height="297mm" margin-left="23mm" margin-right="23mm" margin-bottom="3mm" margin-top="5mm">
                    <fo:region-body margin-bottom="20mm" margin-top="44mm"/>
                    <fo:region-before extent="40mm"/>
                    <fo:region-after extent="20mm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="first-page">

                <!-- Header, probably unused because of stationery (letter paper) -->
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block margin-top="11mm">
                        <fo:table table-layout="fixed" width="100%" border-spacing="0mm">
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell>
                                        <fo:block text-align="right">
                                            HEADER, probably unused because of stationery (letter paper)
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                </fo:static-content>

                <!-- Footer, probably unused because of stationery (letter paper) -->
                <fo:static-content flow-name="xsl-region-after" font-family="Helvetica" font-size="7pt">
                    <fo:table table-layout="fixed" width="100%" border-spacing="0pt">
                        <fo:table-column column-width="33%"/>
                        <fo:table-column column-width="34%"/>
                        <fo:table-column column-width="33%"/>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block>
                                        <xsl:value-of select="$sellerPartyCompanyName"/>
                                    </fo:block>
                                    <fo:block>
                                        <xsl:value-of select="$sellerPartyStreet"/>
                                    </fo:block>
                                    <fo:block>
                                        <xsl:value-of select="$sellerPartyCountryName"/>
                                        <xsl:text> </xsl:text>
                                        <xsl:value-of select="$sellerPartyZip"/>
                                        <xsl:text> </xsl:text>
                                        <xsl:value-of select="$sellerPartyCity"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-left="1mm">
                                    <xsl:if test="$sellerPartyPhone and not($sellerPartyPhone = '')">
                                        <fo:block>
                                            Tel.:
                                            <xsl:value-of select="$sellerPartyPhone"/>
                                        </fo:block>
                                    </xsl:if>
                                    <xsl:if test="$sellerPartyFax and not($sellerPartyFax = '')">
                                        <fo:block>
                                            Fax:
                                            <xsl:value-of select="$sellerPartyFax"/>
                                        </fo:block>
                                    </xsl:if>
                                    <xsl:if test="$sellerPartyMail and not($sellerPartyMail = '')">
                                        <fo:block>
                                            E-Mail:
                                            <xsl:value-of select="$sellerPartyMail"/>
                                        </fo:block>
                                    </xsl:if>
                                    <xsl:if test="$sellerPartyWeb and not($sellerPartyWeb = '')">
                                        <fo:block>
                                            <xsl:value-of select="$sellerPartyWeb"/>
                                        </fo:block>
                                    </xsl:if>
                                </fo:table-cell>
                                <fo:table-cell padding-left="1mm">
                                    <xsl:if test="$sellerPartyManager and not($sellerPartyManager = '')">
                                        <fo:block>Geschäftsführer:</fo:block>
                                        <fo:block>
                                            <xsl:value-of select="$sellerPartyManager"/>
                                        </fo:block>
                                    </xsl:if>
                                    <xsl:if test="$sellerPartyRegisteredOffice and not($sellerPartyRegisteredOffice = '')">
                                        <fo:block>
                                            Firmensitz:
                                            <xsl:value-of select="$sellerPartyRegisteredOffice"/>
                                        </fo:block>
                                    </xsl:if>
                                    <xsl:if test="$sellerPartyCommercialRegister and not($sellerPartyCommercialRegister = '')">
                                        <fo:block>
                                            <xsl:value-of select="$sellerPartyCommercialRegister"/>
                                        </fo:block>
                                    </xsl:if>
                                    <xsl:if test="$sellerPartyVatId and not($sellerPartyVatId = '')">
                                        <fo:block>
                                            USt.-ID:
                                            <xsl:value-of select="$sellerPartyVatId"/>
                                        </fo:block>
                                    </xsl:if>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    <fo:block>
                        FOOTER, probably unused because of stationery (letter paper)
                    </fo:block>
                </fo:static-content>

                <!-- body -->
                <fo:flow flow-name="xsl-region-body" font-family="Helvetica" font-size="10pt">
                    <fo:block>
                        <fo:table table-layout="fixed" width="100%" border-spacing="0mm">
                            <fo:table-column column-width="60%"/>
                            <fo:table-column column-width="40%"/>
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell>
                                        <fo:block space-after="12mm" font-size="6pt">
                                            <xsl:value-of select="$sellerPartyCompanyName"/> &#183;
                                            <xsl:value-of select="$sellerPartyStreet"/> &#183;
                                            <xsl:value-of select="$sellerPartyCountryCode"/>
                                            <xsl:text> </xsl:text>
                                            <xsl:value-of select="$sellerPartyZip"/>
                                            <xsl:text> </xsl:text><xsl:value-of select="$sellerPartyCity"/>
                                        </fo:block>
                                        <fo:block>
                                            <xsl:value-of select="$buyerPartyCompanyName"/>
                                        </fo:block>
                                        <fo:block>
                                            <xsl:value-of select="$buyerPartySalutation"/>
                                            <xsl:text> </xsl:text>
                                            <xsl:value-of select="$buyerPartyTitle"/>
                                            <xsl:text> </xsl:text>
                                            <xsl:value-of select="$buyerPartyFirstName"/>
                                            <xsl:text> </xsl:text>
                                            <xsl:value-of select="$buyerPartyLastName"/>
                                        </fo:block>
                                        <fo:block>
                                            <xsl:value-of select="$buyerPartyAddressAddition"/>
                                        </fo:block>
                                        <fo:block>
                                            <xsl:value-of select="$buyerPartyStreet"/>
                                        </fo:block>
                                        <fo:block>
                                            <xsl:value-of select="$buyerPartyZip"/>
                                            <xsl:text> </xsl:text>
                                            <xsl:value-of select="$buyerPartyCity"/>
                                        </fo:block>
                                        <fo:block>
                                            <xsl:value-of select="$buyerPartyCountryName"/>
                                        </fo:block>
                                    </fo:table-cell>

                                    <fo:table-cell>
                                        <fo:table table-layout="fixed" width="100%" border-spacing="0pt" margin-top="14mm">
                                            <fo:table-column column-width="55%"/>
                                            <fo:table-column column-width="45%"/>
                                            <fo:table-body>
                                                <fo:table-row>
                                                    <fo:table-cell>
                                                        <fo:block>Datum:</fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell>
                                                        <fo:block>
                                                            <xsl:value-of select="$invoiceDate"/>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                                <fo:table-row>
                                                    <fo:table-cell>
                                                        <fo:block>Kunden-Nr.:</fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell>
                                                        <fo:block>
                                                            <xsl:value-of select="$debtorReference"/>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                                <fo:table-row>
                                                    <fo:table-cell>
                                                        <fo:block><xsl:value-of select="$invoiceTypeName"/>s-Nr.:
                                                        </fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell>
                                                        <fo:block>
                                                            <xsl:value-of select="$invoiceId"/>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                                <fo:table-row>
                                                    <fo:table-cell>
                                                        <fo:block>
                                                            <xsl:choose>
                                                                <xsl:when test="($deliveryEndDate and not($deliveryEndDate = '')) and ($deliveryStartDate and not($deliveryStartDate = ''))">
                                                                    Abrechnungszeitraum:
                                                                </xsl:when>
                                                                <xsl:when test="$deliveryStartDate and not($deliveryStartDate = '')">
                                                                    Lieferdatum:
                                                                </xsl:when>
                                                            </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell>
                                                        <fo:block>
                                                            <xsl:choose>
                                                                <xsl:when test="($deliveryEndDate and not($deliveryEndDate = '')) and ($deliveryStartDate and not($deliveryStartDate = ''))">
                                                                    <xsl:value-of select="$deliveryStartDate"/>
                                                                    <xsl:text> - </xsl:text>
                                                                    <xsl:value-of select="$deliveryEndDate"/>
                                                                </xsl:when>
                                                                <xsl:when test="$deliveryStartDate and not($deliveryStartDate = '')">
                                                                    <xsl:value-of select="$deliveryStartDate"/>
                                                                </xsl:when>
                                                            </xsl:choose>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                                <fo:table-row>
                                                    <fo:table-cell>
                                                        <fo:block>Seite:</fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell>
                                                        <fo:block>
                                                            <fo:page-number/>
                                                            von
                                                            <fo:page-number-citation ref-id="terminator"/>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>

                        <fo:block space-before="20mm" font-weight="bold" font-size="12pt">
                            <xsl:value-of select="$invoiceTypeName"/>
                        </fo:block>

                        <!-- InvoiceItems -->
                        <fo:block space-before="12mm">
                            <fo:table table-layout="fixed" width="100%" border-spacing="0pt" padding="3pt">
                                <!-- 210mm total width, 23mm border left and right -->
                                <!-- 210 - 46 = 164mm remaining width -->
                                <fo:table-column column-number="1" column-width="20mm"/>
                                <fo:table-column column-number="2" column-width="54mm"/>
                                <fo:table-column column-number="3" column-width="15mm"/>
                                <fo:table-column column-number="4" column-width="25mm"/>
                                <fo:table-column column-number="5" column-width="20mm"/>
                                <fo:table-column column-number="6" column-width="30mm"/>
                                <fo:table-header>
                                    <fo:table-row font-size="8pt">
                                        <fo:table-cell padding="3pt" border-bottom="0.2pt solid black">
                                            <fo:block>Artikelnr.</fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3pt" border-bottom="0.2pt solid black">
                                            <fo:block>Bezeichnung</fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3pt" border-bottom="0.2pt solid black" text-align="right">
                                            <fo:block>Menge</fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3pt" border-bottom="0.2pt solid black" text-align="right">
                                            <fo:block>Einzelpreis (netto)</fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3pt" border-bottom="0.2pt solid black" text-align="right">
                                            <fo:block>MwSt</fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3pt" border-bottom="0.2pt solid black" text-align="right">
                                            <fo:block>Summe (brutto)</fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </fo:table-header>
                                <fo:table-body>
                                    <xsl:for-each select="InvoiceItems/InvoiceItem">
                                        <fo:table-row>
                                            <fo:table-cell padding="3pt" border-bottom="0.2pt solid black">
                                                <fo:block>
                                                    <xsl:if test="ArticleNumber and not(ArticleNumber = '')">
                                                        <xsl:value-of select="ArticleNumber"/>
                                                    </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding="3pt" border-bottom="0.2pt solid black">
                                                <fo:block hyphenate="true" text-align="justify" language="de"> <!-- FIXME RF@ALL should be hyphenate="true" hyphenation-character="" to break long strings if nessesary -->
                                                    <xsl:if test="Description1 and not(Description1 = '')">
                                                        <xsl:value-of select="Description1"/>
                                                    </xsl:if>
                                                </fo:block>
                                                <fo:block hyphenate="true" text-align="justify" language="de"> <!-- FIXME RF@ALL should be hyphenate="true" hyphenation-character="" to break long strings if nessesary -->
                                                    <xsl:if test="Description2 and not(Description2 = '')">
                                                        <xsl:value-of select="Description2"/>
                                                    </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding="3pt" border-bottom="0.2pt solid black" text-align="right">
                                                <fo:block>
                                                    <xsl:if test="Quantity and not(Quantity = '')">
                                                        <xsl:value-of select="Quantity"/>
                                                    </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding="3pt" border-bottom="0.2pt solid black" text-align="right">
                                                <fo:block>
                                                    <xsl:if test="SingleNetAmount and not(SingleNetAmount = '')">
                                                        <xsl:value-of select='format-number(SingleNetAmount, "#.##0,00","de")'/>
                                                    </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding="3pt" border-bottom="0.2pt solid black" text-align="right">
                                                <fo:block>
                                                    <xsl:if test="TaxRate and not(TaxRate = '')">
                                                        <xsl:value-of select='format-number(TaxRate, "#.##0","de")'/> %
                                                    </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding="3pt" border-bottom="0.2pt solid black" text-align="right">
                                                <fo:block>
                                                    <xsl:if test="not(GrossAmount = '')">
                                                        <xsl:value-of select='format-number(GrossAmount, "#.##0,00","de")'/>
                                                        <xsl:text> </xsl:text>
                                                        <xsl:value-of select="$currency"/>
                                                    </xsl:if>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </xsl:for-each>

                                    <fo:table-row>
                                        <fo:table-cell padding="3pt" number-columns-spanned="5" text-align="right">
                                            <fo:block>Summe (netto)</fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3pt" text-align="right">
                                            <fo:block>
                                                <xsl:value-of select='format-number($netAmount, "#.##0,00","de")'/>
                                                <xsl:text> </xsl:text>
                                                <xsl:value-of select="$currency"/>
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>

                                    <xsl:for-each select="InvoiceTaxItems/InvoiceTaxItem">
                                        <fo:table-row>
                                            <fo:table-cell padding="3pt" number-columns-spanned="5" text-align="right">
                                                <fo:block>
                                                    MwSt (<xsl:value-of select='format-number(TaxRate, "#.##0","de")'/>%)
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding="3pt" text-align="right">
                                                <fo:block>
                                                    <xsl:value-of select='format-number(TaxAmount, "#.##0,00","de")'/>
                                                    <xsl:text> </xsl:text>
                                                    <xsl:value-of select="$currency"/>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </xsl:for-each>

                                    <fo:table-row>
                                        <fo:table-cell padding="3pt" number-columns-spanned="5" text-align="right">
                                            <fo:block>Summe (brutto)</fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell padding="3pt" text-align="right">
                                            <fo:block>
                                                <xsl:value-of select='format-number($grossAmount, "#.##0,00","de")'/>
                                                <xsl:text> </xsl:text>
                                                <xsl:value-of select="$currency"/>
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>

                                </fo:table-body>
                            </fo:table>
                        </fo:block>

                        <!-- PaymentInstructions -->
                        <fo:block space-before="10mm">
                            <xsl:choose>
                                <xsl:when test="$invoiceType = 'RG'">
                                    <xsl:choose>
                                        <xsl:when test="$paymentTypeCode = 'rec'">
                                            <fo:table keep-together.within-page="always" table-layout="fixed" width="100%" text-align="left" border-spacing="0pt" padding="1pt">
                                                <fo:table-column column-number="1" column-width="25%"/>
                                                <fo:table-column column-number="2" column-width="75%"/>
                                                <fo:table-body>
                                                    <xsl:if test="$connectorClientId and ($connectorClientId = 2053)">
                                                        <fo:table-row>
                                                            <fo:table-cell number-columns-spanned="2">
                                                                <fo:block margin-bottom="2mm">
                                                                    Die Zahlungsabwicklung erfolgt über ein Konto der PAYONE GmbH.
                                                                </fo:block>
                                                            </fo:table-cell>
                                                        </fo:table-row>
                                                    </xsl:if>
                                                    <fo:table-row>
                                                        <fo:table-cell number-columns-spanned="2">
                                                            <fo:block margin-bottom="2mm">
                                                                Bitte überweisen Sie den fälligen Gesamtbetrag sofort nach Rechnungseingang auf folgendes Konto:
                                                            </fo:block>
                                                        </fo:table-cell>
                                                    </fo:table-row>
                                                    <fo:table-row>
                                                        <fo:table-cell>
                                                            <fo:block>Zahlungsempfänger</fo:block>
                                                        </fo:table-cell>
                                                        <fo:table-cell>
                                                            <fo:block>
                                                                <xsl:value-of select="$sellerPartyAccountHolder"/>
                                                            </fo:block>
                                                        </fo:table-cell>
                                                    </fo:table-row>
                                                    <fo:table-row>
                                                        <fo:table-cell>
                                                            <fo:block>Kontonummer</fo:block>
                                                        </fo:table-cell>
                                                        <fo:table-cell>
                                                            <fo:block>
                                                                <xsl:value-of select="$sellerPartyAccountNumber"/>
                                                            </fo:block>
                                                        </fo:table-cell>
                                                    </fo:table-row>
                                                    <fo:table-row>
                                                        <fo:table-cell>
                                                            <fo:block>Bankleitzahl</fo:block>
                                                        </fo:table-cell>
                                                        <fo:table-cell>
                                                            <fo:block>
                                                                <xsl:value-of select="$sellerPartyBankCode"/>
                                                            </fo:block>
                                                        </fo:table-cell>
                                                    </fo:table-row>
                                                    <fo:table-row>
                                                        <fo:table-cell>
                                                            <fo:block>Bank</fo:block>
                                                        </fo:table-cell>
                                                        <fo:table-cell>
                                                            <fo:block>
                                                                <xsl:value-of select="$sellerPartyBankName"/> (<xsl:value-of select="$sellerPartyBankCity"/>)
                                                            </fo:block>
                                                        </fo:table-cell>
                                                    </fo:table-row>
                                                    <fo:table-row>
                                                        <fo:table-cell>
                                                            <fo:block>IBAN</fo:block>
                                                        </fo:table-cell>
                                                        <fo:table-cell>
                                                            <fo:block>
                                                                <xsl:value-of select="$sellerPartyIBAN"/>
                                                            </fo:block>
                                                        </fo:table-cell>
                                                    </fo:table-row>
                                                    <fo:table-row>
                                                        <fo:table-cell>
                                                            <fo:block>BIC</fo:block>
                                                        </fo:table-cell>
                                                        <fo:table-cell>
                                                            <fo:block>
                                                                <xsl:value-of select="$sellerPartyBIC"/>
                                                            </fo:block>
                                                        </fo:table-cell>
                                                    </fo:table-row>
                                                    <fo:table-row>
                                                        <fo:table-cell>
                                                            <fo:block margin-bottom="2mm" font-weight="bold">Verwendungszweck</fo:block>
                                                        </fo:table-cell>
                                                        <fo:table-cell>
                                                            <fo:block margin-bottom="2mm" font-weight="bold">
                                                                <xsl:value-of select="$paymentReference"/>
                                                            </fo:block>
                                                        </fo:table-cell>
                                                    </fo:table-row>
                                                    <fo:table-row>
                                                        <fo:table-cell number-columns-spanned="2">
                                                            <fo:block>
                                                                <fo:inline font-weight="bold">Achtung: </fo:inline>
                                                                Wir können nur Überweisungen mit korrektem Verwendungszweck berücksichtigen!
                                                            </fo:block>
                                                        </fo:table-cell>
                                                    </fo:table-row>
                                                </fo:table-body>
                                            </fo:table>
                                        </xsl:when>
                                        <xsl:when test="$paymentTypeCode = 'elv'">
                                            <xsl:variable name="sepaPreNotifyText">
                                                <xsl:if test="$paymentCreditorId">
                                                    <xsl:if test="$paymentMandateId">
                                                        <xsl:text>der Mandatsreferenz </xsl:text>
                                                        <xsl:value-of select="$paymentMandateId"/>
                                                        <xsl:text> und </xsl:text>
                                                    </xsl:if>
                                                    <xsl:text>der Gläubiger-ID </xsl:text>
                                                    <xsl:value-of select="$paymentCreditorId"/>
                                                    <xsl:text> </xsl:text>
                                                </xsl:if>
                                            </xsl:variable>

                                            <xsl:variable name="bankAccountData">
                                                <xsl:if test="$payeePartyAccountAccountNumber and not($payeePartyAccountAccountNumber = '')">
                                                    <xsl:text>Kontonr.: </xsl:text>
                                                    <xsl:value-of select="$payeePartyAccountAccountNumber"/>
                                                    <xsl:text> </xsl:text>
                                                </xsl:if>

                                                <xsl:if test="$payeePartyAccountIBAN and not($payeePartyAccountIBAN = '')">
                                                    <xsl:text>IBAN: </xsl:text>
                                                    <xsl:value-of select="$payeePartyAccountIBAN"/>
                                                    <xsl:text> </xsl:text>
                                                    <xsl:if test="$payeePartyAccountBIC and not($payeePartyAccountBIC = '')">
                                                        <xsl:text> BIC: </xsl:text>
                                                        <xsl:value-of select="$payeePartyAccountBIC"/>
                                                        <xsl:text> </xsl:text>
                                                    </xsl:if>
                                                </xsl:if>
                                            </xsl:variable>

                                            <xsl:variable name="clearingDate">
                                                <xsl:if test="$paymentClearingDate and not($paymentClearingDate = '0000-00-00')">
                                                    <xsl:text>am </xsl:text>
                                                    <xsl:call-template name="formatDate">
                                                        <xsl:with-param name="sqlDate" select="$paymentClearingDate" />
                                                    </xsl:call-template>
                                                </xsl:if>
                                            </xsl:variable>

                                            Der Rechnungsbetrag wird <xsl:value-of select="$sepaPreNotifyText"/> von Ihrem Konto <xsl:value-of select="$bankAccountData"/> per Lastschrift <xsl:value-of select="$clearingDate"/> abgebucht.
                                        </xsl:when>
                                        <xsl:when test="$paymentTypeCode = 'vor'">
                                            Der Rechnungsbetrag ist bereits per Überweisung eingegangen.
                                        </xsl:when>
                                        <xsl:when test="$paymentTypeCode = 'cc'">
                                            Der Rechnungsbetrag wird von Ihrer Kreditkarte abgebucht.
                                        </xsl:when>
                                        <xsl:when test="$paymentTypeCode = 'sb'">
                                            Der Rechnungsbetrag ist per Online Überweisung eingegangen.
                                        </xsl:when>
                                        <xsl:when test="$paymentTypeCode = 'wlt'">
                                            Der Rechnungsbetrag ist per Paypal eingegangen.
                                        </xsl:when>
                                        <xsl:when test="$paymentTypeCode = 'cod'">
                                            Der Rechnungsbetrag wurde per Nachnahme beglichen.
                                        </xsl:when>
                                        <xsl:when test="$paymentTypeCode = 'chq'">
                                            Der Rechnungsbetrag ist per Scheck eingegangen.
                                        </xsl:when>
                                        <xsl:when test="$paymentTypeCode = 'fnc'">
                                            Details zur Zahlungsabwicklung entnehmen Sie bitte Ihren Kreditunterlagen.
                                        </xsl:when>
                                    </xsl:choose>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:choose>
                                        <xsl:when test="$paymentTypeCode = 'cc'">
                                            Der offene Betrag wurde Ihrer Kreditkarte gutgeschrieben.
                                        </xsl:when>
                                        <xsl:when test="$paymentTypeCode = 'wlt'">
                                            Der offene Betrag wurde Ihrem Paypal Konto gutgeschrieben.
                                        </xsl:when>
                                        <xsl:otherwise>
                                            Der offene Betrag wurde Ihrem Konto gutgeschrieben.
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:otherwise>
                            </xsl:choose>
                        </fo:block>

                        <xsl:if test="$appendix1 and not($appendix1 = '')">
                            <fo:block space-before="15mm">
                                <xsl:value-of select="$appendix1"/>
                            </fo:block>
                        </xsl:if>

                        <xsl:if test="$appendix2 and not($appendix2 = '')">
                            <fo:block space-before="5mm">
                                <xsl:value-of select="$appendix2"/>
                            </fo:block>
                        </xsl:if>

                        <xsl:if test="$appendix3 and not($appendix3 = '')">
                            <fo:block space-before="5mm">
                                <xsl:value-of select="$appendix3"/>
                            </fo:block>
                        </xsl:if>

                        <xsl:if test="$appendix4 and not($appendix4 = '')">
                            <fo:block space-before="5mm">
                                <xsl:value-of select="$appendix4"/>
                            </fo:block>
                        </xsl:if>

                        <xsl:if test="$appendix5 and not($appendix5 = '')">
                            <fo:block space-before="5mm">
                                <xsl:value-of select="$appendix5"/>
                            </fo:block>
                        </xsl:if>
                    </fo:block>
                    <fo:block id="terminator"/>
                </fo:flow>
            </fo:page-sequence>

        </fo:root>
    </xsl:template>
</xsl:stylesheet>