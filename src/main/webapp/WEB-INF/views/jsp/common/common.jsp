<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>

    //noinspection JSUnusedLocalSymbols
    function insertCellInRow(index, row, name) {
        row.insertCell(index).innerHTML = name;
    }

    //noinspection JSUnusedLocalSymbols
    function afterErrorPageCheckResult(xhr) {
        if (xhr.status !== 200) {
            document.getElementById("info_result").innerHTML = "Ошибка";
        }
    }

    //noinspection JSUnusedLocalSymbols
    function afterLoadingPageCheckResult(xhr, reloadParentsPage) {
        if (xhr.readyState === 4) {
            let el = document.createElement('html');
            el.innerHTML = xhr.responseText;
            if (el.getElementsByTagName('div') !== null && el.getElementsByTagName('div').length !== 0) {
                let newInfoResult = el.getElementsByTagName('div').namedItem('info_result').innerHTML;
                if (!((newInfoResult === null) || (newInfoResult === ''))) {
                    document.getElementById("info_result").innerHTML = newInfoResult;
                }
            }
        }
    }

    //noinspection JSUnusedLocalSymbols
    function afterLoadingPageProcessJson(xhr) {
        let result = null;

        if (xhr.readyState === 4) {
            result = JSON.parse(xhr.response);
            if ('infoResult' in result) {
                document.getElementById("info_result").innerHTML = result.infoResult;
            }
        }

        return result;
    }

    //noinspection JSUnusedLocalSymbols
    function getHtmlBlackoutAndLoading() {
        return "<div style='background: #000;top: 0; left: 0; position:fixed; height: 100%;width: 100%;opacity: 0.5; z-index: 9990;'></div>" +
            "<img  style='position:absolute; left:40%; top:40%;' src='${pageContext.request.contextPath}/resources/images/gif/loading.gif' >";
    }

</script>