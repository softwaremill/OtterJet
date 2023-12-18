<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html>
<head>
    <title>Jetstream Messages Display</title>
    <style>

        /* Base styles for the message block */
        .message {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
            font-family: 'Roboto', sans-serif;
        }

        /* Styles for the Show/Hide button within the message */
        .message__toggle-button {
            background: #5C6BC0;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 4px;
            cursor: pointer;
            margin-right: 10px;
        }

        .message__toggle-button:hover {
            background: #3F51B5;
        }

        /* Styles for the message details container */
        .message__details {
            display: flex;
            align-items: center;
            flex-wrap: wrap;
            font-size: 0.9em;
        }

        /* Styles for each detail within the message */
        .message__detail {
            margin-right: 15px;
            color: #212529; /* Adjust the color as needed */
        }

        /* Styles for the collapsible content */
        .message__content {
            background: white;
            border: 1px solid #ced4da;
            border-radius: 4px;
            box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
            margin-bottom: 10px;
            white-space: pre-wrap;
            word-wrap: break-word;
            overflow: hidden;
            max-height: 500px;
            padding: 10px;
            transition: max-height 0.2s ease-out, padding 0.2s ease-out;
            opacity: 1;
            overflow-y: auto;
        }

        .message__content.collapsed {
            max-height: 0;
            padding: 0;
        }

        .form-container {
            display: flex;
            align-items: center;
            padding: 10px;
        }

        .form-container form {
            margin: 0 20px;
        }

        body {
            font-family: 'Roboto', sans-serif;
            line-height: 1.6;
            margin: 0;
            padding: 20px;
            background-color: #f8f9fa;
            color: #212529;
        }

        h1 {
            color: #444444;
        }

        .controls__refresh-button, .controls__filter-button {
            background: #5C6BC0;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 4px;
            cursor: pointer;
        }

        .controls__refresh-button:hover, .controls__filter-button:hover {
            background: #3F51B5;
        }

        .form-container {
            margin-bottom: 20px;
        }

        form {
            background: #ffffff;
            padding: 15px;
            border: 1px solid #dee2e6;
            border-radius: 4px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        }

        input[type="text"]:focus, button:focus {
            outline: none;
            border-color: #80bdff;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
        }

        .form-field {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
        }

        label {
            flex-basis: 30%;
            margin-right: 10px;
        }

        input[type="text"] {
            flex-grow: 1;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

    </style>
    <script>
        function htmlDecode(input) {
            var doc = new DOMParser().parseFromString(input, "text/html");
            return doc.documentElement.textContent;
        }

        function toggleCollapse(id) {
            var element = document.getElementById(id);
            element.classList.toggle("collapsed");
        }

        function formatTimestamp(timestamp) {
            var date = new Date(timestamp);
            var options = {year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit'};
            return date.toLocaleDateString(undefined, options);
        }
    </script>
</head>
<body>
<h1>Messages List</h1>
<div class="form-container">
    <form method="GET" action="<@spring.url '/msgs/messages'/>">
        <button class="controls__refresh-button" type="submit">Refresh</button>
    </form>

    <form method="GET" action="<@spring.url '/msgs/filterMessages'/>">
        <div class="form-field">
            <label for="topic">Topic:</label>
            <input type="text" id="topic" name="topic" value="${topic}">
        </div>
        <div class="form-field">
            <label for="type">Message type:</label>
            <input type="text" id="type" name="type" value="${type}">
        </div>
        <button class="controls__filter-button" type="submit">Filter Messages</button>
    </form>
</div>
<#list messages as message>
    <div class="message">
        <button class="message__toggle-button" onclick="toggleCollapse('collapsible-${message_index}')">Show/Hide
        </button>
        <div class="message__details">
            <div class="message__detail">Subject: ${message.subject()}</div>
            <div class="message__detail">Timestamp: <span class="message__timestamp"
                                                          data-timestamp="${message.timestamp()}"></span></div>
            <#if message.name()?has_content>
                <div class="message__detail">Type: ${message.name()}</div>
            </#if>
        </div>
    </div>
    <pre id="collapsible-${message_index}" class="message__content"></pre>

    <script>
        try {
            var decodedMessage = htmlDecode('${message.body()?json_string}');
            var messageData = JSON.parse(decodedMessage);
            var formattedJson = JSON.stringify(messageData, null, 4);
            document.getElementById('collapsible-${message_index}').textContent = formattedJson;
        } catch (error) {
            document.getElementById('collapsible-${message_index}').textContent = '${message.body()}';
        }

        document.addEventListener('DOMContentLoaded', (event) => {
            var timestamps = document.querySelectorAll('.message__timestamp');
            timestamps.forEach(function (elem) {
                var timestamp = elem.getAttribute('data-timestamp');
                elem.textContent = formatTimestamp(timestamp);
            });
        });
    </script>
</#list>
</body>
</html>
