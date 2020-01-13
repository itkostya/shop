<%@page import="com.eva.web.tools.PageContainer" %>

<%--@elvariable id="infoResult" type="java.lang.String"--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>

<head>
    <title>Create products and check results</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="../../../resources/css/common/common.css"/>" rel="stylesheet" type="text/css">
    <jsp:include page="../../views/jsp/common/common.jsp"/>
    <jsp:useBean id="now" class="java.util.Date"/>
</head>

<body>

<script>

    function createElements() {
        const formData = new FormData(document.forms["formCreateElements"]);
        const xhr = new XMLHttpRequest();

        xhr.open("POST", "${pageContext.request.contextPath}${PageContainer.CREATE_CHECK_PAGE}${PageContainer.CREATE}", true);
        xhr.send(formData);

        xhr.onerror = function () {
            afterErrorPageCheckResult(xhr);
        };
        xhr.onload = function () {
            afterLoadingPageCheckResult(xhr, false);
        };

        let button = document.getElementById("info_result");
        button.innerHTML = "Creation..." + getHtmlBlackoutAndLoading();
        button.disabled = true;
    }

    function refreshProductTable(current_table, responseMap) {
        let body, row;

        // Delete head, body
        current_table.deleteTHead();
        while (current_table.hasChildNodes()) {
            current_table.removeChild(current_table.lastChild);
        }

        if (!('productTable' in responseMap) && !('productColumns' in responseMap)) {
            return;
        }

        let productTable = responseMap.productTable;
        let productColumns = responseMap.productColumns;

        // Create head
        row = current_table.createTHead().insertRow(0);
        for (let j = 0; j < productColumns.length; j++) {
            insertCellInRow(j, row, '' + productColumns[j])
        }

        // Create body
        body = current_table.appendChild(document.createElement('tbody'));
        for (let i = 0; i < productTable.length; i++) {
            row = body.insertRow(i);
            for (let j = 0; j < productColumns.length; j++) {
                insertCellInRow(j, row, '' + productTable[i][productColumns[j]])
            }
            row.style.cssText = ((i % 2) === 0 ? " background: rgb(255, 248, 234); " : "");
        }
    }

    function checkSingleExpression(regExpValue) {
        const formData = new FormData(document.forms["checkRegularExpression"]);
        const xhr = new XMLHttpRequest();

        xhr.open("POST", "${pageContext.request.contextPath}${PageContainer.SHOP_PAGE}${PageContainer.SHOP_PRODUCT_SINGLE_PAGE}?${PageContainer.PRODUCT_REGULAR_EXPRESSION}=" + encodeURIComponent(regExpValue), true);
        xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
        xhr.send(formData);

        xhr.onerror = function () {
            afterErrorPageCheckResult(xhr);
        };
        xhr.onload = function () {
            let responseMap = afterLoadingPageProcessJson(xhr);
            refreshProductTable(document.getElementById("tableProductsId"), responseMap);
        };

        let button = document.getElementById("info_result");
        button.innerHTML = "Checking single regular expression..." + getHtmlBlackoutAndLoading();
        button.disabled = true;

    }

    async function checkMultithreading() {
        let maxThreads = document.getElementById("numberOfThreadsId").valueAsNumber;
        let array = Array(...Array(maxThreads)).map((_, i) => i);  // array [0, ..., maxThreads - 1]

        // map array to promises
        const promises = array.map(runThread);
        // wait until all promises are resolved
        await Promise.all(promises);
        console.log('Done checkMultithreading()!')
    }

    async function runThread(currentNumber) {
        let numberOfQueries = document.getElementById("numberOfQueryInThreadId").value;
        let generator = generateSequence(1, numberOfQueries);
        for await (let value of generator) {
            checkMultiThreadExpression(generateRandomRegExp(), currentNumber, value)
        }
    }

    async function* generateSequence(start, end) {
        for (let i = start; i <= end; i++) {
            await new Promise(resolve => setTimeout(resolve, 1000));
            yield i;
        }
    }

    function generateRandomRegExp() {
        return "^.*[" + getRandomNumber(0, 1000).toString() + "].*$"
    }

    function getRandomNumber(min, max) {
        return Math.floor(Math.random() * (max - min)) + min;
    }

    async function checkMultiThreadExpression(regExpValue, threadNumber, queryNumber) {
        const formData = new FormData(document.forms["checkRegularExpression"]);
        const xhr = new XMLHttpRequest();

        formData.append("threadNumber", threadNumber);
        formData.append("queryNumber", queryNumber);

        xhr.open("POST", "${pageContext.request.contextPath}${PageContainer.SHOP_PAGE}${PageContainer.SHOP_PRODUCT_MULTI_PAGE}?${PageContainer.PRODUCT_REGULAR_EXPRESSION}=" + encodeURIComponent(regExpValue), true);
        xhr.send(formData);

        xhr.onerror = function () {
            afterErrorPageCheckResult(xhr);
        };
        xhr.onload = function () {
            afterLoadingPageCheckResult(xhr, false);
        };
    }
</script>

<form method="post" name="formCreateElements">
    <input type="number" name="numberOfElements" title="Enter number of products for creation" value="1000000">
    <div class="div-like-button" onclick="createElements();" id="command-create">Create products</div>
</form>

<form method="post" name="checkRegularExpression">
    <input type="text" name="regularExpression" id="regularExpressionId" title="Check regular expression"
           value="^.*[12345678].*$">
    <div class="div-like-button" onclick="checkSingleExpression(document.getElementById('regularExpressionId').value)"
         id="command-check">Check regular expression
    </div>
    <div>--------------------------------------------------------</div>
    <div>Multithreading</div>
    <input type="number" name="numberOfThreads" id="numberOfThreadsId" title="Number of threads" value="10">
    <input type="number" name="numberOfQueryInThread" id="numberOfQueryInThreadId" title="Number of query in thread"
           value="30">
    <div class="div-like-button" onclick="checkMultithreading();" id="checkMultithreadingId">Check multithreading</div>
</form>

<div id="info_result">${infoResult}</div>

<div style="height:80%;" id="div-for-main-table">
    <div class="table-wrapper">
        <div class="table-scroll-products">
            <table class="table-products" id="tableProductsId"></table>
        </div>
    </div>
</div>

</body>
</html>