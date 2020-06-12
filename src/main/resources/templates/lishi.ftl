<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head lang="en">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title></title>
</head>
<body style="margin: 0;padding: 0">
<table style="text-align: center;width: 575px;font-weight:800;border-spacing: 0px;padding: 0;margin: 0">

    <tr style="color: blue;">
        <td style="font-size: 22px">Event</td>
        <td style="font-size: 22px">Time</td>
        <td style="font-size: 22px">Numbers</td>
        <td style="font-size: 22px">Sum</td>
        <td style="font-size: 22px">Result</td>
    </tr>

<#--    <#list lishis as lishi>-->
<#--        ${lishi.result}-->
<#--    </#list>-->
    <#list lishis as lishi>
        <tr style="font-family:微软雅黑">
            <td style="color: cadetblue; font-size: 22px">${lishi.event}</td>
            <td style="color: cadetblue; font-size: 22px">${lishi.time}</td>
            <td style="color: #e8517b; font-size: 22px">${lishi.numbers}</td>
            <td style="color: crimson; font-size: 22px">${lishi.sum}</td>
            <td style="font-size: 22px;">${lishi.result}</td>
        </tr>
    </#list>

</table>
</body>
</html>