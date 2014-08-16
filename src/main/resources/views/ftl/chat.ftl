<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
    <script type='text/javascript'>

        var source;
        var counter;

        function openChannel(name) {
            source = new EventSource("/chat/channel?name=" + name);
            counter = 0;

            source.addEventListener('open', function(e) {
                console.log("open: " + e.data);
            }, false);

            source.addEventListener('error', function(e) {
                writeObj(e);
                var errorMsg = "unknown"
                switch (e.target.readyState) {
                    // if reconnecting
                    case EventSource.CONNECTING:
                        errorMsg = 'Reconnecting...';
                        break;
                    // if error was fatal
                    case EventSource.CLOSED:
                        // Connection was closed.
                        errorMsg = "Connection closed";
                        break;
                }
                console.log("error: " + errorMsg);
            }, false);

            source.onmessage = function(event) {
                console.log("onmessage: " + event.data);
                handleData(event.data);
            };

            function handleData(data) {
                // TODO catch SyntaxError
                var parsed = jQuery.parseJSON(data);

                document.getElementById("channelArea").innerHTML += "&lt;" + parsed.user + "&gt; " + parsed.message + "<br>";

                // scroll to bottom of div
                $("#channelArea").scrollTop($("#channelArea")[0].scrollHeight)
            }

            function writeObj(obj, message) {
                if (!message) {
                    message = obj;
                }
                var details = "*****************" + "\n" + message + "\n";
                var fieldContents;
                for (var field in obj) {
                    fieldContents = obj[field];
                    if (typeof(fieldContents) == "function") {
                        fieldContents = "(function)";
                    }
                    details += "  " + field + ": " + fieldContents + "\n";
                }
                console.log(details);
            }
        }

        function send(name, message) {
            data = {"message": message};
            $.post("/chat/send", data);
            // TODO handle failure, retry?
        }

    </script>
    <title>Simple chat channel using HTML5 Server-Side Events</title>
</head>
<body>
Chat<br/>

<form method="GET" action="/chat">
    Name: <input id="username" type="text" name="name">&nbsp;&nbsp;&nbsp;Channel: <input id="channel" type="text" name="channel">
    <input type="submit" value="Connect" onclick="openChannel(getElementById('username').value); return false;">
</form>
<hr/>

<div id="channelArea" style="width: 100%; height: 200px; overflow: auto; display: block;"></div>

<form method="POST" action="/chat/send">
    <input id="message" type="text" name="message">
    <input type="submit" value="Send" onclick="send(getElementById('username').value, getElementById('message').value); return false;">
</form>

</body>
</html>