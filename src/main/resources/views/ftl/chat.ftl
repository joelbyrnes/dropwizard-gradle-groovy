<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <script src="/assets/jquery-1.11.1.min.js"></script>
    <script type='text/javascript'>

        var source;
        var counter;
        var channel = 'chat';

        function openChannel(username) {
            source = new EventSource("/channel/events?channel=" + channel + "&user=" + username);
            counter = 0;

            source.addEventListener('open', function(e) {
                log("Connected to channel " + channel);
                addChannelLine("Connected");
//                getChannelInfo();
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
                log("error: " + errorMsg);
            }, false);

            source.onmessage = function(event) {
                log("onmessage: " + event.data);
                handleData(event.data);
            };
        }

        function handleData(data) {
            var parsed = jQuery.parseJSON(data);
            addChannelLine("&lt;" + parsed.user + "&gt; " + parsed.message);
        }

        function addChannelLine(line) {
            document.getElementById("channelArea").innerHTML += line + "<br>";

            // scroll to bottom of div
            $("#channelArea").scrollTop($("#channelArea")[0].scrollHeight)
        }

        function log(msg) {
            // stupid IE doesn't support console, so processing stops. annoy the user with alerts instead.
            if (typeof(console) !== "undefined") console.log(msg);
//            else alert(msg);
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
            log(details);
        }

        function send(name, message) {
            var data = {user: name, message: message};
//            $.post("/chat/send", data);
            publish('message', data);
            // TODO handle failure, retry?
        }

        function publish(event, data) {
            $.post('/channel/' + channel + '/' + event, data);
        }

    </script>
    <title>Simple chat channel using HTML5 Server-Side Events</title>
</head>
<body>
Chat<br/>

<form method="GET" action="/channel/chat">
    Name: <input id="username" type="text" name="name">
    <#--&nbsp;&nbsp;&nbsp;Channel: <input id="channel" type="text" name="channel">-->
    <input type="submit" value="Connect" onclick="openChannel(getElementById('username').value); return false;">
</form>
<hr/>

<div id="channelArea" style="width: 100%; height: 200px; overflow: auto; display: block;"></div>

<form method="POST" action="/channel/chat/message">
    <input id="message" type="text" name="message">
    <input type="submit" value="Send" onclick="send(getElementById('username').value, getElementById('message').value); return false;">
</form>

</body>
</html>