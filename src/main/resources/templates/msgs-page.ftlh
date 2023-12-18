<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html>
<head>
    <title>Jetstream Messages Display</title>
    <style>

        /* Base styles for the message block */
        .message {
            padding-top: 10px;
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
            width: 100px;
            text-align: center;
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
            font-size: 1.1em;
            font-weight: bold;
            color: #333;
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

        .controls {
            padding-top: 10px;
            padding-bottom: 10px;
        }

        .controls__forms {
            display: flex;
            align-items: center;
        }

        .controls__form {
            display: flex;
            align-items: center;
        }

        .controls__form--refresh {
            margin-left: 10px;
        }

        .controls__field {
            display: flex;
            align-items: center;
            margin-right: 10px;
        }

        .controls__label {
            margin-right: 5px;
            white-space: nowrap;
        }

        .controls__input {
            /* Styles for your input fields */
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .controls__button {
            /* Styles for the filter button */
            background: #5C6BC0;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 4px;
            cursor: pointer;
        }

        .controls__button:hover {
            background: #3F51B5;
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

        function toggleCollapse(contentId, button) {
            var element = document.getElementById(contentId);
            element.classList.toggle("collapsed");

            // Update button text based on the state
            if (element.classList.contains("collapsed")) {
                button.textContent = "Show";
            } else {
                button.textContent = "Hide";
            }
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
<div class="controls">
    <div class="controls__forms">
        <form method="GET" action="<@spring.url '/msgs/filterMessages'/>" class="controls__form">
            <div class="controls__field">
                <label for="topic" class="controls__label">Topic:</label>
                <input type="text" id="topic" name="topic" value="${topic}" class="controls__input">
            </div>
            <div class="controls__field">
                <label for="type" class="controls__label">Message type:</label>
                <input type="text" id="type" name="type" value="${type}" class="controls__input">
            </div>
            <button type="submit" class="controls__button">Filter Messages</button>
        </form>

        <form method="GET" action="<@spring.url '/msgs/messages'/>" class="controls__form controls__form--refresh">
            <button type="submit" class="controls__button controls__button--refresh">Refresh</button>
        </form>
    </div>
</div>

<#list messages as message>
    <div class="message">
        <button class="message__toggle-button" onclick="toggleCollapse('collapsible-${message_index}', this)"
                id="toggle-button-${message_index}">Hide
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
