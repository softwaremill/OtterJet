<!DOCTYPE html>
<html>
<head>
    <title>Jetstream Messages Display</title>
    <style>
        .collapsible-content {
            border: 1px solid #ccc;
            padding: 10px;
            background-color: #f9f9f9;
            white-space: pre-wrap;
            word-wrap: break-word;
            overflow: hidden;
            transition: max-height 0.2s ease-out;
            max-height: 500px; /* Adjust this value based on your preference */
            overflow-y: auto; /* Makes the content scrollable */
        }
        .collapsed {
            max-height: 0;
            padding: 0;
        }
        .heading-container {
            display: flex;
            align-items: center; /* Aligns items vertically */
        }
        .heading-container h2 {
            margin-right: 10px; /* Adds some space between the heading and the button */
            flex-grow: 1; /* Allows the heading to take up the available space */
        }
    </style>
</head>
<body>
<h1>Messages List</h1>
<#list messages as message>
    <div class="heading-container">
        <button onclick="toggleCollapse('collapsible-${message_index}')" style="margin-right: 5px">Show/Hide</button>
        <h2>Subject: ${message.timestamp()} || ${message.subject()} || Type: ${message.name()} </h2>
    </div>
    <pre id="collapsible-${message_index}" class="collapsible-content"></pre>
    <script>
        function htmlDecode(input) {
            var doc = new DOMParser().parseFromString(input, "text/html");
            return doc.documentElement.textContent;
        }

        function toggleCollapse(id) {
            var element = document.getElementById(id);
            element.classList.toggle("collapsed");
        }

        try {
            var decodedMessage = htmlDecode('${message.body()?json_string}');
            var messageData = JSON.parse(decodedMessage);
            var formattedJson = JSON.stringify(messageData, null, 4);
            document.getElementById('collapsible-${message_index}').textContent = formattedJson;
        } catch (error) {
            console.error('Error parsing JSON message:', error);
            document.getElementById('collapsible-${message_index}').textContent = 'Error in JSON format';
        }
    </script>
</#list>
</body>
</html>
