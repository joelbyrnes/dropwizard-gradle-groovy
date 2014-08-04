<html>
<head>
    <title>Log</title>
    <script src="/assets/jquery-1.11.1.min.js"></script>

    <script type='text/javascript'>
        // TODO get from request
        var channel = "pig";

        window.onload = function(){
//            log("called onload");

            if(typeof(EventSource) !== "undefined") {
//                setupEventSource("/channel/events");
                setupEventSource("/channel/events?channel=" + channel);
            } else {
                log("EventSource is undefined");
                addAlert("Sorry, your browser does not support server-sent events, so it cannot receive sound events. You can still play sounds to other users though. ");
            }
        };

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
            var source = new EventSource(path);

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

//            source.onmessage = function(event) {
//                log("onmessage default event handler: " + event.data);
//            };

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
//                log("lastEventId: " + event.lastEventId);
                var data = jQuery.parseJSON(event.data);
                log("event data: " + data);
                addMessage("sound: " + data.sound);
            }, false);
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
    <h2>Log</h2>

    Alerts<br/>
    <div id="alerts"></div>
    <br/>

    <!--<input type="button" name="sound" value="Play sound locally" onclick="playLocal('2squeaks'); return false;" /><br/>-->

    Messages<br/>
    <div id="messages"></div>

</div>

</body>
</html>