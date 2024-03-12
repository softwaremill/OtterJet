<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html>
<head>
    <title>OtterJet</title>
    <link rel="stylesheet" href="/css/index.css">
    <style>
        h1 {
            color: #333;
            text-align: center;
        }

        nav {
            background-color: #333;
            color: #fff;
            padding: 15px;
        }

        nav ul {
            list-style-type: none;
            padding: 0;
        }

        nav ul li {
            display: inline;
            margin-right: 10px;
        }

        nav ul li a {
            display: inline-block;
            color: #fff;
            text-decoration: none;
            background-color: #5C6BC0;
            padding: 10px 15px;
            border-radius: 4px;
            transition: background-color 0.3s ease;
        }

        nav ul li a:hover {
            background-color: #3F51B5;
        }

        .header {
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .header img {
            margin-right: 20px;
            width: 4%;
        }

    </style>
</head>
<body>
<div class="header">
    <img id="logo" src="<@spring.url '/images/otterjet-logo.png'/>" alt="OtterJet Logo">
    <h1>Welcome to the OtterJet</h1>
</div>
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
