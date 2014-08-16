<html>
<head>
    <title>Test</title>
    <script src="/assets/jquery-1.11.1.min.js"></script>

    <script type='text/javascript'>
        // alternatively get the ./info page and extract channel from that?
        var channel = "${channel?html}";
        var source;

        window.onload = function(){
            if(typeof(EventSource) !== "undefined") {
                connect(channel, 'test');
            } else {
                log("EventSource is undefined");
                alert("Sorry, your browser does not support server-sent events, so it cannot receive events. You can still send events to other users though. ");
            }
        };

        function connect(channel, user) {
//            setupEventSource("/channel/" + channel + "/events");
            setupEventSource("/channel/events?user=" + user + "&channel=" + channel);
        }

        function disconnect() {
            source.close();
        }

        function quit() {
            $.post('/channels/quit', {message: 'bye'});
        }

        function publish(event, data) {
//            log("publishing: " + event + ", data: " + data);
            $.post('/channel/' + channel + '/' + event, data);
        }

        function ping() {
            log("sending ping");
            publish("ping", {time: new Date().getTime()})
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

            // generic event handler called if no specific event listener
            source.onmessage = function(event) {
                log("onmessage default event handler");
                log(event.type + " event: " + event.data);
//                log("lastEventId: " + event.lastEventId);
                var data = jQuery.parseJSON(event.data);
                writeObj(data, event.type + " event data" );
            };

            // event with no type defaults to message
            source.addEventListener('message', function(event) {
                log("message event: " + event.data);
//                log("lastEventId: " + event.lastEventId);
                var data = jQuery.parseJSON(event.data);
                addMessage("message: " + data.message);
            }, false);

            source.addEventListener('server-message', function(event) {
                var data = jQuery.parseJSON(event.data);
                addMessage("server-message: " + data.message);
            }, false);

            source.addEventListener('channel-message', function(event) {
                var data = jQuery.parseJSON(event.data);
                addMessage("channel-message: " + data.message);
                document.getElementById('info').innerHTML = "Users: " + data.userCount
            }, false);

            source.addEventListener('sound', function(event) {
                var data = jQuery.parseJSON(event.data);
                addMessage("sound: " + data.url);
                playSound(data.url);
            }, false);

            source.addEventListener('ping', function(event) {
                var received = new Date().getTime();
                var data = jQuery.parseJSON(event.data);
                var sent = Number(data.time);
                log("ping time: " + sent);
//                log("ping time isNumeric: " + data.time.isNumeric());
                log("received time: " + received);
                addMessage("ping time: " + sent + ", round trip time: " + (received - sent) + "ms");
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

        function playSound(soundUrl) {
            if (typeof(Audio) == "undefined") {
                addMessage("This browser cannot play sounds using HTML5 Audio.")
            } else {
                addMessage("Playing sound " + soundUrl + "");
                var snd = new Audio(soundUrl); // buffers automatically when created
                snd.play();
            }
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
    <div id="info">Users: ?</div>
    <br/>

    Alerts<br/>
    <div id="alerts"></div>
    <br/>

    <input type="button" name="sound" value="Disconnect" onclick="disconnect(); return false;" /><br/>
    <input type="button" name="sound" value="Connect" onclick="connect(); return false;" /><br/>
    <input type="button" name="sound" value="Quit" onclick="quit(); return false;" /><br/>
    <input type="button" name="sound" value="Send unhandled event message" onclick="publish('weird', {message: 'a message'}); return false;" /><br/>
    <input type="button" name="sound" value="Squeak" onclick="publish('sound', {url: '/assets/sounds/2squeaks.wav'}); return false;" /><br/>
    <input type="button" name="sound" value="Ping" onclick="ping(); return false;" /><br/>

    <br/>
    Messages<br/>
    <div id="messages"></div>

</div>

</body>
</html>