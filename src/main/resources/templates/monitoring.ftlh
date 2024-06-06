<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html>
<head>
    <title>OtterJet</title>
    <link rel="stylesheet" href="/css/index.css">
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        th {
            background-color: #f1f1f1;
            color: #333;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>
<h1>Monitoring</h1>

<h2>Overall Metrics</h2>
<table>
    <tr>
        <th>Streams</th>
        <th>Consumers</th>
        <th>Messages</th>
    </tr>
    <tr>
        <td>${metrics.streams()}</td>
        <td>${metrics.consumers()}</td>
        <td>${metrics.messages()}</td>
    </tr>
</table>

<h2>Account Details</h2>
<#list metrics.accountDetails() as account>
    <h3>Account: ${account.name()}</h3>
    <table>
        <tr>
            <th>Stream Name</th>
            <th>Subjects</th>
            <th>Messages</th>
            <th>Consumers</th>
        </tr>
        <#list account.streamDetails() as stream>
            <tr>
                <td>${stream.name()}</td>
                <td>
                    <#list stream.config().subjects() as subject>
                        ${subject}<#if subject_has_next>, </#if>
                    </#list>
                </td>
                <td>${stream.state().messages()}</td>
                <td>${stream.state().consumers()}</td>
            </tr>
        </#list>
    </table>
</#list>
<nav>
    <ul>
        <li><a href="<@spring.url '/'/>">Back</a></li>
    </ul>
</nav>
</body>
</html>
