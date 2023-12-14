<#import "/spring.ftl" as spring />
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
            max-height: 500px;
            overflow-y: auto;
        }

        .collapsed {
            max-height: 0;
            padding: 0;
        }

        .heading-container {
            display: flex;
            align-items: center;
        }

        .heading-container h2 {
            margin-right: 10px;
            flex-grow: 1;
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

        .form-container, .heading-container {
            margin-bottom: 20px;
        }

        form {
            background: #ffffff;
            padding: 15px;
            border: 1px solid #dee2e6;
            border-radius: 4px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        }

        button {
            background: #5C6BC0;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background: #3F51B5;
        }

        .collapsible-content {
            background: white;
            margin-bottom: 10px;
            border-radius: 4px;
        }

        .collapsible-content, .collapsed {
            border: 1px solid #ced4da;
            box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
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
    </script>
</head>
<body>
<h1>Messages List</h1>
<div class="form-container">
    <form method="GET" action="<@spring.url '/msgs/messages'/>">
        <button type="submit">Refresh</button>
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
        <button type="submit">Filter Messages</button>
    </form>
</div>
<#list messages as message>
    <div class="heading-container">
        <button onclick="toggleCollapse('collapsible-${message_index}')" style="margin-right: 5px">Show/Hide</button>
        <h2>Subject: ${message.timestamp()} || ${message.subject()} || Type: ${message.name()} </h2>
    </div>
    <pre id="collapsible-${message_index}" class="collapsible-content"></pre>
    <script>
        try {
            var decodedMessage = htmlDecode('${message.body()?json_string}');
            var messageData = JSON.parse(decodedMessage);
            var formattedJson = JSON.stringify(messageData, null, 4);
            document.getElementById('collapsible-${message_index}').textContent = formattedJson;
        } catch (error) {
            document.getElementById('collapsible-${message_index}').textContent = '${message.body()}';
        }
    </script>
</#list>
</body>
</html>
