<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html>
<head>
    <title>JetStreamDrop</title>
    <link rel="stylesheet" href="/css/index.css">
</head>
<body>
<h1>Welcome to the JetStreamDrop</h1>
<nav>
    <ul>
        <li><a href="<@spring.url '/msgs'/>">Messages List</a></li>
        <#if isMonitoringEnabled>
            <li><a href="<@spring.url '/monitoring'/>">Monitoring</a></li>
        <#else>
            <li>Monitoring is not configured. You can configure it to enable the view. ADD LINK TO README HOW TO DO IT
            </li>
        </#if>
    </ul>
</nav>
</body>
</html>
