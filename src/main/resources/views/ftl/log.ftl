<html>
<head>
    <title>Log</title>
    <script src="/assets/jquery-1.11.1.min.js"></script>

    <script type='text/javascript'>
        // alternatively get the ./info page and extract channel from that?
        var channel = "${channel?html}";

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

        function debugEvent(event) {
            var data = jQuery.parseJSON(event.data);
            writeObj(data, event.type + " event data" );
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

        function setUserCount(count) {
            document.getElementById('info').innerHTML = "Users: " + count;
        }

        function setupEventSource(path) {
            var source = new EventSource(path);

            source.addEventListener('open', function(e) {
                // Connection was opened.
                log("EventListener connected to " + path);

                $.get('/channel/' + channel + '/info', function(info) {
                    writeObj(info, "channel info");
                    setUserCount(info.userCount);
                });
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

            // generic event handler called if no specific event listener
            source.onmessage = function(event) {
                log("onmessage default event handler");
//                log("lastEventId: " + event.lastEventId);
                debugEvent(event);
            };

            source.addEventListener('server-message', function(event) {
                var data = jQuery.parseJSON(event.data);
                addMessage("server-message: " + data.message);
            }, false);

            source.addEventListener('channel-message', function(event) {
                var data = jQuery.parseJSON(event.data);
                addMessage("channel-message: " + data.message);
                document.getElementById('info').innerHTML = "Users: " + data.userCount
            }, false);

//            source.addEventListener('sound', function(event) {
//                log("sound event: " + event.data);
////                log("lastEventId: " + event.lastEventId);
//                var data = jQuery.parseJSON(event.data);
//                writeObj(event.data, event.type + " event data" );
//                addMessage("sound: " + data.sound);
//            }, false);
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
    <h2>Log</h2>
    <div id="info"></div>
    <br/>

    Alerts<br/>
    <div id="alerts"></div>
    <br/>

    <!--<input type="button" name="sound" value="Play sound locally" onclick="playLocal('2squeaks'); return false;" /><br/>-->

    Messages<br/>
    <div id="messages"></div>

</div>

</body>
</html>