<html>
<head>
    <title>Test</title>
    <script src="/assets/jquery-1.11.1.min.js"></script>

    <script type='text/javascript'>
        var channel = "${channel?html}";
        var source;

        window.onload = function(){
            if(typeof(EventSource) !== "undefined") {
//                setupEventSource("/channel/" + channel + "/events");
                setupEventSource("/channel/events?channel=" + channel);
            } else {
                log("EventSource is undefined");
                alert("Sorry, your browser does not support server-sent events, so it cannot receive events. You can still send events to other users though. ");
            }
        };

        function connect() {
            setupEventSource("/channel/events?channel=" + channel);
        }

        function disconnect() {
            source.close();
        }

        function quit() {
            $.post('/channels/quit', {message: 'bye'});
        }

        function plainMessage() {
            publish("plain", "plain message")
        }

        function publish(event, data) {
//            log("publishing: " + event + ", data: " + data);
            $.post('/channel/' + channel + '/' + event, data);
        }

        function log(msg) {
            // stupid IE doesn't support console, so processing stops. annoy the user with alerts instead.
            if (typeof(console) !== "undefined") console.log(msg);
//            else alert(msg);
        }

        function addLineToElement(div, msg) {
            var msgElement = document.getElementById(div);
            msgElement.innerHTML = msgElement.innerHTML + msg + "<br/>";
        }

        function addAlert(msg) {
            addLineToElement("alerts", msg);
        }

        function addMessage(msg) {
            addLineToElement("messages", msg);
        }

        function setupEventSource(path) {
            source = new EventSource(path);

            source.addEventListener('open', function(e) {
                // Connection was opened.
                log("EventListener connected to " + path);
            }, false);

            source.addEventListener('error', function(e) {
                console.log("error: " + e);
//                console.log("target: " + e.target);
//                console.log("target data: " + e.target.data);
                switch (e.target.readyState) {
                    // if reconnecting
                    case EventSource.CONNECTING:
                        log("Error. Reconnecting...");
                        break;
                    // if error was fatal
                    case EventSource.CLOSED:
                        // Connection was closed.
                        log("Error. Connection closed.");
                        break;
                }
            }, false);

            // generic event handler where event name not known
            source.onmessage = function(event) {
                log("onmessage default event handler: " + event.data);
//                log("event name: " + event.name);
                writeObj(event)
            };

            // event with no type defaults to message
            source.addEventListener('message', function(event) {
                log("message event: " + event.data);
//                log("lastEventId: " + event.lastEventId);
                var data = jQuery.parseJSON(event.data);
                addMessage("message: " + data.message)
            }, false);

            source.addEventListener('server-message', function(event) {
                var data = jQuery.parseJSON(event.data);
                addMessage("server-message: " + data.message)
            }, false);

            source.addEventListener('channel-message', function(event) {
                var data = jQuery.parseJSON(event.data);
                addMessage("channel-message: " + data.message)
            }, false);

            source.addEventListener('sound', function(event) {
                log("sound event: " + event.data);
                log("event name: " + event.name);
//                log("lastEventId: " + event.lastEventId);
                var data = jQuery.parseJSON(event.data);
                log("event data: " + data);
                addMessage("sound: " + data.sound);
            }, false);
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

    </script>
    <style>
        .centered {
            text-align: center;
        }
    </style>
</head>

<body>

<div class="centered">
    <h2>Test</h2>

    Alerts<br/>
    <div id="alerts"></div>
    <br/>

    <input type="button" name="sound" value="Disconnect" onclick="disconnect(); return false;" /><br/>
    <input type="button" name="sound" value="Connect" onclick="connect(); return false;" /><br/>
    <input type="button" name="sound" value="Quit" onclick="quit(); return false;" /><br/>
    <input type="button" name="sound" value="Send plain message" onclick="plainMessage(); return false;" /><br/>

    Messages<br/>
    <div id="messages"></div>

</div>

</body>
</html>